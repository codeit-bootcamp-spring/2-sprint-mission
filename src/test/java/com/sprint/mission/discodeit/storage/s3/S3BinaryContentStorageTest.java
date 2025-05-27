package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayOutputStream;

import java.util.UUID;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
public class S3BinaryContentStorageTest {

  private static S3BinaryContentStorage storage;

  @BeforeAll
  static void setUp() {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    // 시스템 속성으로도 등록 (CI에서 .env 없이도 동작하도록)
    dotenv.entries().forEach(entry -> {
      if (System.getProperty(entry.getKey()) == null) {
        System.setProperty(entry.getKey(), entry.getValue());
      }
    });

    String accessKey = dotenv.get("AWS_S3_ACCESS_KEY");
    String secretKey = dotenv.get("AWS_S3_SECRET_KEY");
    String region = dotenv.get("AWS_S3_REGION");
    String bucket = dotenv.get("AWS_S3_BUCKET");

    storage = new S3BinaryContentStorage(accessKey, secretKey, region, bucket);
  }

  @Test
  @DisplayName("S3에 파일을 업로드, 다운로드, 삭제하는 테스트")
  void shouldUploadDownloadAndDeleteFromS3() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    String content = "S3 테스트용";
    byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

    // when - 업로드
    UUID uploadedId = storage.put(id, bytes);

    // then - 다운로드 확인
    InputStream inputStream = storage.get(uploadedId);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while ((length = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, length);
    }
    String downloadedContent = outputStream.toString(StandardCharsets.UTF_8);
    assertThat(downloadedContent).isEqualTo(content);
    System.out.println("다운로드 결과: " + downloadedContent);

    // cleanup - 삭제
    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
        .bucket(storage.getBucket())
        .key(uploadedId.toString())
        .build();

    storage.getS3Client().deleteObject(deleteRequest);

    System.out.println("삭제 완료: " + uploadedId);
  }

  @Test
  @DisplayName("S3Client가 정상 생성됨")
  void shouldCreateS3Client() {
    assertThat(storage.getS3Client()).isNotNull();
  }

  @Test
  @DisplayName("Presigned URL이 정상 생성됨")
  void shouldGeneratePresignedUrl() {
    String key = "test/sample.txt";
    String contentType = "text/plain";

    String url = storage.generatePresignedUrl(key, contentType);

    System.out.println("Generated URL: " + url);

    assertThat(url).startsWith("https://");
    assertThat(url).contains(key);
  }

  @Test
  @DisplayName("download()는 302 리디렉트와 Location 헤더를 반환한다")
  void downloadShouldReturnRedirectWithLocation() {
    BinaryContentDto dto = new BinaryContentDto(
        UUID.randomUUID(),
        "test.txt",
        1234L,
        "text/plain"
    );

    ResponseEntity<Void> response = storage.download(dto);

    assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
    assertThat(response.getHeaders().getLocation()).isNotNull();
    System.out.println("Redirect URL: " + response.getHeaders().getLocation());
  }
}
