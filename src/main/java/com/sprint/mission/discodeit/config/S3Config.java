package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.s3.S3StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@EnableConfigurationProperties(S3StorageProperties.class)
public class S3Config {

    private final S3StorageProperties properties;

    public S3Config(S3StorageProperties properties){
        this.properties = properties;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}

