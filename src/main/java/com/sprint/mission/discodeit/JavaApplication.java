package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.ProfileImageRequest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaApplication {
    static User setupUser(UserService userService, boolean withProfileImage) {
        UserCreateRequest userCreateRequest = new UserCreateRequest("woody", "woody@codeit.com", "woody1234");

        ProfileImageRequest profileImageRequest = null;
        if (withProfileImage) {
            profileImageRequest = new ProfileImageRequest(new byte[]{1,2,3,4});
        }
        return userService.create(userCreateRequest, profileImageRequest);
    }


    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

//    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//
//        UserService userService = context.getBean(UserService.class);
//
//        //프로필 이미지 제외
//        User user1 = setupUser(userService, false);
//        System.out.println("User1 ID: " + user1.getId());
//
//        //프로필 이미지 포함
//        User user2 = setupUser(userService, true);
//        System.out.println("User2 ID: " + user2.getId());
//    }
}