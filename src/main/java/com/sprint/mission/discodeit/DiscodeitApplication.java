package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // User 테스트
        /*
        try {
            UserCreateRequest request1 = new UserCreateRequest("hye", "hye@gmail.com", "hyehyehye");
            userService.create(request1, Optional.empty());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*
        // username 중복 테스트
        try {
            UserCreateRequest request = new UserCreateRequest("hye", "lin@gmail.com", "hyehye");
            userService.create(request, Optional.empty());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // email 중복 테스트
        try {
            UserCreateRequest request = new UserCreateRequest("seon", "hye@gmail.com", "seon");
            userService.create(request, Optional.empty());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        // update & find, delete & findAll 테스트
        try {

            UserCreateRequest request = new UserCreateRequest("tree", "tree@gmail.com", "treee");
            User user = userService.create(request, Optional.empty());

            UserUpdateRequest userUpdateRequest = new UserUpdateRequest(user.getId(), Optional.of("newhye"),
                    Optional.of("newhye@g.com"), Optional.of("newpass"));
            userService.update(userUpdateRequest, Optional.empty());

            System.out.println("\n<update 결과 출력>");
            System.out.println(userService.find(user.getId()).toString());

            userService.delete(user.getId());

            System.out.println("\n<findAll 결과 출력>");
            List<UserInfoResponse> AlluserInfo = userService.findAll();
            for (UserInfoResponse userInfoResponse : AlluserInfo) {
                System.out.println(userInfoResponse.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        // Channel 테스트
        // Public/Private channel create & find 테스트
        try {
            PublicChannelRequest publicRequest = new PublicChannelRequest(ChannelType.PUBLIC, "공개채널1", "공개된 채널");
            Channel channel = channelService.create(publicRequest);
            System.out.println("\n" + channelService.find(channel.getId()));

            UserCreateRequest request = new UserCreateRequest("member1", "member1@gmail.com", "member11");
            User user = userService.create(request, Optional.empty());

            List<UUID> privateMember = new ArrayList<>();
            privateMember.add(user.getId());

            PrivateChannelRequest privateRequest = new PrivateChannelRequest(ChannelType.PRIVATE, privateMember);
            Channel channel2 = channelService.create(privateRequest);
            System.out.println("\n" + channelService.find(channel2.getId()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // update &  delete 테스트
        try {
            PublicChannelRequest publicRequest = new PublicChannelRequest(ChannelType.PUBLIC, "공개채널2", "공개된 채널2");
            Channel channel = channelService.create(publicRequest);
            System.out.println(\n"<update 전 출력>");
            System.out.println(channelService.find(channel.getId()));

            ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(channel.getId(), Optional.of("[변경]공개채널2"),
                    Optional.of("바꿨지롱"));
            Channel updateChannel = channelService.update(updateRequest);
            System.out.println(\n"<update 후 출력>");
            System.out.println(channelService.find(updateChannel.getId()));

            channelService.delete(updateChannel.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // findAllByUserId 테스트
        try {
            UserCreateRequest request = new UserCreateRequest("sun", "sun@gmail.com", "sun11");
            User user = userService.create(request, Optional.empty());
            UserCreateRequest request2 = new UserCreateRequest("kim", "kim@gmail.com", "kim11");
            User user2 = userService.create(request2, Optional.empty());

            List<UUID> privateMember = new ArrayList<>();
            privateMember.add(user.getId());
            privateMember.add(user2.getId());

            PrivateChannelRequest privateRequest = new PrivateChannelRequest(ChannelType.PRIVATE, privateMember);
            channelService.create(privateRequest);

            List<ChannelByIdResponse> result = channelService.findAllByUserId(user.getId());
            for (ChannelByIdResponse response : result) {
                System.out.println(response.toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        */
        // Message 테스트
        try {
            UserCreateRequest userRequest = new UserCreateRequest("metest", "metest@gmail.com", "metest11");
            User user = userService.create(userRequest, Optional.empty());

            PublicChannelRequest publicRequest = new PublicChannelRequest(ChannelType.PUBLIC, "공개채널3", "메시지 테스트용");
            Channel channel = channelService.create(publicRequest);

            List<BinaryContentCreateRequest> messageBinaryContent = new ArrayList<>();
            MessageCreateRequest messageRequest1 = new MessageCreateRequest("메시지 생성1", channel.getId(), user.getId());
            Message message1 = messageService.create(messageRequest1, messageBinaryContent);

            MessageCreateRequest messageRequest2 = new MessageCreateRequest("메시지 생성2", channel.getId(), user.getId());
            Message message2 = messageService.create(messageRequest2, messageBinaryContent);

            System.out.println("\n<findAllByChannelId 출력>");
            for (Message message : messageService.findAllByChannelId(channel.getId())) {
                System.out.println(message.toString());
            }

            MessageUpdateRequest updateRequest = new MessageUpdateRequest(message1.getId(), "메시지1 변경했지롱");
            messageService.update(updateRequest);

            System.out.println("\n<message update 후 findAllByChannelId 출력>");
            for (Message message : messageService.findAllByChannelId(channel.getId())) {
                System.out.println(message.toString());
            }

            System.out.println("\n<channel 삭제 후 삭제한 채널의 메시지 출력>");
            channelService.delete(channel.getId());
            for (Message message : messageService.findAllByChannelId(channel.getId())) {
                System.out.println(message.toString());
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
