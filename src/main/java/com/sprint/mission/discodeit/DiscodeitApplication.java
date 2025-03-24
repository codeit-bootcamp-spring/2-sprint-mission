package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.ArrayList;
import java.util.Optional;

public class DiscodeitApplication {

	// 사용자 생성 테스트
	static User setupUser(UserService userService) {
		UserDto userDto = new UserDto(null, "woody","woody@codeit.com","woody1234",null,  false );
		User user = userService.create(userDto, Optional.empty());
		return user;
	}

	// 채널 생성 테스트
	static Channel setupChannel(ChannelService channelService) {
		ChannelDto channelDto = new ChannelDto(null, ChannelType.PUBLIC, "공지", "공지 채널입니다.", new ArrayList<>());
		Channel channel = channelService.createPublicChannel(channelDto);
		return channel;
	}

	// 메시지 생성 테스트
	static void setupMessage(MessageService messageService, Channel channel, User author) {
		MessageDto messageDto = new MessageDto(null, "안녕하세요.", author.getId(), channel.getId(), new ArrayList<>());
		Message message = messageService.create(messageDto, new ArrayList<>());
		System.out.println("메시지 생성: " + message.getId() + ", 내용: " + message.getContent());
	}

	// 애플리케이션 실행
	public static void main(String[] args) {
		// 레포지토리 초기화
		UserRepository userRepository = new FileUserRepository();
		ChannelRepository channelRepository = new FileChannelRepository();
		MessageRepository messageRepository = new FileMessageRepository();
		UserStatusRepository userStatusRepository = new FileUserStatusRepository();
		ReadStatusRepository readStatusRepository = new FileReadStatusRepository();
		BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository();

		// 서비스 초기화
		UserService userService = new BasicUserService(userRepository, userStatusRepository, binaryContentRepository);
		ChannelService channelService = new BasicChannelService(channelRepository, readStatusRepository, messageRepository);
		MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository, binaryContentRepository);
		UserStatusService userStatusService = new BasicUserStatusService(userStatusRepository, userRepository);
		ReadStatusService readStatusService = new BasicReadStatusService(readStatusRepository, userRepository, channelRepository);
		BinaryContentService binaryContentService = new BasicBinaryContentService(binaryContentRepository);

		// 테스트 출력
		System.out.println("테스트 결과입니다.");

		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);

		System.out.println("사용자 생성: " + user.getId() + ", 사용자 이름: " + user.getUserName() + ", 이메일: " + user.getUserEmail());
		System.out.println("채널 생성: " + channel.getId() + ", 채널 이름: " + channel.getChannelName() + ", 채널 설명: " + channel.getDescription());

		setupMessage(messageService, channel, user);
	}
}
