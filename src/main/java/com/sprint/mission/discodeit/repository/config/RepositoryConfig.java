package com.sprint.mission.discodeit.repository.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Configuration
public class RepositoryConfig {

    // BinaryContentRepository 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public BinaryContentRepository fileBinaryContentRepository(
            @Value("${discodeit.repository.file-directory}") String fileDirectory)throws IOException {
        return new FileBinaryContentRepository(fileDirectory);
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public BinaryContentRepository jcfBinaryContentRepository() {
        return new JCFBinaryContentRepository();
    }

    // ChannelRepository 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public ChannelRepository fileChannelRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        return new FileChannelRepository(fileDirectory);
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ChannelRepository jcfChannelRepository() {
        return new JCFChannelRepository();
    }

    // MessageRepository 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public MessageRepository fileMessageRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        return new FileMessageRepository(fileDirectory);
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public MessageRepository jcfMessageRepository() {
        return new JCFMessageRepository();
    }

    // ReadStatusRepository 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public ReadStatusRepository fileReadStatusRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        return new FileReadStatusRepository(fileDirectory);
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ReadStatusRepository jcfReadStatusRepository() {
        return new JCFReadStatusRepository();
    }

    // UserRepository 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserRepository fileUserRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        return new FileUserRepository(fileDirectory);
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserRepository jcfUserRepository() {
        return new JCFUserRepository();
    }

    // UserStatusRepository 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserStatusRepository fileUserStatusRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        return new FileUserStatusRepository(fileDirectory);
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserStatusRepository jcfUserStatusRepository() {
        return new JCFUserStatusRepository();
    }
}
