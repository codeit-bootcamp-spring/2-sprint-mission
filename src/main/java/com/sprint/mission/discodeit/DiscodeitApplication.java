package com.sprint.mission.discodeit;

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
		// Spring에서 자체 실행되는 문장
		ConfigurableApplicationContext content = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = content.getBean(UserService.class);
		ChannelService channelService = content.getBean(ChannelService.class);
		MessageService messageService = content.getBean(MessageService.class);

		// DiscodeitApplication에 옮겨둔 static 메소드 호출
		User user = DiscodeitApplication.setupUser(userService);
		Channel channel = DiscodeitApplication.setupChannel(channelService);
		DiscodeitApplication.messageCreateTest(messageService, channel, user);

	}

	// test
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
}
