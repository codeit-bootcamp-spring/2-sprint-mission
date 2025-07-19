package com.sprint.mission.discodeit.common.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3Config {

  private final String region;

  private final StaticCredentialsProvider credentialsProvider;

  public S3Config(
      @Value("${discodeit.storage.s3.access-key}")
      String accessKey,
      @Value("${discodeit.storage.s3.secret-key}")
      String secretKey,
      @Value("${discodeit.storage.s3.region}")
      String region
  ) {
    this.region = region;
    this.credentialsProvider = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey));
  }

  @Bean
  public S3Client createS3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(credentialsProvider)
        .build();
  }

  @Bean
  public S3Presigner createS3Presigner() {
    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(credentialsProvider)
        .build();
  }

}
