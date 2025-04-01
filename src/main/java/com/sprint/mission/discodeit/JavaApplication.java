//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
//import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.service.basic.ChannelService;
//import com.sprint.mission.discodeit.service.basic.MessageService;
//import com.sprint.mission.discodeit.service.basic.UserService;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//
//public class JavaApplication {
//    static User setupUser(UserService userService) {
//        UserCreateRequest userCreateRequest = new UserCreateRequest("woody", "woody@codeit.com", "woody1234", null);
//        return userService.create(userCreateRequest);
//    }
//
////    static Channel setupChannel(ChannelService channelService) {
////        ChannelCreateDTO channelCreateDTO = new ChannelCreateDTO(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
////        Channel channel = channelService.create(channelCreateDTO);
////        return channel;
////    }
//
//    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
//        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
//        System.out.println("메시지 생성: " + message.getId());
//    }
//
//    public static void main(String[] args) {
//        // 레포지토리 초기화
//        UserRepository userRepository = new FileUserRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
//        MessageRepository messageRepository = new FileMessageRepository();
//
//        // 서비스 초기화
//        UserService userService = new BasicUserService(userRepository);
//        ChannelService channelService = new BasicChannelService(channelRepository);
//        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
//
//        // 셋업
//        User user = setupUser(userService);
////        Channel channel = setupChannel(channelService);
//        // 테스트
////        messageCreateTest(messageService, channel, user);
//    }
//}
