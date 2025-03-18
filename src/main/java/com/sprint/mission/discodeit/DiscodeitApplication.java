package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ProfileImageRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// 서비스 초기화
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		User user1 = setupUser(userService, false);
		System.out.println("User1 ID: " + user1.getId());

		User user2 = setupUser(userService, true);
		System.out.println("User2 ID: " + user2.getId());

		Channel channel = setupChannel(channelService);
		messageCreateTest(messageService, channel, user1);
	}


	static User setupUser(UserService userService, boolean withProfileImage) {
		UserCreateRequest userCreateRequest = new UserCreateRequest("woody", "woody@codeit.com","woody1234");

		ProfileImageRequest profileImageRequest = null;
		if (withProfileImage){
			profileImageRequest = new ProfileImageRequest(new byte[] {1,2,3,4});
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


	}

