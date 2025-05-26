package com.sprint.mission.discodeit.storage.s3;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discodeit.storage.s3")
@Getter
@Setter
@NoArgsConstructor

public class S3Properties {

  @Value("${discodeit.storage.s3.access-key}")
  private String accessKey;
  @Value("${discodeit.storage.s3.secret-key}")
  private String secretKey;
  @Value("${discodeit.storage.s3.region}")
  private String region;
  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;
  private long presignedUrlExpiration = 600;
}
