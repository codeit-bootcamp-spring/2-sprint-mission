package com.sprint.mission.discodeit.storage.s3;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@Component
public class S3Properties {

  @Value("${AWS_S3_ACCESS_KEY}")
  private String accessKeyId;

  @Value("${AWS_S3_SECRET_KEY}")
  private String secretKey;

  @Value("${AWS_S3_REGION}")
  private String region;

  @Value("${AWS_S3_BUCKET}")
  private String bucket;
}