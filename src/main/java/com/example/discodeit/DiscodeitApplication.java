package com.example.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.userDto.UserRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication

public class DiscodeitApplication {
    static User setupUser(UserService userService) {
        UserRequest req = new UserRequest("woody", "woody@codeit.com", "woody1234", "ex".getBytes());
        return userService.create(req);
    }

    static Channel setupChannel(ChannelService channelService) {
        PublicChannelCreateRequest publicChannelCreateRequest =
                new PublicChannelCreateRequest("공지", "공지 채널입니다.");
        Channel channel = channelService.createPublicChannel(publicChannelCreateRequest);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        List<String> strs = new ArrayList<>();
        MessageCreateRequest messageCreateRequest =
                new MessageCreateRequest(channel.getId(), author.getId(), "안녕하세요.", strs);
        Message message = messageService.create(messageCreateRequest);
        System.out.println("메시지 생성: " + message.getId());
    }

	public static void main(String[] args) {

		SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService;
		ChannelService channelService;
		MessageService messageService;
	}

}
