package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
	static User userEngine(UserService userService, BinaryContentService binaryContentService) {
		BinaryContentDTO binaryContentDTO = BinaryContentDTO.create("test1", null, null);
		BinaryContent content1 = binaryContentService.create(binaryContentDTO);
		UserCRUDDTO userDTO = UserCRUDDTO.create("test1", "test1", "123",content1);
		userService.register(userDTO);
	}

	static Server serverEngine(ServerService serverService) {
		serverService.create();
	}

	static Channel channelEngine(ChannelService channelService) {
		channelService.create();
	}

	static Message messageEngine(MessageService messageService) {
		messageService.create();
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		ServerService serverService = context.getBean(ServerService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);




	}

}
