package com.sprint.mission.discodeit.storage.s3;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class AWSS3Test {

  private final S3Client s3Client;

  @Value("${discodeit.storage.s3.access-key}")
  private String accessKey;

  @Value("${discodeit.storage.s3.secret-key}")
  private String secretKey;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucketName;

  @Value("${discodeit.storage.s3.region}")
  private String region;

  @Value("${discodeit.storage.s3.base-url}")
  private String baseUrl;

  // 업로드
  public String upload(MultipartFile image) {
    String fileName = image.getOriginalFilename();
    String contentType = image.getContentType();
    long size = image.getSize();

    String s3Key = UUID.randomUUID() + "-" + fileName;

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .contentType(contentType)
        .build();

    try {
      s3Client.putObject(putObjectRequest,
          RequestBody.fromInputStream(image.getInputStream(), size));
    } catch (IOException e) {
      log.error("Error uploading file to S3", e);
      throw new RuntimeException("Error uploading file to S3", e);
    }

    String s3Url = baseUrl + "/" + s3Key;

    log.info("File uploaded successfully: {}", s3Url);

    return s3Url;
  }

  //다운로드
  public byte[] download(String key) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest)) {
      return response.readAllBytes();
    } catch (IOException e) {
      log.error("Error downloading file from S3", e);
      throw new RuntimeException("Error downloading file from S3", e);
    }
  }

  //PresignedUrl 생성
  public String generatePresignedUrl(String key, Duration duration) {
    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(duration)
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
        presignRequest);

    log.info("PresignedUrl generated successfully: {}", presignedGetObjectRequest.url().toString());

    return presignedGetObjectRequest.url().toString();
  }

}
