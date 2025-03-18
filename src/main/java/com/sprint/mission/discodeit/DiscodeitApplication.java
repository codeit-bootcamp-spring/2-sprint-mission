package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.DTO.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {
	static User userEngine(UserService userService) {
		byte[] test = {0, 0, 1};
		BinaryContentDTO binaryContentDTO = BinaryContentDTO.create("test1", "test", test);
		Optional<BinaryContentDTO> optional = Optional.of(binaryContentDTO);

		UserCRUDDTO userDTO = UserCRUDDTO.create("test1", "test1", "123");

		User user = userService.register(userDTO, optional);
		return user;
	}

	static Server serverEngine(ServerService serverService, User user) {
		ServerCRUDDTO create = ServerCRUDDTO.create(user.getId(), "테스트서버");
		Server server = serverService.create(create);
		return server;
	}

	static Channel channelEngine(ChannelService channelService, Server server,User user) {
		ChannelCRUDDTO create = ChannelCRUDDTO.create(server.getServerId(), user.getId(), "테스트 채널");
		Channel channel = channelService.create(create);
		return channel;
	}

	static Message messageEngine(MessageService messageService, Channel channel, User user) {
		MessageCRUDDTO messageCRUDDTO = MessageCRUDDTO.create(user.getId(), channel.getChannelId(), "Test");
		Message message = messageService.create(messageCRUDDTO);
		return message;
	}


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		ServerService serverService = context.getBean(ServerService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		User testUser = userEngine(userService);
		Server testServer = serverEngine(serverService, testUser);
		Channel testChannel = channelEngine(channelService, testServer, testUser);
		Message message = messageEngine(messageService, testChannel, testUser);
		System.out.println(message);
	}

}
