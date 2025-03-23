package com.sprint.mission.discodeit.main;

import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.DTO.MessageDTO;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@RequiredArgsConstructor
public class DiscodeitApplication implements CommandLineRunner {
	private final UserService userService;
	private final ChannelService channelService;
	private final MessageService messageService;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
	}

	@Override
	public void run(String... args) {

		User user1 = userService.createUser(UserDTO.fromName("Kim"));
		User user2 = userService.createUser(UserDTO.fromName("Lee"));
		User user3 = userService.createUser(UserDTO.fromName("Park"));

		System.out.println("=== 유저 등록 완료 ===");
		System.out.println("User 1: " + user1.getId() + " | Name: " + user1.getUserName());
		System.out.println("User 2: " + user2.getId() + " | Name: " + user2.getUserName());
		System.out.println("User 3: " + user3.getId() + " | Name: " + user3.getUserName());

		User fetchedUser = userService.getUser(user1.getId());
		System.out.println("=== 유저 조회 ===");
		System.out.println("Fetched User: " + fetchedUser.getId() + " | Name: " + fetchedUser.getUserName());

		System.out.println("=== 모든 유저 조회 ===");
		for (User u : userService.getAllUsers()) {
			System.out.println("ID: " + u.getId() + " | Name: " + u.getUserName());
		}

		userService.updateUser(user3.getId(), "Ha");
		System.out.println("=== 유저 정보 수정 완료 ===");
		System.out.println("Updated User 3: " + userService.getUser(user3.getId()).getUserName());


		System.out.println("=== 유저 삭제 완료 ===");

		try {
			userService.deleteUser(user2.getId());
			System.out.println("유저 삭제 완료: " + user2.getId());
		} catch (IllegalArgumentException e) {
			System.err.println("에러 발생: " + e.getMessage());
		}

		System.out.println("=== 삭제 후 모든 유저 조회 ===");
		for (User u : userService.getAllUsers()) {
			System.out.println("ID: " + u.getId() + " | Name: " + u.getUserName());
		}

		System.out.println("-----");

		JCFChannelService channelService1 = JCFChannelService.getInstance();
		JCFChannelService channelService2 = JCFChannelService.getInstance();

		System.out.println(channelService1);
		System.out.println(channelService2);

		if (channelService1 == channelService2) {
			System.out.println("같은 인스턴스입니다! (싱글톤 확인 완료)");
		} else {
			System.out.println("다른 인스턴스입니다! (싱글톤 적용 실패)");
		}

		Channel channel1 = channelService.createChannel(ChannelDTO.fromName("Channel1"));
		Channel channel2 = channelService.createChannel(ChannelDTO.fromName("Channel2"));
		Channel channel3 = channelService.createChannel(ChannelDTO.fromName("Channel3"));

		System.out.println("=== 채널 등록 완료 ===");
		System.out.println("Channel 1: " + channel1.getId() + " | Name: " + channel1.getChannelName());
		System.out.println("Channel 2: " + channel2.getId() + " | Name: " + channel2.getChannelName());
		System.out.println("Channel 3: " + channel3.getId() + " | Name: " + channel3.getChannelName());

		Channel fetchedChannel = channelService.getChannel(channel1.getId());
		System.out.println("=== 채널 조회 ===");
		System.out.println("Fetched Channel: " + fetchedChannel.getId() + " | Name: " + fetchedChannel.getChannelName());

		System.out.println("=== 모든 채널 조회 ===");
		for (Channel c : channelService.getAllChannels()) {
			System.out.println("ID: " + c.getId() + " | Name: " + c.getChannelName());
		}

		channelService.updateChannel(channel1.getId(), "Announcements");
		System.out.println("=== 채널 정보 수정 완료 ===");
		System.out.println("Updated Channel 1: " + channelService.getChannel(channel1.getId()).getChannelName());

		System.out.println("=== 채널 삭제 완료 ===");

		try {
			channelService.deleteChannel(channel2.getId());
			System.out.println("채널 삭제 완료: " + channel2.getId());
		} catch (IllegalArgumentException e) {
			System.err.println("에러 발생: " + e.getMessage());
		}

		System.out.println("=== 삭제 후 모든 채널 조회 ===");
		for (Channel c : channelService.getAllChannels()) {
			System.out.println("ID: " + c.getId() + " | Name: " + c.getChannelName());
		}

		System.out.println("-----");

		Message message1 = messageService.createMessage(new MessageDTO(user1.getId(), channel1.getId(), "Hello World"));
		Message message2 = messageService.createMessage(new MessageDTO(user3.getId(), channel1.getId(), "Hi Kim!"));
		Message message3 = messageService.createMessage(new MessageDTO(user1.getId(), channel3.getId(), "Java"));


		System.out.println("=== 메세지 목록 ===");
		messageService.getAllMessages().forEach(msg ->
				System.out.println("[" + msg.getChannelId() + "] " + msg.getContent()));

		messageService.updateMessage(message1.getId(), "Hello CodeIt! (edited)");
		System.out.println("메세지 변경: " + messageService.getMessage(message1.getId()).getContent());

		System.out.println("=== 메세지 삭제 ===");
		try {
			messageService.deleteMessage(message2.getId());
			System.out.println("메세지 삭제 완료: " + message2.getId());
		} catch (IllegalArgumentException e) {
			System.err.println("에러 발생: " + e.getMessage());
		}

		System.out.println("=== 메세지 목록 (삭제 후) ===");
		messageService.getAllMessages().stream()
				.map(msg -> "[" + msg.getChannelId() + "] " + msg.getUserId() + ": " + msg.getContent())
				.forEach(System.out::println);
	}
}