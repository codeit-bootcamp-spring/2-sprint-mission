package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.DTO.Channel.ChannelDetailsDto;
import com.sprint.mission.discodeit.DTO.Channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.DTO.Message.CreateMessageDto;
import com.sprint.mission.discodeit.DTO.Message.MessageDto;
import com.sprint.mission.discodeit.DTO.Message.UpdateMessageDto;
import com.sprint.mission.discodeit.DTO.User.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.User.UserDto;
import com.sprint.mission.discodeit.DTO.User.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		// 테스트 실행
		userCRUDTest(userService);
		channelCRUDTest(channelService);
		messageCRUDTest(messageService, channelService, userService);

		// 셋업 및 테스트 실행 후 데이터 삭제
		UserDto user = setupUser(userService);
		ChannelDetailsDto channel = setupChannel(channelService);
		messageCreateTest(messageService, channel, user);

		userService.delete(user.id());
		channelService.delete(channel.id());
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

	static void userCRUDTest(UserService userService) {
		// 생성
		UserCreateRequest createRequest = new UserCreateRequest("uniqueUser", "unique@codeit.com", "password123");
		UserDto userDto = userService.create(createRequest, Optional.empty());
		System.out.println("유저 생성: " + userDto.id());

		// 조회
		UserDto foundUser = userService.find(userDto.id());
		System.out.println("유저 조회(단건): " + foundUser.id());
		List<UserDto> foundUsers = userService.findAll();
		System.out.println("유저 조회(다건): " + foundUsers.size());

		// 수정
		UserUpdateRequest updateRequest = new UserUpdateRequest("updatedUser", "updated@codeit.com", null);
		User updatedUserDto = userService.update(userDto.id(), updateRequest, Optional.empty());
		System.out.println("유저 수정: " + String.join("/", updatedUserDto.getUsername(), updatedUserDto.getEmail()));

		// 삭제
		userService.delete(userDto.id());
		List<UserDto> foundUsersAfterDelete = userService.findAll();
		System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
	}

	static void channelCRUDTest(ChannelService channelService) {
		// 생성
		ChannelDetailsDto createdChannel = channelService.createPublicChannel(new CreatePublicChannelDto("공지", "공지 채널입니다."));
		System.out.println("채널 생성: " + createdChannel.id());

		// 조회
		ChannelDetailsDto foundChannel = channelService.find(createdChannel.id());
		System.out.println("채널 조회(단건): " + foundChannel.id());
		List<ChannelDetailsDto> foundChannels = channelService.findAllByUserId(null);
		System.out.println("채널 조회(다건): " + foundChannels.size());

		// 수정
		channelService.update(createdChannel.id(), "공지사항", null);
		ChannelDetailsDto updatedChannel = channelService.find(createdChannel.id());
		System.out.println("채널 수정: " + String.join("/", updatedChannel.name(), updatedChannel.description()));

		// 삭제
		channelService.delete(createdChannel.id());
		List<ChannelDetailsDto> foundChannelsAfterDelete = channelService.findAllByUserId(null);
		System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
	}

	static void messageCRUDTest(MessageService messageService, ChannelService channelService, UserService userService) {
		try {
			// 생성
			ChannelDetailsDto createdChannel = channelService.createPublicChannel(new CreatePublicChannelDto("테스트용 채널", "테스트용 채널입니다"));
			UserCreateRequest createRequest = new UserCreateRequest("user1", "user1@codeit.com", "user1234");
			UserDto userDto = userService.create(createRequest, Optional.empty());
			CreateMessageDto createdMessage = new CreateMessageDto("안녕하세요.", createdChannel.id(), userDto.id(), List.of());
			MessageDto savedMessage = messageService.create(createdMessage);
			System.out.println("메시지 생성: " + savedMessage.id());

			// 조회
			try {
				MessageDto foundMessage = messageService.find(savedMessage.id());
				System.out.println("메시지 조회(단건): " + foundMessage.id());
			} catch (NoSuchElementException e) {
				System.out.println("메시지 조회 실패: " + e.getMessage());
			}

			// 다건 조회
			List<MessageDto> foundMessages = messageService.findAllByChannelId(createdChannel.id());
			System.out.println("메시지 조회(다건): " + foundMessages.size());

			// 수정
			try {
				UpdateMessageDto updateRequest = new UpdateMessageDto(savedMessage.id(), "반갑습니다.");
				MessageDto updatedMessage = messageService.update(updateRequest);
				System.out.println("메시지 수정: " + updatedMessage.content());
			} catch (NoSuchElementException e) {
				System.out.println("메시지 수정 실패: " + e.getMessage());
			}

			// 삭제
			try {
				messageService.delete(savedMessage.id()); // 올바른 메시지 ID 사용
				System.out.println("메시지 삭제 성공");
			} catch (NoSuchElementException e) {
				System.out.println("메시지 삭제 실패: " + e.getMessage());
			}

			List<MessageDto> foundMessagesAfterDelete = messageService.findAllByChannelId(createdChannel.id());
			System.out.println("메시지 삭제 후 조회: " + foundMessagesAfterDelete.size());

		} catch (NoSuchElementException e) {
			System.out.println("메시지 CRUD 테스트 중 오류 발생: " + e.getMessage());
		}
	}

	static UserDto setupUser(UserService userService) {
		// 기존 데이터 삭제
		userService.findAll().forEach(user -> userService.delete(user.id()));

		// 고유한 사용자 생성
		UserCreateRequest userCreateRequest = new UserCreateRequest("uniqueUser", "unique@codeit.com", "password123");
		return userService.create(userCreateRequest, Optional.empty());
	}

	static ChannelDetailsDto setupChannel(ChannelService channelService) {
		return channelService.createPublicChannel(new CreatePublicChannelDto("공지", "공지 채널입니다."));
	}

	static void messageCreateTest(MessageService messageService, ChannelDetailsDto channel, UserDto author) {
		CreateMessageDto createMessageDto = new CreateMessageDto("안녕하세요.", channel.id(), author.id(), List.of());
		MessageDto message = messageService.create(createMessageDto);
		System.out.println("메시지 생성: " + message.id());
	}
}
