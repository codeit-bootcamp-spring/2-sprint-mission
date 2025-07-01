package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.local.LocalBinaryContentStorage;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class S3Config {

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "S3")
    public S3Client s3Client(
        @Value("${discodeit.storage.s3.access-key}") String accessKey,
        @Value("${discodeit.storage.s3.secret-key}") String secretKey,
        @Value("${discodeit.storage.s3.region}") String region
    ){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider staticCredentialsProvider =
            StaticCredentialsProvider.create(credentials);

        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(staticCredentialsProvider)
            .build();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
    public S3Presigner s3Presigner(
        @Value("${discodeit.storage.s3.access-key}") String accessKey,
        @Value("${discodeit.storage.s3.secret-key}") String secretKey,
        @Value("${discodeit.storage.s3.region}") String region
    ){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider credentialsProvider =
            StaticCredentialsProvider.create(credentials);

        return S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(credentialsProvider)
            .build();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
    public BinaryContentStorage s3Storage(
        S3Client s3Client,
        S3Presigner s3Presigner,
        @Value("${discodeit.storage.s3.bucket}") String bucket,
        @Value("${discodeit.storage.s3.presigned-url-expiration:600}") int expirationSeconds
    ) {
        log.info("✅ Using S3BinaryContentStorage");
        return new S3BinaryContentStorage(s3Client, s3Presigner, bucket, expirationSeconds);
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local", matchIfMissing = true)
    public BinaryContentStorage localStorage(
        @Value("${discodeit.storage.local.root-path}") String rootPath) {
        log.info("✅ Using LocalBinaryContentStorage");
        return new LocalBinaryContentStorage(Paths.get(rootPath));
    }
}
