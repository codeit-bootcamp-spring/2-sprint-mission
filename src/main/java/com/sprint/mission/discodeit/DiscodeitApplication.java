package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
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

import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		// Spring에서 자체 실행되는 문장
		ConfigurableApplicationContext content = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = content.getBean(UserService.class);
		ChannelService channelService = content.getBean(ChannelService.class);
		MessageService messageService = content.getBean(MessageService.class);

		System.out.println("===== TEST START =====");

		try {
			// 1) 유저 생성 테스트
			CreateUserDTO userDTO = new CreateUserDTO("alice", "alice@example.com", "password123", null, null);
			UserResponseDTO createdUser = userService.create(userDTO);
			System.out.println("Created user: " + createdUser);

			// 2) 채널 생성 테스트 (PUBLIC 채널 예시)
			CreatePublicChannelDTO channelDTO = new CreatePublicChannelDTO("General", "General discussion channel");
			ChannelResponseDTO createdChannel = channelService.createPublic(channelDTO);
			System.out.println("Created channel: " + createdChannel);

			// 3) 메시지 생성 테스트
			CreateMessageDTO messageDTO = new CreateMessageDTO(
					"Hello, Channel!",
					createdUser.getId(),
					createdChannel.getChannelId(),
					null // 첨부파일 없다고 가정
			);
			Message createdMessage = messageService.create(messageDTO);
			System.out.println("Created message: " + createdMessage);

			// 4) 유저, 채널, 메시지 조회
			//    (메소드 시그니처는 실제 코드에 맞게 조정)
			List<UserResponseDTO> allUsers = userService.findAll();
			System.out.println("All users: " + allUsers);

			List<ChannelResponseDTO> allChannels = channelService.findAll();
			System.out.println("All channels: " + allChannels);

			List<Message> channelMessages = messageService.findAllByChannelId(createdChannel.getChannelId());
			System.out.println("Messages in channel: " + channelMessages);

			 // 5) 업데이트 테스트 (원하면 추가)
			 UpdateChannelDTO updateChDto = new UpdateChannelDTO(createdChannel.getChannelId(), "NewName", "NewDescription");
			 ChannelResponseDTO updatedChannel = channelService.update(updateChDto);
			 System.out.println("Updated channel: " + updatedChannel);

			 // 6) 삭제 테스트 (원하면 추가)
			 messageService.delete(createdMessage.getId());
			 System.out.println("Deleted message with id: " + createdMessage.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("===== TEST END =====");
	}
}
