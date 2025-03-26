package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDTO;
import com.sprint.mission.discodeit.dto.message.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.user.CreateUserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {

    static User setupUser(UserService userService) {
        CreateUserDTO dto = new CreateUserDTO("woody","woody@codeit.com", "woody1234");
        User user = userService.createUser(dto, Optional.empty());
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        CreatePublicChannelDTO dto = new CreatePublicChannelDTO("공지","공지 채널입니다.");
        Channel channel = channelService.createPublicChannel(dto);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, User author, Channel channel) {
        CreateMessageDTO dto = new CreateMessageDTO("안녕하세요.",author.getId(),channel.getId());
        Message message = messageService.createMessage(dto,new ArrayList<>());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, user, channel);

    }

}
