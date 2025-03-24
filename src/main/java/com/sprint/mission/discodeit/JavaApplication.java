package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {
//    static User setupUser(UserService userService) {
//        User user = userService.create("codeit", "codeit@codeit.com", "codeit");
//        System.out.println("유저 생성 : " + user.getId() + ", 이름 : " + user.getUserName() + ", 이메일 : " + user.getUserEmail());
//        return user;
//    }
//
//    static Channel setupChannel(ChannelService channelService) {
//        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
//        System.out.println("채널 생성 : " + channel.getId() + ", 채널 타입 : " + channel.getType() + ", 채널 이름: " + channel.getChannelName() + ", 채널 설명 : " + channel.getDescription());
//        return channel;
//    }
//
//    static void messageCreateTest(MessageService messageService, Channel channel, User user) {
//        Message message = messageService.create("안녕하세요.", channel.getId(), user.getId());
//        System.out.println("메시지 생성: " + message.getId() + ", 내용: " + message.getContent());
//    }

    public static void main(String[] args) {

//        UserRepository userRepository = new FileUserRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
//        MessageRepository messageRepository = new FileMessageRepository();
//
//        UserService userService = new BasicUserService(userRepository);
//        ChannelService channelService = new BasicChannelService(channelRepository);
//        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
//
//        System.out.println("테스트 결과입니다.");
//        User basicUser = setupUser(userService);
//        Channel basicChannel = setupChannel(channelService);
//        messageCreateTest(messageService, basicChannel, basicUser);

    }
}