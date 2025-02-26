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
        UserRepository userRepository = UserRepository.getInstance();
        ChannelRepository channelRepository = ChannelRepository.getInstance();
        MessageRepository messageRepository = MessageRepository.getInstance();

        UserService userService = new JCFUserService(userRepository);
        ChannelService channelService = new JCFChannelService(userRepository, channelRepository, messageRepository);
        MessageService messageService = new JCFMessageService(userRepository, channelRepository, messageRepository);

        // userService
        // createUser 검증
        System.out.println("UserSerivce의 createUser 메서드 검증");
        userService.createUser("user01", "user01@gmail.com", "1111");
        userService.createUser("user02", "user02@gmail.com", "2222");
        userService.createUser("user03", "user03@gmail.com", "3333");
        System.out.println(userService.readAllUsers());


        // read
        // createUser로 생성할 경우, id 를 가져올 방법이 없어 생성자로 생성 후, repository에 직접 추가
        System.out.println("UserService의 readUser 메서드 검증");
        User testUser01 = new User("testUser01", "testUser01@gmail.com", "1111");
        User testUser02 = new User("testUser02", "testUser02@gmail.com", "2222");
        User testUser03 = new User("testUser03", "testUser03@gmail.com", "3333");
        userRepository.addUser(testUser01);
        userRepository.addUser(testUser02);
        userRepository.addUser(testUser03);
        System.out.println(userService.readUser(testUser01.getId()));
        System.out.println(userService.readUser(testUser02.getId()));
        System.out.println(userService.readUser(testUser03.getId()));

        // readAll          //userRepository의 정렬순서 확인해볼 것
        System.out.println("UserService의 readAllUsers 검증");
        System.out.println(userService.readAllUsers());

        // update
        userService.updateUserName(testUser02.getId(), "updateUserName02");
        userService.updatePassword(testUser03.getId(), "updatePassword03");
        System.out.println(userService.readAllUsers());

        // delete
        userService.deleteUser(testUser01.getId());
        System.out.println(userService.readAllUsers());





        // ChannelService
        // create
        System.out.println("ChannelSerivce의 createChannel 메서드 검증");
        channelService.createChannel("channel01");
        channelService.createChannel("channel02");
        channelService.createChannel("channel03");

        // read
        System.out.println("ChannelService의 readChannel 메서드 검증");
        Channel testChannel01 = new Channel("testChannel01");
        Channel testChannel02 = new Channel("testChannel02");
        Channel testChannel03 = new Channel("testChannel03");
        channelRepository.addChannel(testChannel01);
        channelRepository.addChannel(testChannel02);
        channelRepository.addChannel(testChannel03);
        System.out.println(channelService.readChannel(testChannel01.getId()));
        System.out.println(channelService.readChannel(testChannel02.getId()));
        System.out.println(channelService.readChannel(testChannel03.getId()));

        // readAll
        System.out.println("ChannelService의 readAllChannels 메서드 검증");
        System.out.println(channelService.readAllChannels());

        // update
        System.out.println("ChannelService의 update 메서드 검증");
        channelService.updateChannelName(testChannel01.getId(), "updateChannelName01");
        channelService.addChannelParticipant(testChannel03.getId(), testUser03);
        System.out.println(channelService.readAllChannels());

        //delete
        System.out.println("ChannelService의 deleteChannel 메서드 검증");
        channelService.deleteChannel(testChannel01.getId());
        System.out.println(channelService.readAllChannels());







        // MessageService
        //create
        System.out.println("MessageSerivce의 createMessage 메서드 검증");
        // messageService.createMessage(testUser01, "test01", testChannel01);   // 위에서 testUser01 삭제함.
        messageService.createMessage(testUser02, "test02", testChannel02);
        messageService.createMessage(testUser03, "test03", testChannel03);

        // read
        System.out.println("MessageSerivce의 readMessage 메서드 검증");
        // Message testMessage01 = new Message(testUser01, "01test01", testChannel01);   // 위에서 testUser01 삭제함.
        Message testMessage02 = new Message(testUser02, "02test02", testChannel02);
        Message testMessage03 = new Message(testUser03, "03test03", testChannel03);
        messageRepository.addMessage(testMessage02);
        messageRepository.addMessage(testMessage03);
        // System.out.println(messageService.readMessage(testMessage01.getId()));
        System.out.println(messageService.readMessage(testMessage02.getId()));
        System.out.println(messageService.readMessage(testMessage03.getId()));

        // readAll
        System.out.println("MessageSerivce의 readAllMessage 메서드 검증");
        messageService.readAllMessages();

       // update
        System.out.println("MessageSerivce의 updateMessageContent 메서드 검증");
        messageService.updateMessageContent(testMessage02.getId(), "updateMessageContent02");
        System.out.println(messageService.readAllMessages());

       // delete
        System.out.println("MessageSerivce의 deleteMessage 메서드 검증");
        messageService.deleteMessage(testMessage03.getId());
        System.out.println(messageService.readAllMessages());




        // ChannelService 의 readMessageList (메세지가 있어야 해서 끝에 두었음)
        System.out.println("ChannelService의 readMessageListByChannelId 메서드 검증");
        System.out.println(channelService.readMessageListByChannelId(testChannel02.getId()));

    }
}
