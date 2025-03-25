//package com.sprint.mission.discodeit.service;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.repository.*;
//import com.sprint.mission.discodeit.service.advance.UserServiceImp;
//import com.sprint.mission.discodeit.service.basic.BasicChannelServiceImp;
//import com.sprint.mission.discodeit.service.basic.BasicMessageServiceImp;
//import com.sprint.mission.discodeit.service.basic.BasicUserServiceImp;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ServiceConfig {
//    @Bean
//    public ChannelService channelService(ChannelRepository channelRepository) {
//        return new BasicChannelServiceImp(channelRepository);
//    }
//
//    @Bean
//    public MessageService messageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
//        return new BasicMessageServiceImp(messageRepository, channelRepository, userRepository);
//    }
//
//    @Bean
//    public UserService userService(UserRepository userRepository, BinaryContentRepository binaryContentRepository, UserStatusRepository userStatusRepository) {
//        return new UserServiceImp(userRepository, binaryContentRepository, userStatusRepository);
//    }
//
//}
