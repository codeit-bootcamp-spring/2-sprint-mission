package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;


public class JavaApplication {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        UserRepository userRepository = UserRepository.getInstance();
        ChannelRepository channelRepository = ChannelRepository.getInstance();
        MessageRepository messageRepository = MessageRepository.getInstance();

        UserService userService = new JCFUserService(userRepository);
        ChannelService channelService = new JCFChannelService(channelRepository);
        MessageService messageService = new JCFMessageService(messageRepository);

        // entity 생성자 검증
        User testUser1 = new User("testUser1", "testUser1@gmail.com", "1111");
        User testUser2 = new User("testUser2", "testUser2@gmail.com", "2222");
        User testUser3 = new User("testUser3", "testUser3@gmail.com", "3333");
        User testUser4 = new User("testUser4", "testUser4@gmail.com", "4444");
        User testUser5 = new User("testUser5", "testUser5@gmail.com", "5555");

        Channel testChannel1 = new Channel("testChannel1", testUser1);
        Channel testChannel2 = new Channel("testChannel2", testUser2);
        Channel testChannel3 = new Channel("testChannel3", testUser3);
        Channel testChannel4 = new Channel("testChannel4", testUser4);
        Channel testChannel5 = new Channel("testChannel5", testUser5);

        Message testMessage1 = new Message(testUser1, "testMessage1", testChannel1);
        Message testMessage2 = new Message(testUser2, "testMessage2", testChannel2);
        Message testMessage3 = new Message(testUser3, "testMessage3", testChannel3);
        Message testMessage4 = new Message(testUser4, "testMessage4", testChannel4);
        Message testMessage5 = new Message(testUser5, "testMessage5", testChannel5);



        // UserService 검증
        // UserService 의 createUser 검증
        userService.createUser(testUser1);
        userService.createUser(testUser2);
        userService.createUser(testUser3);
        userService.createUser(testUser4);
        userService.createUser(testUser5);

        // UserService 의 readUser 검증
        System.out.println(userService.readUser(testUser3.getId()));

        // UserService 의 readAllUsers 검증
        System.out.println(userService.readAllUsers());

        // UserService 의 updateUserName 검증
        userService.updateUserName(testUser3.getId(), "testUser3333");
        System.out.println(userService.readAllUsers());

        // UserService 의 updatePassword 검증
        userService.updatePassword(testUser3.getId(), "33333333");
        System.out.println(userService.readAllUsers());

        // UserService 의 deleteUser 검증
        userService.deleteUser(testUser3.getId());
        System.out.println(userService.readAllUsers());



        // ChannelService 검증
        channelService.createChannel(testChannel1);
        channelService.createChannel(testChannel2);
        channelService.createChannel(testChannel3);
        channelService.createChannel(testChannel4);
        channelService.createChannel(testChannel5);

        System.out.println(channelService.readChannel(testChannel3.getId()));

        System.out.println(channelService.readAllChannels());

        channelService.updateChannelName(testChannel3.getId(), "updatedChannel3333");
        System.out.println(channelService.readAllChannels());

        channelService.addChannelParticipant(testChannel3.getId(), testUser5);
        channelService.addChannelParticipant(testChannel3.getId(), testUser2);
        System.out.println(channelService.readAllChannels());

        channelService.deleteChannel(testChannel3.getId());
        System.out.println(channelService.readAllChannels());



        // MessageService 검증
        messageService.createMessage(testMessage1);
        messageService.createMessage(testMessage2);
        messageService.createMessage(testMessage3);
        messageService.createMessage(testMessage4);
        messageService.createMessage(testMessage5);

        System.out.println(messageService.readMessage(testMessage3.getId()));

        System.out.println(messageService.readAllMessages());

        messageService.updateMessageContent(testMessage3.getId(), "Hello, this message is updated!!!");
        System.out.println(messageService.readAllMessages());

        messageService.deleteMessage(testMessage3.getId());
        System.out.println(messageService.readAllMessages());
    }
}
