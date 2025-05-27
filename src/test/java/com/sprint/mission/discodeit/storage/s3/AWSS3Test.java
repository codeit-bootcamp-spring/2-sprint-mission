package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.config.AWSS3Properties;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@SpringBootTest
@ActiveProfiles("test")
public class AWSS3Test {

  @Autowired
  private AWSS3Properties properties;

  @Mock
  private S3Client s3Client;

  @Mock
  private S3Presigner s3Presigner;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("S3 파일 업로드 - putObject 호출 검증")
  void uploadFile() {
    File file = new File("src/test/resources/sample.txt");

    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(properties.getBucket())
        .key("test/upload.txt")
        .contentType("text/plain")
        .build();

    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenReturn(PutObjectResponse.builder().eTag("mock-etag").build());

    s3Client.putObject(request, RequestBody.fromFile(file));
  }

  @Test
  @DisplayName("S3 파일 다운로드 - getObject 호출 후 파일 생성 확인")
  void downloadFile() throws IOException {
    File file = new File("src/test/resources/download.txt");

    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key("test/upload.txt")
        .build();

    when(s3Client.getObject(any(GetObjectRequest.class), any(Path.class)))
        .thenAnswer(invocation -> {
          Files.writeString(file.toPath(), "mock content");
          return null;
        });

    s3Client.getObject(request, file.toPath());
  }

  @Test
  @DisplayName("Presigned URL 생성 - presignGetObject 호출 및 반환 URL 검증")
  void presignedUrlTest() throws MalformedURLException {
    URL mockUrl = new URL("https://mock-url.com/file");

    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key("test/upload.txt")
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .getObjectRequest(request)
        .build();

    PresignedGetObjectRequest mockedPresigned = mock(PresignedGetObjectRequest.class);
    when(mockedPresigned.url()).thenReturn(mockUrl);

    when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .thenReturn(mockedPresigned);

    URL url = s3Presigner.presignGetObject(presignRequest).url();

    assertEquals(mockUrl, url);
  }
}
