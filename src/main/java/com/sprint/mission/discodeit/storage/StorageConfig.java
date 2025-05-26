package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import com.sprint.mission.discodeit.storage.s3.S3Properties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class StorageConfig {

  @Bean
  @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
  public BinaryContentStorage s3Storage(S3Client s3Client, S3Properties properties,
      S3Presigner s3Presigner) {
    return new S3BinaryContentStorage(s3Client, properties, s3Presigner);
  }
}
