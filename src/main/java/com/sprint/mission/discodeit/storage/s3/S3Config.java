package com.sprint.mission.discodeit.storage.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

  @Value("${AWS_S3_ACCESS_KEY}")
  private String accessKey;

  @Value("${AWS_S3_SECRET_KEY}")
  private String secretKey;

  @Value("${AWS_S3_REGION}")
  private String region;

  @Bean
  public S3Client s3Client() {
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
        .build();
  }

  @Bean
  public S3Presigner s3Presigner() {
    AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
        .build();
  }
}
