package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFRepositoryConfig {
    @Bean
    public UserRepository userRepository() {
        return new JCFUserRepository();
    }

    @Bean
    public ChannelRepository channelRepository() {
        return new JCFChannelRepository();
    }

    @Bean
    public MessageRepository messageRepository() {
        return new JCFMessageRepository();
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        return new JCFBinaryContentRepository();
    }

    @Bean
    public ReadStatusRepository readStatusRepository() {
        return new JCFReadStatusRepository();
    }

    @Bean
    public UserStatusRepository userStatusRepository() {
        return new JCFUserStatusRepository();
    }

}
