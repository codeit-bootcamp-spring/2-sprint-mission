package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;


public class JavaApplication {
    /*public static void main(String[] args) {
        // Initialize services
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(userService,channelService);

        //df
        // Create Users
        User user1 = new User("Alice");
        User user2 = new User("Bob");
        userService.createUser(user1);
        userService.createUser(user2);

        // Create Channels
        Channel channel1 = new Channel("General");
        channelService.createChannel(channel1);

        Message message1 = new Message("Hello, world!", user1.getId(), channel1.getId());
        // Create Messages
        messageService.createMessage(message1);

        // Read Users
        System.out.println("All Users: " + userService.getAllUsers());
        System.out.println("User1: " + userService.getUser(user1.getId()).getUsername());

        // Update User
        userService.updateUser(user1.getId(), "AliceUpdated");
        System.out.println("Updated User1: " + userService.getUser(user1.getId()).getUsername());

        // Delete User with Validation
        if (messageService.getAllMessages().stream().noneMatch(m -> m.getauthorId().equals(user2.getId()))) {
            userService.deleteUser(user2.getId());
        }
        System.out.println("All Users After Deletion: " + userService.getAllUsers());

        // Read Channels
        System.out.println("All Channels: " + channelService.getAllChannels());

        // Update Channel
        channelService.updateChannel(channel1.getId(), "UpdatedGeneral");
        System.out.println("Updated Channel: " + channelService.getChannel(channel1.getId()).getName());

        // Read Messages
        System.out.println("All Messages: " + messageService.getAllMessages());

        // Update Message with Validation

        messageService.updateMessage(message1.getId(), "Updated Message");

        System.out.println("Updated Message: " + messageService.getMessage(message1.getId()).getContent());

        // Delete Message
        messageService.deleteMessage(message1.getId());
        System.out.println("All Messages After Deletion: " + messageService.getAllMessages());
    }*/
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
        // 서비스 초기화
        UserRepository U =  new FileUserRepository();
        ChannelRepository C =  new FileChannelRepository();
        MessageRepository M =  new FileMessageRepository();

        // TODO Basic*Service 구현체를 초기화하세요.
        UserService userService = new BasicUserService(U);
        ChannelService channelService = new BasicChannelService(C);
        MessageService messageService = new BasicMessageService(M);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}