package com.sprint.mission.discodeit.storage.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

public class AWSS3Test {

  private static S3Client s3Client;
  private static S3Presigner presigner;
  private static String bucketName;
  private static final String Test_Key = "test-object.txt";

  @BeforeAll
  static void setup() throws IOException {
    Properties props = new Properties();
    props.load(new FileInputStream(System.getProperty("user.dir") + "/.env"));

    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        props.getProperty("AWS_S3_ACCESS_KEY"),
        props.getProperty("AWS_S3_SECRET_KEY")
    );

    s3Client = S3Client.builder()
        .region(Region.of(props.getProperty("AWS_S3_REGION")))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    presigner = S3Presigner.builder()
        .region(Region.of(props.getProperty("AWS_S3_REGION")))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    bucketName = props.getProperty("AWS_S3_BUCKET");
  }

  @Test
  void uploadFileTest() throws IOException {
    // 1. 테스트 파일 생성
    File tempFile = File.createTempFile("s3test", ".txt");
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write("AWS S3 SDK 테스트 파일");
    }

    // 2. 업로드 실행
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(Test_Key)
        .build();

    s3Client.putObject(request, tempFile.toPath());

    // 3. 업로드 검증
    HeadObjectResponse head = s3Client.headObject(
        HeadObjectRequest.builder()
            .bucket(bucketName)
            .key(Test_Key)
            .build()
    );
    System.out.println("업로드 성공: " + head);
  }

  @Test
  void downloadFileTest() {
    // 1. 다운로드 요청 생성
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(Test_Key)
        .build();

    // 2. 파일 다운로드 및 내용 출력
    try (InputStream is = s3Client.getObject(request)) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      System.out.println("파일 내용: " + reader.readLine());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void generatePresignedUrlTest() {
    // 1. Presigned URL 생성 요청
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(Test_Key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    // 2. URL 생성 및 출력
    URL presignedUrl = presigner.presignGetObject(presignRequest).url();
    System.out.println("Presigned URL: " + presignedUrl);
  }
}
