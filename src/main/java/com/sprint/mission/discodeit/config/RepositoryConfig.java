package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {

    private final RepositoryProperties properties;

    @Bean
    public UserRepository userRepository() {
        return isFile()
                ? new FileUserRepository(properties.getFileDir())
                : new JCFUserRepository();
    }

    @Bean
    public ChannelRepository channelRepository() {
        return isFile()
                ? new FileChannelRepository(properties.getFileDir())
                : new JCFChannelRepository();
    }

    @Bean
    public MessageRepository messageRepository() {
        return isFile()
                ? new FileMessageRepository(properties.getFileDir())
                : new JCFMessageRepository();
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        return isFile()
                ? new FileBinaryContentRepository(properties.getFileDir())
                : new JCFBinaryContentRepository();
    }

    @Bean
    public ReadStatusRepository readStatusRepository() {
        return isFile()
                ? new FileReadStatusRepository(properties.getFileDir())
                : new JCFReadStatusRepository();
    }

    @Bean
    public UserStatusRepository userStatusRepository() {
        return isFile()
                ? new FileUserStatusRepository(properties.getFileDir())
                : new JCFUserStatusRepository();
    }

    private boolean isFile() {
        return "file".equalsIgnoreCase(properties.getType());
    }
}
