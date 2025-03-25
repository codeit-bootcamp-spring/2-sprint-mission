package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.DTO.Channel.ChannelDetailsDto;
import com.sprint.mission.discodeit.DTO.Channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.DTO.Message.CreateMessageDto;
import com.sprint.mission.discodeit.DTO.Message.MessageDto;
import com.sprint.mission.discodeit.DTO.User.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.User.UserDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration .class})
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserService userService, ChannelService channelService, MessageService messageService) {
		return args -> {
			// 기존 데이터 삭제
			userService.findAll().forEach(user -> userService.delete(user.id()));
			channelService.findAllByUserId(null).forEach(channel -> channelService.delete(channel.id()));

			// 사용자 데이터 추가
			UserCreateRequest userRequest = new UserCreateRequest("testUser", "test@codeit.com", "password123");
			UserDto user = userService.create(userRequest, Optional.empty());
			System.out.println("테스트 사용자 생성: " + user.id());

			// 채널 데이터 추가
			CreatePublicChannelDto channelRequest = new CreatePublicChannelDto("공지사항", "테스트 채널입니다.");
			ChannelDetailsDto channel = channelService.createPublicChannel(channelRequest);
			System.out.println("테스트 채널 생성: " + channel.id());

			// 메시지 데이터 추가
			CreateMessageDto messageRequest = new CreateMessageDto("안녕하세요.", channel.id(), user.id(), List.of());
			MessageDto message = messageService.create(messageRequest);
			System.out.println("테스트 메시지 생성: " + message.id());
		};
	}

}
