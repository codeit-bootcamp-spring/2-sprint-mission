package com.sprint.mission.discodeit.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class StorageProperties {

  private String type = "local";
  private Local local = new Local();
  private S3 s3 = new S3();

  @Data
  public static class Local {

    private String rootPath;
  }

  @Data
  public static class S3 {

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private Long presignedUrlExpiration;
  }
}
