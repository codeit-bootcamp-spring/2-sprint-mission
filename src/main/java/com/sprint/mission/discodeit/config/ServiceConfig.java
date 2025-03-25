package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ServiceConfig {
    @Bean
    public BinaryContentService binaryContentService(BinaryContentRepository binaryContentRepository) {
        return new BasicBinaryContentService(binaryContentRepository);
    }

    @Bean
    public ChannelService channelService(ChannelRepository channelRepository, MessageRepository messageRepository, ReadStatusRepository readStatusRepository) {
        return new BasicChannelService(channelRepository, messageRepository, readStatusRepository);
    }

    @Bean
    public MessageService messageService(UserRepository userRepository, ChannelService channelService, MessageRepository messageRepository) {
        return new BasicMessageService(userRepository, channelService, messageRepository);
    }

    @Bean
    public ReadStatusService readStatusService(ReadStatusRepository readStatusRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        return new BasicReadStatusService(readStatusRepository, userRepository, channelRepository);
    }

    @Bean
    public BasicUserService basicUserService(UserRepository userRepository,UserStatusRepository userStatusRepository, BinaryContentRepository binaryContentRepository, BCryptPasswordEncoder passwordEncoder) {
        return new BasicUserService(userRepository, userStatusRepository, binaryContentRepository, passwordEncoder);
    }

    @Bean
    public BasicUserStatusService basicUserStatusService(UserStatusRepository userStatusRepository) {
        return new BasicUserStatusService(userStatusRepository);
    }
}
