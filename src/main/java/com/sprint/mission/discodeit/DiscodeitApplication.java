package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelReadResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserReadResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
    /*static User setupUser(UserService userService) {
        User user = userService.createUser("testUser1", "test1@codeit.com", "1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.createChannel("testChannel1");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.createMessage(author.getId(), "testMessage1", channel.getId());
        System.out.println("메시지 생성: " + message.getId());
    }*/

    static UUID CreateUser1(UserService userService) {
        return userService.createUser(
                new UserCreateRequest("user1",
                                        "user1@email.com",
                                        "password1",
                                        new BinaryContentCreateRequest("filePath1",
                                                                        "imageFile1",
                                                                        "png",
                                                                        1111)));
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        AuthService authService = context.getBean(AuthService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        UserService userService = context.getBean(UserService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);


        /*User user1 = setupUser(userService);
        Channel channel1 = setupChannel(channelService);
        channelService.addChannelParticipant(channel1.getId(), user1.getId());
        messageCreateTest(messageService, channel1, user1);*/

        // binaryContentService 테스트
        UUID binaryContent1Id = binaryContentService.create(new BinaryContentCreateRequest("filePath1",
                                                                    "file2",
                                                                    "png",
                                                                    2222));
        System.out.println(binaryContentService.existsById(binaryContent1Id));
        BinaryContentFindResponse binaryContentFindResponse1 = binaryContentService.findById(binaryContent1Id);
        binaryContentService.findAllByIdIn(List.of(binaryContent1Id));
        // binaryContentService.deleteByID(binaryContent1Id);

        // UserService 테스트
        UUID user1ID = CreateUser1(userService);
        UserReadResponse userReadResponse1 = userService.readUser(user1ID);
        List<UserReadResponse> userReadResponseList = userService.readAllUsers();
        // update() 메서드 생략 (binaryContent create을 통해 UUID를 받아와야 update 가능)
        //userService.deleteUser(user1ID);

        // ChannelService 테스트
        UUID privateChannel1Id = channelService.createPrivateChannel(new PrivateChannelCreateRequest(List.of(new UserReadResponse(user1ID, "user1", "user1@email.com", null, true))));
        UUID publicChannel1Id = channelService.createPublicChannel("publicChannel1");
        ChannelReadResponse privateChannelReadResponse1 = channelService.readChannel(privateChannel1Id);
        List<ChannelReadResponse> channelReadResponseList = channelService.findAllByUserId(user1ID);
        List<Message> readMessageList = channelService.readMessageListByChannelId(publicChannel1Id);
        channelService.updateChannel(new ChannelUpdateRequest(publicChannel1Id, "updateNewPublicChannel1"));
        channelService.addChannelParticipant(publicChannel1Id, user1ID);
        //channelService.deleteChannel(privateChannel1Id);
        //channelService.deleteChannel(publicChannel1Id);

        // MessageService 테스트
        UUID message1Id = messageService.createMessage(new MessageCreateRequest(user1ID, "message1", privateChannel1Id, new ArrayList<BinaryContentCreateRequest>()));
        UUID message2Id = messageService.createMessage(new MessageCreateRequest(user1ID, "message2", publicChannel1Id, List.of(new BinaryContentCreateRequest("filePath3", "imageFile3", "png", 3333))));
        messageService.readMessage(message1Id);
        messageService.findAllByChannelId(privateChannel1Id);
        messageService.updateMessage(new MessageUpdateRequest(message2Id, "updatemessage2", List.of(binaryContent1Id)));
        // messageService.deleteBinaryContentInMessage(message2Id, binary); // 컨트롤러에서 binaryContent 생성을 따로 할지 결정하고 난뒤에 검증하는게 좋을듯
        messageService.deleteMessage(message1Id);

        //


        System.out.println("finish");
    }
}
