package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
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
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
public class S3BinaryContentStorageTest {

  private S3BinaryContentStorage storage;

  @Mock
  private S3Client s3Client;

  @Mock
  private S3Presigner s3Presigner;

  private final String accessKey = "test-key";
  private final String secretKey = "test-secret";
  private final String region = "ap-northeast-2";
  private final String bucket = "test-bucket";

  @BeforeEach
  void setup() throws Exception {
    storage = spy(new S3BinaryContentStorage(accessKey, secretKey, region, bucket));

    // 프라이빗 필드 presignedUrlExpiration 설정 (600초)
    Field expirationField = S3BinaryContentStorage.class.getDeclaredField("presignedUrlExpiration");
    expirationField.setAccessible(true);
    expirationField.set(storage, 600L);

    lenient().doReturn(s3Client).when(storage).getS3Client();
    lenient().doReturn(s3Presigner).when(storage).getS3Presigner();
  }

  @Test
  void testPutMethod() {
    // Given
    UUID contentId = UUID.randomUUID();
    byte[] testData = "test-content".getBytes();

    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenReturn(PutObjectResponse.builder().build());

    // When
    UUID result = storage.put(contentId, testData);

    // Then
    assertEquals(contentId, result);
    verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

  @Test
  void testGetMethod() {
    // Given
    UUID contentId = UUID.randomUUID();
    InputStream inputStream = new ByteArrayInputStream("test-data".getBytes());
    ResponseInputStream<GetObjectResponse> mockResponse =
        new ResponseInputStream<>(GetObjectResponse.builder().build(),
            AbortableInputStream.create(inputStream));

    when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockResponse);

    // When & Then
    assertNotNull(storage.get(contentId));
    verify(s3Client).getObject(any(GetObjectRequest.class));
  }

  @Test
  void testDownloadMethod() throws Exception {
    // Given
    UUID contentId = UUID.randomUUID();
    BinaryContentDto meta = new BinaryContentDto(contentId, "file.txt", 1024L, "text/plain");
    String expectedUrl = "https://test.url";
    doReturn(expectedUrl).when(storage).generatePresignedUrl(contentId.toString(), "text/plain");

    // When
    ResponseEntity<Void> response = storage.download(meta);

    // Then
    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertEquals(expectedUrl, response.getHeaders().getFirst(HttpHeaders.LOCATION));
  }

  @Test
  void testPresignedUrlGeneration() throws Exception {
    // Given
    class TestStorage extends S3BinaryContentStorage {

      TestStorage() {
        super(accessKey, secretKey, region, bucket);
      }

      @Override
      public S3Presigner getS3Presigner() {
        return s3Presigner;
      }
    }

    TestStorage testStorage = new TestStorage();

    Field expirationField = S3BinaryContentStorage.class.getDeclaredField("presignedUrlExpiration");
    expirationField.setAccessible(true);
    expirationField.set(testStorage, 600L);

    PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
    URL expectedUrl = new URL("https://expected.url");

    // When
    when(presignedRequest.url()).thenReturn(expectedUrl);
    when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .thenReturn(presignedRequest);

    // Then
    String result = testStorage.generatePresignedUrl("test-key", "text/plain");
    assertEquals(expectedUrl.toString(), result);
    verify(s3Presigner).presignGetObject(any(GetObjectPresignRequest.class));
  }

}
