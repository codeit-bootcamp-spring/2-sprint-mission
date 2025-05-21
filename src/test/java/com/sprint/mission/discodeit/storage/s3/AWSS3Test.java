package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.AWSS3Properties;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@SpringBootTest
public class AWSS3Test {

  @Autowired
  private AWSS3Properties properties;

  // S3 실제 요청 수행하는 client
  private S3Client s3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(properties.getAccessKey(),
        properties.getSecretKey());

    return S3Client.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  // Presigned URL 생성
  private S3Presigner presigner() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(properties.getAccessKey(),
        properties.getSecretKey());

    return S3Presigner.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  @Test
  void uploadFile() {
    S3Client s3Client = s3Client();

    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(properties.getBucket())
        .key("test/upload.txt")
        .contentType("text/plain")
        .build();

    File file = new File("src/test/resources/sample.txt");

    s3Client.putObject(request, RequestBody.fromFile(file));
    System.out.println("업로드 완료");
  }

  @Test
  void downloadFile() {
    S3Client s3Client = s3Client();

    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key("test/upload.txt")
        .build();

    File file = new File("src/test/resources/download.txt");

    s3Client.getObject(request, file.toPath());
    System.out.println("다운로드 완료");
  }

  @Test
  void presignedUrlTest() {
    S3Presigner presigner = presigner();

    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key("test/upload.txt")
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .getObjectRequest(request)
        .build();

    URL url = presigner.presignGetObject(presignRequest).url();

    System.out.println("Presigned URL: " + url);

  }

}
