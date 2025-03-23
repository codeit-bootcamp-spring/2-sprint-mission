package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.util.FileSerializationUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileRepositoryConfig {

    @Bean
    public UserRepository userRepository() {
        return new FileUserRepository(fileSerializationUtil());
    }

    @Bean
    public ChannelRepository channelRepository() {
        return new FileChannelRepository(fileSerializationUtil());
    }

    @Bean
    public MessageRepository messageRepository() {
        return new FileMessageRepository(fileSerializationUtil());
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() { return new FileBinaryContentRepository(fileSerializationUtil()); }

    @Bean
    public ReadStatusRepository readStatusRepository() {return new FileReadStatusRepository(fileSerializationUtil()); }

    @Bean
    public UserStatusRepository userStatusRepository() {return new FileUserStatusRepository(fileSerializationUtil()); }

    @Bean
    public FileSerializationUtil fileSerializationUtil() {
        return new FileSerializationUtil();
    }

}
