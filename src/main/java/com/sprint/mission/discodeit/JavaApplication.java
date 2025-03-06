package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JavaApplication {
    static void userCRUDTest(UserService userService) {
        User user = null;
        // 생성
        try {
            user = userService.create("woody", "woody@codeit.com", "woody1234");
            System.out.println("유저 생성: " + user.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("유저 생성 예외 발생 : " + e.getMessage());
        }
        // 조회
        try {
            User foundUser = userService.find(user.getId());
            System.out.println("유저 조회(단건): " + foundUser.getId());
            List<User> foundUsers = userService.findAll();
            System.out.println("유저 조회(다건): " + foundUsers.size());
        } catch (NoSuchElementException e) {
            System.out.println("유저 조회 예외 발생 : " + e.getMessage());
        }
        // 수정
        try {
            User updatedUser = userService.update(user.getId(), null, null, "woody5678");
            System.out.println("유저 수정: " + String.join("/", updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getPassword()));
        } catch (NoSuchElementException e) {
            System.out.println("유저 수정 예외 발생 : " + e.getMessage());
        }
        // 삭제
        try {
            userService.delete(user.getId());
            List<User> foundUsersAfterDelete = userService.findAll();
            System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
        } catch (NoSuchElementException e) {
            System.out.println("유저 삭제 예외 발생 : " + e.getMessage());
        }
    }

    static void channelCRUDTest(ChannelService channelService) {
        Channel channel = null;
        try {
            // 생성
            channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
            System.out.println("채널 생성: " + channel.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("채널 생성 예외 발생 : " + e.getMessage());
        }
        // 조회
        try {
            Channel foundChannel = channelService.find(channel.getId());
            System.out.println("채널 조회(단건): " + foundChannel.getId());
            List<Channel> foundChannels = channelService.findAll();
            System.out.println("채널 조회(다건): " + foundChannels.size());
        } catch (NoSuchElementException e) {
            System.out.println("채널 조회 예외 발생 : " + e.getMessage());
        }
        try {
            // 수정
            Channel updatedChannel = channelService.update(channel.getId(), "공지사항", null);
            System.out.println("채널 수정: " + String.join("/", updatedChannel.getName(), updatedChannel.getDescription()));
        } catch (NoSuchElementException e) {
            System.out.println("채널 수정 예외 발생 : " + e.getMessage());
        }
        // 삭제
        try {
            channelService.delete(channel.getId());
            List<Channel> foundChannelsAfterDelete = channelService.findAll();
            System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
        } catch (NoSuchElementException e) {
            System.out.println("채널 삭제 예외 발생 : " + e.getMessage());
        }

    }

    static void messageCRUDTest(UserService userService, ChannelService channelService, MessageService messageService) {
        // 생성
        User user = null;
        Channel channel = null;
        Message message = null;
        try {
            user = userService.create("woody", "woody@codeit.com", "woody1234");
            channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
            message = messageService.create("안녕하세요.", channel.getId(), user.getId());
            System.out.println("메시지 생성: " + message.getId());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("메시지 생성 예외 발생 : " + e.getMessage());
        }

        // 조회
        try {
            Message foundMessage = messageService.find(message.getId());
            System.out.println("메시지 조회(단건): " + foundMessage.getId());
            List<Message> foundMessages = messageService.findAll();
            System.out.println("메시지 조회(다건): " + foundMessages.size());
        } catch (NoSuchElementException e) {
            System.out.println("메시지 조회 예외 발생 : " + e.getMessage());
        }
        // 수정
        try {
            Message updatedMessage = messageService.update(message.getId(), "반갑습니다.");
            System.out.println("메시지 수정: " + updatedMessage.getContent());
        } catch (NoSuchElementException e) {
            System.out.println("메시지 수정 예외 발생 : " + e.getMessage());
        }
        // 삭제
        try {

            messageService.delete(message.getId());
            List<Message> foundMessagesAfterDelete = messageService.findAll();
            System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
        } catch (NoSuchElementException e) {
            System.out.println("메시지 삭제 예외 발생 : " + e.getMessage());
        }

    }

    static User setupUser(UserService userService) {
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // Repository 인스턴스 생성
        UserRepository userRepository = FileUserRepository.getInstance();
        ChannelRepository channelRepository = FileChannelRepository.getInstance();
        MessageRepository messageRepository = FileMessageRepository.getInstance();

        // Service 인스턴스 생성
        UserService userService = BasicUserService.getInstance(userRepository);
        ChannelService channelService = BasicChannelService.getInstance(channelRepository);
        MessageService messageService = BasicMessageService.getInstance(messageRepository, channelRepository, userRepository);

        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService);
        messageCRUDTest(userService, channelService, messageService);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
