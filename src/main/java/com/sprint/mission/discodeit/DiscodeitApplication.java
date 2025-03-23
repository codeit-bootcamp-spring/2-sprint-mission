package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.config.FileRepositoryConfig;
import com.sprint.mission.discodeit.config.JCFRepositoryConfig;
import com.sprint.mission.discodeit.config.SecurityConfig;
import com.sprint.mission.discodeit.config.ServiceConfig;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.groups.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@SpringBootApplication(scanBasePackages = "com.sprint.mission.discodeit")
public class DiscodeitApplication {
	static UserService userService;
	static ChannelService channelService;
	static MessageService messageService;

	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				FileRepositoryConfig.class,
				JCFRepositoryConfig.class,
				SecurityConfig.class,
				ServiceConfig.class
		);

		userService = applicationContext.getBean(UserService.class);
		channelService = applicationContext.getBean(ChannelService.class);
		messageService = applicationContext.getBean(MessageService.class);

		try {
			System.out.println("====== 테스트 1: 기본 생성 ======");
            UUID user1Uuid = UUID.randomUUID();
            UUID channel1Uuid = UUID.randomUUID();
            UUID channel11Uuid = UUID.randomUUID();
            UUID message1Uuid = UUID.randomUUID();
            testCreate(user1Uuid, channel1Uuid, channel11Uuid, message1Uuid);

			System.out.println("\n====== 테스트 2: 생성 후 삭제 ======");
			UUID user2Uuid = UUID.randomUUID();
			UUID channel2Uuid = UUID.randomUUID();
			UUID channel22Uuid = UUID.randomUUID();
			UUID message2Uuid = UUID.randomUUID();
			testCreate(user2Uuid, channel2Uuid, channel22Uuid, message2Uuid);
			delete(user2Uuid, channel2Uuid, message2Uuid);
//
			System.out.println("\n====== 테스트 3: 생성 후 조회 ======");
			UUID user3Uuid = UUID.randomUUID();
			UUID channel3Uuid = UUID.randomUUID();
			UUID channel33Uuid = UUID.randomUUID();
			UUID message3Uuid = UUID.randomUUID();
			testCreate(user3Uuid, channel3Uuid, channel33Uuid, message3Uuid);
			findById(user3Uuid, channel3Uuid, message3Uuid);
//
			System.out.println("\n====== 테스트 4: 생성 후 업데이트 ======");
			UUID user4Uuid = UUID.randomUUID();
			UUID channel4Uuid = UUID.randomUUID();
			UUID channel44Uuid = UUID.randomUUID();
			UUID message4Uuid = UUID.randomUUID();
			testCreate(user4Uuid, channel4Uuid, channel44Uuid, message4Uuid);
			update(user4Uuid, channel44Uuid, message4Uuid);
//
			System.out.println("\n====== 테스트 5: 전체 항목 조회 ======");
			findAll(user4Uuid, channel4Uuid);

			System.out.println("\n모든 테스트가 성공적으로 완료되었습니다!");
		} catch (Exception e) {
			System.err.println("테스트 실패: " + e.getMessage());
			e.printStackTrace();
		}

	}

    public static void testCreate(UUID userUuid, UUID channelUuid1, UUID channelUuid2, UUID messageUuid) {
        String name = "yang" +userUuid.toString();
		String userEmail = name + "@gmail.com";
		userService.create(new UserCreateDto(
				userUuid, name, "password", userEmail, null, null
		));
        channelService.create(new ChannelPublicCreateDto(
				channelUuid1, "PRIVATE채널", "PRIVATE채널설명", userUuid, ChannelType.PRIVATE
		));
		channelService.create(new ChannelPublicCreateDto(
				channelUuid2, "PUBLIC채널", "PUBLIC채널설명", userUuid, ChannelType.PUBLIC
		));
        messageService.create(
				new MessageCreateDto(messageUuid, "메시지", channelUuid1, userUuid, null, null));
    }

	public static void findById(UUID userUuid, UUID channelUuid, UUID messageUuid) {
		System.out.println(userService.findById(userUuid));
		System.out.println(channelService.findById(channelUuid));
		System.out.println(messageService.findById(messageUuid));
	}
//
	public static void update(UUID userUuid, UUID channelUuid, UUID messageUuid) throws IOException {
		userService.update(new UserUpdateDto(userUuid, "진호", null, null, null, null));
		channelService.update(new ChannelUpdateDto(channelUuid, "진호채널", "채널설명1", Instant.now()));
		messageService.update(new MessageUpdateDto(messageUuid, "바뀐내용"));
	}
//
	public static void delete(UUID userUuid, UUID channelUuid, UUID messageUuid) {
		userService.delete(userUuid);
		channelService.delete(channelUuid);
	}
//
	public static void findAll(UUID UserId, UUID ChannelId) {
		System.out.println("===User===");
		System.out.println(userService.findAll());
		System.out.println("===Channel===");
		System.out.println(channelService.findAllByUserId(UserId));
		System.out.println("===Message===");
		System.out.println(messageService.findByChannelId(ChannelId));
	}
}
