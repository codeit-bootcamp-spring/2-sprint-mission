package com.sprint.mission.discodeit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class AWSS3Config {

  private final AWSS3Properties properties;

  @Bean
  public S3Client S3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(properties.getAccessKey(),
        properties.getSecretKey());

    return S3Client.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  @Bean(destroyMethod = "close")
  public S3Presigner S3Presigner() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(properties.getAccessKey(),
        properties.getSecretKey());

    return S3Presigner.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }
}
