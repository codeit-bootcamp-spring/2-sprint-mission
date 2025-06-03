package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.local.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import java.nio.file.Path;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({S3StorageProperties.class, LocalStorageProperties.class})
public class StorageConfig {

    /* ---------- 로컬 ---------- */
    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local", matchIfMissing = true)
    public BinaryContentStorage localStorage(LocalStorageProperties props) {
        return new LocalBinaryContentStorage(Path.of(props.getRootPath()));
    }

    /* ---------- S3 ---------- */
    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
    public BinaryContentStorage s3Storage(S3StorageProperties props) {
        return new S3BinaryContentStorage(props);
    }
}