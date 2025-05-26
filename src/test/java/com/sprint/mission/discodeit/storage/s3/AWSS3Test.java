package com.sprint.mission.discodeit.storage.s3;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

class AWSS3Test {

  private S3Client s3Client;
  private String bucket;
  private S3Presigner s3Presigner;

  @BeforeEach
  void setUp() throws IOException {
    Properties props = new Properties();
    props.load(Files.newBufferedReader(Paths.get(".env")));

    String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
    String region = props.getProperty("AWS_S3_REGION");
    bucket = props.getProperty("AWS_S3_BUCKET");

    s3Client = S3Client.builder()
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
            accessKey,
            secretKey
        )))
        .region(Region.of(region))
        .build();

    s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
            accessKey,
            secretKey
        )))
        .build();
  }

  @Test
  void uploadTest() throws IOException {
    UUID binaryContentId = UUID.randomUUID();
    String fileName = "000701.png";
    String contentType = "image/jpeg";

    Path filePath = Paths.get("src/test/resources/000701.png");
    byte[] bytes = Files.readAllBytes(filePath);

    String s3Key = binaryContentId.toString() + "-" + fileName;

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(s3Key)
        .contentType(contentType)
        .cacheControl("no-cache")
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

    System.out.println("업로드 성공: " + s3Key);
  }

  @Test
  void downloadTest() throws IOException {
    String key = "0763fa22-fd2a-460a-b373-51a810554b4c-test-image.jpg"; // 또는 uploadTest()에서 출력된 key

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

    Path localPath = Paths.get("src/test/resources/downloaded-" + key);
    Files.copy(s3Object, localPath);

    System.out.println("다운로드 성공: " + localPath.toAbsolutePath());
  }

  @Test
  void presignedUrlTest() {
    String key = "f735d70b-312a-49a1-bbac-6e4a9802e3b8-000701.png";
    String contentType = "image/jpeg";

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofMinutes(5))
        .build();

    PresignedGetObjectRequest presignedUrl = s3Presigner.presignGetObject(presignRequest);

    System.out.println("URL: " + presignedUrl.url());
  }

}

