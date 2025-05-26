package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.DiscodeitApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = DiscodeitApplication.class)
public class AWSS3Test {

  @Autowired
  private S3Client s3Client;

  @Autowired
  private S3Properties properties;

  @Test
  void uploadTest() {
    String key = "test/" + UUID.randomUUID() + ".txt";
    String content = "Hello from test";

    PutObjectResponse response = s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(properties.getBucket())
            .key(key)
            .build(),
        RequestBody.fromString(content)
    );

    assertThat(response.sdkHttpResponse().isSuccessful()).isTrue();
  }

  @Test
  void downloadTest() {
    String key = "test-file.txt";
    GetObjectResponse response = s3Client.getObject(
        GetObjectRequest.builder()
            .bucket(properties.getBucket())
            .key(key)
            .build(),
        ResponseTransformer.toFile(java.nio.file.Paths.get("downloaded.txt"))
    );
    assertThat(response.sdkHttpResponse().isSuccessful()).isTrue();
  }

  @Test
  void generatePresignedUrlTest() {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key("test-file.txt")
        .build();

    String url = s3Client.utilities()
        .getUrl(b -> b.bucket(properties.getBucket()).key("test-file.txt")).toString();

    System.out.println("Presigned URL: " + url);
    assertThat(url).contains("http");
  }
}
