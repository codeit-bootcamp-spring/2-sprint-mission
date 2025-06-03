package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class S3BinaryContentStorageTest {

  private S3BinaryContentStorage s3Storage;

  @BeforeEach
  void setUp() {
    s3Storage = new S3BinaryContentStorage(
        System.getenv("AWS_S3_ACCESS_KEY"),
        System.getenv("AWS_S3_SECRET_KEY"),
        System.getenv("AWS_S3_REGION"),
        System.getenv("AWS_S3_BUCKET"),
        600
    );
  }

  @Test
  void testPutAndGet() {
    UUID id = UUID.randomUUID();
    byte[] content = "Hello, S3!".getBytes();

    UUID resultId = s3Storage.put(id, content);
    assertThat(resultId).isEqualTo(id);

    InputStream inputStream = s3Storage.get(resultId);
    assertThat(inputStream).isNotNull();
  }

  @Test
  void testDownloadPresignedUrlRedirect() {
    UUID id = UUID.randomUUID();
    byte[] content = "Presigned URL Test".getBytes();

    s3Storage.put(id, content);

    BinaryContentDto metaData = new BinaryContentDto(
        id,
        "test.txt",
        (long) content.length,
        "text/plain"
    );

    ResponseEntity<?> response = s3Storage.download(metaData);
    assertThat(response.getStatusCodeValue()).isEqualTo(302);
    assertThat(response.getHeaders().getLocation()).isNotNull();
    assertThat(response.getHeaders().getLocation().toString()).contains("https://");
  }
}
