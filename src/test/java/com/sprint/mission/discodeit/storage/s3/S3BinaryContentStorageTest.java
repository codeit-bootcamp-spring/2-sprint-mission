package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class S3BinaryContentStorageTest {

  private S3Client mockS3Client;
  private S3Presigner mockS3Presigner;
  private S3BinaryContentStorage storage;

  private final String accessKeyId = "testAccessKey";
  private final String secretKey = "testSecretKey";
  private final String region = "us-west-2";
  private final String bucket = "test-bucket";
  private final long presignedDuration = 3600;

  @BeforeEach
  void setUp() {
    mockS3Client = mock(S3Client.class);
    mockS3Presigner = mock(S3Presigner.class);

    // 실제 클래스는 spy로 감싸서 내부 메서드 mocking
    storage = spy(new S3BinaryContentStorage(accessKeyId, secretKey, region, bucket, presignedDuration));

    doReturn(mockS3Client).when(storage).getS3Client();
    doReturn(mockS3Presigner).when(storage).getS3Presigner();
  }

  @Test
  void put_shouldUploadFileToS3() {
    UUID id = UUID.randomUUID();
    byte[] content = "test content".getBytes();

    given(mockS3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .willReturn(PutObjectResponse.builder().build());

    UUID result = storage.put(id, content);

    assertThat(result).isEqualTo(id);
    verify(mockS3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

  @Test
  void get_shouldReturnInputStream() {
    UUID id = UUID.randomUUID();
    ResponseInputStream<GetObjectResponse> mockStream = mock(ResponseInputStream.class);

    given(mockS3Client.getObject(any(GetObjectRequest.class))).willReturn(mockStream);

    InputStream result = storage.get(id);

    assertThat(result).isNotNull();
    verify(mockS3Client).getObject(any(GetObjectRequest.class));
  }

  @Test
  void generatePresignedUrl_shouldReturnUrlString() throws Exception {
    String key = "sample-key";
    String contentType = "application/octet-stream";
    URL url = new URL("https://example.com/presigned-url");

    PresignedGetObjectRequest mockPresignedRequest = mock(PresignedGetObjectRequest.class);
    given(mockPresignedRequest.url()).willReturn(url);
    given(mockS3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .willReturn(mockPresignedRequest);

    String result = storage.generatePresignedUrl(key, contentType);

    assertThat(result).isEqualTo(url.toString());
    verify(mockS3Presigner).presignGetObject(any(GetObjectPresignRequest.class));
  }

  @Test
  void download_shouldReturnRedirectResponse() {
    UUID id = UUID.randomUUID();
    String contentType = "image/png";
    String fileName = "file1";
    Long size = 5L;
    String presignedUrl = "https://example.com/file";

    doReturn(presignedUrl).when(storage).generatePresignedUrl(id.toString(), contentType);

    BinaryContentDto dto = new BinaryContentDto(id, fileName, size, contentType);

    ResponseEntity<?> response = storage.download(dto);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    assertThat(response.getHeaders().getLocation()).hasToString(presignedUrl);
  }
}
