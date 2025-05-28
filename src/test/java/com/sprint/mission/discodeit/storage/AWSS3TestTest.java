package com.sprint.mission.discodeit.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.storage.s3.AWSS3Test;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AWSS3TestTest {

  @Autowired
  private AWSS3Test awsS3Test;

  @BeforeAll
  static void loadEnv() {
    Dotenv dotenv = Dotenv.configure()
        .ignoreIfMissing()
        .load();

    dotenv.entries().forEach(entry -> {
      if (System.getProperty(entry.getKey()) == null) {
        System.setProperty(entry.getKey(), entry.getValue());
      }
    });
  }

  @Test
  void testUploadAndDownload() throws Exception {
    String content = "Hello S3!";
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "test.txt", "text/plain", content.getBytes(StandardCharsets.UTF_8));

    String uploadedUrl = awsS3Test.upload(mockFile);
    assertNotNull(uploadedUrl);

    // Key 추출
    URI uri = new URI(uploadedUrl);
    String key = uri.getPath().substring(1);

    byte[] downloaded = awsS3Test.download(key);
    assertNotNull(downloaded);
    assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), downloaded);
  }

  @Test
  void testPresignedUrl() {
    String key = "d56ba30d-459b-46ae-a0c5-34b87fed7975-test.txt";

    String url = awsS3Test.generatePresignedUrl(key, Duration.ofMinutes(10));

    assertNotNull(url);
  }
}
