package com.sprint.mission.discodeit.storage.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "discodeit.storage.s3")
public record S3StorageProperties(
        String bucket,
        String baseUrl,
        @DefaultValue("ap-northeast-2") String region,
        @DefaultValue("PT10M") Duration presignedUrlExpiry
) {}
