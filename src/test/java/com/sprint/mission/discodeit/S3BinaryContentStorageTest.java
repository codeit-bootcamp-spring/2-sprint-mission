package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.S3BinaryContentStorage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {


  @Mock
  private S3Client s3Client;

  @Mock
  private S3Presigner s3Presigner;

  private S3BinaryContentStorage storage;

  @BeforeEach
  void setUp() {
    storage = new S3BinaryContentStorage("fake-key", "fake-secret", "ap-northeast-2",
        "test-bucket") {
      @Override
      protected S3Client getS3Client() {
        return s3Client; // override for mocking
      }

      @Override
      protected String generatePresignedUrl(String key, String contentType) {
        return "http://fake-presigned-url"; // override
      }
    };
  }

  @Test
  void put_Success() {
    UUID id = UUID.randomUUID();
    byte[] data = "test data".getBytes();

    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenReturn(PutObjectResponse.builder().build());

    UUID result = storage.put(id, data);

    assertEquals(id, result);
  }

  @Test
  void get_Success() {
    UUID id = UUID.randomUUID();

    ByteArrayInputStream contentStream = new ByteArrayInputStream("mock data".getBytes());
    GetObjectResponse getObjectResponse = GetObjectResponse.builder().build();
    ResponseInputStream<GetObjectResponse> responseInputStream =
        new ResponseInputStream<>(getObjectResponse, contentStream);

    when(s3Client.getObject(any(GetObjectRequest.class)))
        .thenReturn(responseInputStream);
    
    InputStream result = storage.get(id);

    assertNotNull(result);
    verify(s3Client).getObject(any(GetObjectRequest.class));
  }

  @Test
  void download_Success() {
    UUID id = UUID.randomUUID();
    BinaryContentDto dto = new BinaryContentDto(id, "test", 0L, "image/png");

    ResponseEntity<?> response = storage.download(dto);

    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertTrue(response.getHeaders().containsKey(HttpHeaders.LOCATION));
  }
}