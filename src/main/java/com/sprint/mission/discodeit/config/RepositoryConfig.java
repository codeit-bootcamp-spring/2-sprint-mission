package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.file.FileRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public FileRepository<BinaryContent> binaryContentFileRepository() {
        return new FileRepositoryImpl<>();
    }

    @Bean
    public FileRepository<Channel> channelFileRepository() {
        return new FileRepositoryImpl<>();
    }

    @Bean
    public FileRepository<Message> messageFileRepository() {
        return new FileRepositoryImpl<>();
    }

    @Bean
    public FileRepository<ReadStatus> readStatusFileRepository() {
        return new FileRepositoryImpl<>();
    }

    @Bean
    public FileRepository<User> userFileRepository() {
        return new FileRepositoryImpl<>();
    }

    @Bean
    public FileRepository<UserStatus> userStatusFileRepository() {
        return new FileRepositoryImpl<>();
    }
}

