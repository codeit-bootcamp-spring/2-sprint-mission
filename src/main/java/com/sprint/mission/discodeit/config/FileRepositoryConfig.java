package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.util.FileSerializationUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
    public FileSerializationUtil fileSerializationUtil() {
        return new FileSerializationUtil();
    }

}
