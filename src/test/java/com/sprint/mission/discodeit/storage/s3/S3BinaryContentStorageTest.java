package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(initializers = S3BinaryContentStorageTest.EnvInitializer.class)
class S3BinaryContentStorageTest {

  @Autowired
  BinaryContentStorage storage;

  UUID testId;
  byte[] testData;

  @BeforeEach
  void setUp() {
    testId = UUID.randomUUID();
    testData = "Hello, S3 Test!".getBytes();
  }

  @Test
  void putAndGet() {
    UUID storedId = storage.put(testId, testData);
    assertThat(storedId).isEqualTo(testId);

    InputStream in = storage.get(testId);
    assertThat(in).isNotNull();
  }

  @Test
  void downloadPresignedUrl() {
    storage.put(testId, testData);
    BinaryContentDto dto = new BinaryContentDto(testId, null, null, null);

    var response = storage.download(dto);
    assertThat(response.getStatusCodeValue()).isEqualTo(302);
    assertThat(response.getHeaders().getLocation()).isNotNull();
    System.out.println("Presigned URL: " + response.getHeaders().getLocation());
  }

  static class EnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues.of(
          "discodeit.storage.type=s3",
          "discodeit.storage.s3.access-key=" + System.getenv("AWS_S3_ACCESS_KEY"),
          "discodeit.storage.s3.secret-key=" + System.getenv("AWS_S3_SECRET_KEY"),
          "discodeit.storage.s3.region=" + System.getenv("AWS_S3_REGION"),
          "discodeit.storage.s3.bucket=" + System.getenv("AWS_S3_BUCKET"),
          "discodeit.storage.s3.presigned-url-expiration=" +
              (System.getenv("AWS_S3_PRESIGNED_URL_EXPIRATION") != null
                  ? System.getenv("AWS_S3_PRESIGNED_URL_EXPIRATION")
                  : "600")).applyTo(context.getEnvironment());
    }
  }
}
