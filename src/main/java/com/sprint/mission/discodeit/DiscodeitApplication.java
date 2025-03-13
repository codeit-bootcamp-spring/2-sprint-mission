package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
    static void userCRUDTest(UserService userService) {
        // 생성
        try {
            User user = userService.create("sang", "sang@codeit.com", "sang1234");
            System.out.println("유저 생성: " + user.getId());
        } catch (RuntimeException e) {
            System.out.println("유저 생성 실패: " + e.getMessage());
        }

        // 동일한 유저 email로 생성
        try {
            User user = userService.create("park", "sang@codeit.com", "park1234");
            System.out.println("유저 생성: " + user.getId());
        } catch (RuntimeException e) {
            System.out.println("유저 생성 실패: " + e.getMessage());
        }

        // 조회
        List<User> foundUsers = userService.findAll();
        System.out.println("유저 조회(다건): " + foundUsers.size());

        try {
            User user = userService.create("findTest", "findTest@codeit.com", "test1234");
            User foundUser = userService.findById(user.getId());
            System.out.println("유저 조회(단건): " + foundUser.getId());
        } catch (RuntimeException e) {
            System.out.println("유저 조회(단건) 실패: " + e.getMessage());
        }

        // 존재하지 않는 ID 조회
        try {
            User foundUser = userService.findById(UUID.randomUUID());
            System.out.println("유저 조회(단건): " + foundUser.getId());
        } catch (RuntimeException e) {
            System.out.println("유저 조회(단건) 실패: " + e.getMessage());
        }

        // 수정
        try {
            User user = userService.create("updateTest", "updateTest@codeit.com", "updateTest1234");
            User updatedUser = userService.update(user.getId(), null, null, "woody5678");
            System.out.println("유저 수정: " + String.join("/", updatedUser.getUsername(), updatedUser.getEmail(),
                    updatedUser.getPassword()));
        } catch (RuntimeException e) {
            System.out.println("유저 수정 실패: " + e.getMessage());
        }

        // 존재하지 않는 ID로 수정
        try {
            User updatedUser = userService.update(UUID.randomUUID(), null, null, "woody5678");
            System.out.println("유저 수정: " + String.join("/", updatedUser.getUsername(), updatedUser.getEmail(),
                    updatedUser.getPassword()));
        } catch (RuntimeException e) {
            System.out.println("유저 수정 실패: " + e.getMessage());
        }

        // 삭제
        User deletedUser = userService.create("deletedTest", "deletedTest@codeit.com", "deletedTest1234");
        List<User> foundUsersBeforeDelete = userService.findAll();
        System.out.println("유저 삭제 전: " + foundUsersBeforeDelete.size());
        userService.delete(deletedUser.getId());
        List<User> foundUsersAfterDelete = userService.findAll();
        System.out.println("유저 삭제 후: " + foundUsersAfterDelete.size());
    }

    static void channelCRUDTest(ChannelService channelService) {
        // 생성
        try {
            Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
            System.out.println("채널 생성: " + channel.getId());
        } catch (RuntimeException e) {
            System.out.println("채널 생성 실패: " + e.getMessage());
        }

        // 동일한 채널 이름으로 생성
        try {
            Channel channel = channelService.create(ChannelType.PRIVATE, "공지", "공지 채널입니다요.");
            System.out.println("채널 생성: " + channel.getId());
        } catch (RuntimeException e) {
            System.out.println("채널 생성 실패: " + e.getMessage());
        }

        // 조회
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("채널 조회(다건): " + foundChannels.size());

        try {
            Channel channel = channelService.create(ChannelType.PUBLIC, "조회 테스트", "조회 테스트 채널입니다.");
            Channel foundChannel = channelService.findById(channel.getId());
            System.out.println("채널 조회(단건): " + foundChannel.getId());
        } catch (RuntimeException e) {
            System.out.println("채널 조회(단건) 실패: " + e.getMessage());
        }

        // 존재하지 않는 ID 조회
        try {
            Channel foundChannel = channelService.findById(UUID.randomUUID());
            System.out.println("채널 조회(단건): " + foundChannel.getId());
        } catch (RuntimeException e) {
            System.out.println("채널 조회(단건) 실패: " + e.getMessage());
        }

        // 수정
        try {
            Channel channel = channelService.create(ChannelType.PUBLIC, "수정 테스트", "수정 테스트 채널입니다.");
            Channel updatedChannel = channelService.update(channel.getId(), "수정 성공 테스트", "수정 성공 테스트 채널입니다.");
            System.out.println("채널 수정: " + String.join("/", updatedChannel.getName(), updatedChannel.getDescription()));
        } catch (RuntimeException e) {
            System.out.println("채널 수정 실패: " + e.getMessage());
        }

        // 존재하지 않는 ID로 수정
        try {
            Channel updatedChannel = channelService.update(UUID.randomUUID(), "수정 실패 테스트", "수정 실패 테스트 채널입니다.");
            System.out.println("채널 수정: " + String.join("/", updatedChannel.getName(), updatedChannel.getDescription()));
        } catch (RuntimeException e) {
            System.out.println("채널 수정 실패: " + e.getMessage());
        }

        // 삭제
        Channel deletedChannel = channelService.create(ChannelType.PUBLIC, "삭제 테스트", "삭제 테스트 채널입니다.");
        List<Channel> foundChannelsBeforeDelete = channelService.findAll();
        System.out.println("채널 삭제 전: " + foundChannelsBeforeDelete.size());
        channelService.delete(deletedChannel.getId());
        List<Channel> foundChannelsAfterDelete = channelService.findAll();
        System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
    }

    static void messageCRUDTest(MessageService messageService, UserService userService, ChannelService channelService) {
        User user = userService.create("메세지 기본 유저 1", "메세지 기본 유저 1@codeit.com", "메세지 기본 유저 1234");
        Channel channel = channelService.create(ChannelType.PUBLIC, "메세지 테스트", "메세지 테스트 채널입니다.");
        // 생성
        try {
            Message message = messageService.create(user.getId(), channel.getId(), "안녕하세요.");
            System.out.println("메시지 생성: " + message.getId());
        } catch (RuntimeException e) {
            System.out.println("메시지 생성 실패: " + e.getMessage());
        }

        // 존재하지 않는 유저로 생성
        try {
            Message message = messageService.create(UUID.randomUUID(), channel.getId(), "안녕하세요.");
            System.out.println("메시지 생성: " + message.getId());
        } catch (RuntimeException e) {
            System.out.println("메시지 생성 실패: " + e.getMessage());
        }

        // 존재하지 않는 채널로 생성
        try {
            Message message = messageService.create(user.getId(), UUID.randomUUID(), "안녕하세요.");
            System.out.println("메시지 생성: " + message.getId());
        } catch (RuntimeException e) {
            System.out.println("메시지 생성 실패: " + e.getMessage());
        }

        // 조회
        List<Message> foundMessages = messageService.findAll();
        System.out.println("메시지 조회(다건): " + foundMessages.size());

        try {
            Message message = messageService.create(user.getId(), channel.getId(), "메시지 조회 테스트");
            Message foundMessage = messageService.findById(message.getId());
            System.out.println("메시지 조회(단건): " + foundMessage.getId());
        } catch (RuntimeException e) {
            System.out.println("메시지 조회(단건) 실패: " + e.getMessage());
        }

        // 존재하지 않는 ID 조회
        try {
            Message foundMessage = messageService.findById(UUID.randomUUID());
            System.out.println("메시지 조회(단건): " + foundMessage.getId());
        } catch (RuntimeException e) {
            System.out.println("메시지 조회(단건) 실패: " + e.getMessage());
        }

        // 채널 ID로 조회
        List<Message> foundMessagesByChannelId = messageService.findByChannelId(channel.getId());
        System.out.println("메시지 채널 ID로 조회: " + foundMessagesByChannelId.size());

        // 유저 ID로 조회
        List<Message> foundMessagesByAuthorId = messageService.findByAuthorId(user.getId());
        System.out.println("메시지 유저 ID로 조회: " + foundMessagesByAuthorId.size());

        // 수정
        try {
            Message message = messageService.create(user.getId(), channel.getId(), "메시지 수정 테스트");
            Message updatedMessage = messageService.update(message.getId(), "메시지 수정 성공 테스트입니다.");
            System.out.println("메시지 수정: " + updatedMessage.getContent());
        } catch (RuntimeException e) {
            System.out.println("메시지 수정 실패: " + e.getMessage());
        }

        // 존재하지 않는 ID로 수정
        try {
            Message updatedMessage = messageService.update(UUID.randomUUID(), "메시지 수정 실패 테스트입니다.");
            System.out.println("메시지 수정: " + updatedMessage.getContent());
        } catch (RuntimeException e) {
            System.out.println("메시지 수정 실패: " + e.getMessage());
        }

        // 삭제
        Message deletedMessage = messageService.create(user.getId(), channel.getId(), "메시지 삭제 테스트");
        List<Message> foundMessagesBeforeDelete = messageService.findAll();
        System.out.println("메시지 삭제 전: " + foundMessagesBeforeDelete.size());
        messageService.delete(deletedMessage.getId());
        List<Message> foundMessagesAfterDelete = messageService.findAll();
        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 초기화
        UserService userService = context.getBean(BasicUserService.class);
        ChannelService channelService = context.getBean(BasicChannelService.class);
        MessageService messageService = context.getBean(BasicMessageService.class);

        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService);
        messageCRUDTest(messageService, userService, channelService);
    }

}
