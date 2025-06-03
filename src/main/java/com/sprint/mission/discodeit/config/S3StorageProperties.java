package com.sprint.mission.discodeit.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties("discodeit.storage.s3")
public class S3StorageProperties {

    @NotBlank
    private String accessKey;
    @NotBlank
    private String secretKey;
    @NotBlank
    private String region;
    @NotBlank
    private String bucket;

    // presigned URL 만료(초)
    private long presignedUrlExpiration = 600;
}
