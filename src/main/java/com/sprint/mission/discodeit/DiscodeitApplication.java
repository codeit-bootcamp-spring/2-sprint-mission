package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
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
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		byte[] profileImage = new byte[]{1, 2, 3, 4, 5};

		UserCreateRequest userRequest1 = new UserCreateRequest("홍길동", "홍길동@example.com", "password123", profileImage);
		UserCreateRequest userRequest2 = new UserCreateRequest("이순신", "이순신@example.com", "password456", null);
		UserResponse hong = userService.create(userRequest1);
		UserResponse lee = userService.create(userRequest2);

		PublicChannelCreateRequest publicChannelRequest = new PublicChannelCreateRequest("일반", "일반 채팅방");
		ChannelResponse publicChannel = channelService.createPublicChannel(publicChannelRequest);

		PrivateChannelCreateRequest privateChannelRequest = new PrivateChannelCreateRequest(List.of(hong.id(), lee.id()));
		ChannelResponse privateChannel = channelService.createPrivateChannel(privateChannelRequest);

		MessageCreateRequest messageRequest = new MessageCreateRequest("홍길동입니다!", publicChannel.id(), hong.id(), null);
		MessageResponse messageResponse = messageService.create(messageRequest);
		System.out.println(messageResponse);

		MessageCreateRequest privateMessageRequest = new MessageCreateRequest("안녕하세요!.", privateChannel.id(), hong.id(), null);
		MessageResponse privateMessageResponse = messageService.create(privateMessageRequest);
		System.out.println(privateMessageResponse);

		List<UserResponse> allUsers = userService.findAll();
		for (UserResponse user : allUsers) {
			System.out.println("유저: " + user.username() + " | 이메일: " + user.email() + " | 온라인 상태: " + user.isOnline());
		}

		List<ChannelResponse> allChannels = channelService.findAllByUserId(hong.id());
		for (ChannelResponse channel : allChannels) {
			System.out.println("채널: ID=" + channel.id() + " | 생성 시간=" + channel.createdAt() + " | 최신 메시지 시간=" + channel.latestMessageTime());
			System.out.println("채널 멤버: " + channel.memberIds());
		}
	}
}
