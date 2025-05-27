package com.sprint.mission.discodeit.storage.s3;

import io.github.cdimascio.dotenv.Dotenv;
import java.time.Duration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.file.Paths;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AWSS3Test {

  private static Dotenv dotenv;
  private static S3Client s3;
  private static String bucket;
  private static String key = "test/sample.txt";

  @BeforeAll
  static void setup() {
    dotenv = Dotenv.configure()
        .ignoreIfMissing()  // 없으면 넘어가도록
        .load();

    // 시스템 속성으로도 등록 (CI에서 .env 없이도 동작하도록)
    dotenv.entries().forEach(entry -> {
      if (System.getProperty(entry.getKey()) == null) {
        System.setProperty(entry.getKey(), entry.getValue());
      }
    });

    s3 = S3Client.builder()
        .region(Region.of(dotenv.get("AWS_S3_REGION")))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    dotenv.get("AWS_S3_ACCESS_KEY"),
                    dotenv.get("AWS_S3_SECRET_KEY")
                )
            )
        )
        .build();

    bucket = dotenv.get("AWS_S3_BUCKET");
  }

  @Test
  @Order(1)
  void uploadFileToS3() {
    File file = new File("sample.txt");
    assertTrue(file.exists(), "로컬 파일 sample.txt가 존재해야 합니다.");

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    s3.putObject(putRequest, RequestBody.fromFile(file));
    System.out.println("업로드 성공");

    HeadObjectResponse head = s3.headObject(b -> b.bucket(bucket).key(key));
    assertNotNull(head);
  }

  @Test
  @Order(2)
  void downloadFileFromS3() {
    // 기존에 파일 있으면 삭제
    File downloadedFile = new File("downloaded_sample.txt");
    if (downloadedFile.exists()) {
      boolean deleted = downloadedFile.delete();
      System.out.println("기존 파일 삭제됨: " + deleted);
    }

    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    s3.getObject(getRequest, Paths.get("downloaded_sample.txt"));
    System.out.println("다운로드 성공");

    assertTrue(new File("downloaded_sample.txt").exists());
  }

  @Test
  @Order(3)
  void deleteFileFromS3() {
    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    s3.deleteObject(deleteRequest);
    System.out.println("삭제 성공");

    assertThrows(NoSuchKeyException.class, () -> {
      s3.headObject(b -> b.bucket(bucket).key(key));
    });
  }

  @Test
  @Order(4)
  void generatePresignedUrl() {
    try (S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(dotenv.get("AWS_S3_REGION")))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    dotenv.get("AWS_S3_ACCESS_KEY"),
                    dotenv.get("AWS_S3_SECRET_KEY")
                )
            )
        )
        .build()) {

      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(5)) // 5분간 유지
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

      String presignedUrl = presignedRequest.url().toString();
      System.out.println("Presigned URL 생성 성공: " + presignedUrl);

      assertNotNull(presignedUrl);
      assertTrue(presignedUrl.contains(bucket));
      assertTrue(presignedUrl.contains(key));
    }
  }

}
