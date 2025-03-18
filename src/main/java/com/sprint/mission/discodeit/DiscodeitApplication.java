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
		Message message2 = messageService.create("헬로.", channel.getId(), author.getId());
		Message message3 = messageService.create("하이.", channel.getId(), author.getId());
		System.out.println("테스트 메시지 생성");
		System.out.println("메세지 id: " + message.getId());
		System.out.println("메세지 내용: " + message.getContent());
		System.out.println("메세지 내용: " + message2.getContent());
		System.out.println("메세지 내용: " + message3.getContent());
	}

	public static void main(String[] args) {
		// context 변수에 컨테이너 주소값이 있고, 그 안에 콩들이 들어있음. 스프링이 @bean들을 실행할때 컨테이너에 자동으로 넣어줌.
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// Spring Context에서 Bean 조회하여(명시적으로 등록한 콩들 꺼내오기, getBean()) 할당
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		// 셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);

		// 테스트
		messageCreateTest(messageService, channel, user);
	}
}
