package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.common.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.dto.channel.response.PublicChannelCreateResponse;
import com.sprint.mission.discodeit.dto.message.common.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageCreateResponse;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.ChannelService;
import com.sprint.mission.discodeit.service.basic.MessageService;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.domain.BinaryContentService;
import com.sprint.mission.discodeit.service.domain.ReadStatusService;
import com.sprint.mission.discodeit.service.domain.UserStatusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// 서비스 초기화
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		// User Create Test
		try{
			UserCreateRequest userCreateRequest = new UserCreateRequest("woody", "woody@codeit.com", "1234", Optional.empty());
			UserCreateResponse user = userService.create(userCreateRequest);
			System.out.println("SignUp User: " + user);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		// 동일 이름으로 생성 Test
		try {
			UserCreateRequest userCreateRequest = new UserCreateRequest("woody", "buzz@codeit.com", "1234", Optional.empty());
			UserCreateResponse user = userService.create(userCreateRequest);
			System.out.println("user = " + user);
		}catch(Exception e){
			System.out.println("중복 이름 에러: " + e.getMessage());
		}

		// 동일 이메일 생성 Test
		try{
			UserCreateRequest userCreateRequest = new UserCreateRequest("buzz", "woody@codeit.com", "1234", Optional.empty());
			UserCreateResponse user = userService.create(userCreateRequest);
			System.out.println("user = " + user);
		}catch(Exception e){
			System.out.println("중복 이메일 에러: " + e.getMessage());
		}

		// User find,findAll Test
		try{
			UserCreateRequest userCreateRequest = new UserCreateRequest("potato", "potato@codeit.com", "1234", Optional.empty());
			UserCreateResponse user = userService.create(userCreateRequest);

			System.out.println("find user: " + userService.find(user.id()));
			System.out.println("findAll: " + userService.findAll());

			// 저장 안 된 유저 조회
			System.out.println(userService.find(UUID.randomUUID()));
		}catch(Exception e){
			System.out.println("없는 유저 조회: " + e.getMessage());
		}

		// User update/delete Test
		try{
			UserCreateRequest userCreateRequest = new UserCreateRequest("rex", "rex@codeit.com", "1234", Optional.empty());
			UserCreateResponse user = userService.create(userCreateRequest);
			System.out.println("before update: " + userService.find(user.id()));

			userService.update(new UserUpdateRequest(user.id(), Optional.of("T-rex"), Optional.empty(), Optional.of("abcd"), Optional.empty()));
			System.out.println("after update: " + userService.find(user.id()));

			userService.delete(user.id());
			System.out.println("after delete find: " + userService.find(user.id()));
		}catch(Exception e){
			System.out.println("삭제 후 없는 유저 조회: " + e.getMessage());
		}

		// Test Setting
		User userA = new User("mike", "mike@codeit.com", "1234");
		User userB = new User("troll", "troll@codeit.com", "1234");
		List<User> users = List.of(userA, userB);

		// Channel(Private/Public) Create,Find Test
		try{
			// Create
			PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(users);
			PrivateChannelCreateResponse privateChannel = channelService.createPrivate(privateChannelCreateRequest);
			System.out.println("\nprivateChannel: " + privateChannel);

			PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest("codeit", "codeit 채널입니다.");
			PublicChannelCreateResponse publicChannel = channelService.createPublic(publicChannelCreateRequest);
			System.out.println("publicChannel: " + publicChannel);

			// Find
			System.out.println("privateChannelFind: " + channelService.find(privateChannel.id()));
			System.out.println("publicChannelFind: " + channelService.find(publicChannel.id()));

			// FindAllByUserId
			List<ChannelFindResponse> findAllByUserId = channelService.findAllByUserId(userA.getId());
			System.out.println("FindAllByUserId: " + findAllByUserId);

			// 없는 id로 조회
			System.out.println(channelService.find(UUID.randomUUID()));

		}catch(Exception e) {
			System.out.println("없는 id로 조회: " + e.getMessage());
		}

		// Test Setting
		PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest("sprint", "sprint 채널입니다.");
		PublicChannelCreateResponse publicChannel = channelService.createPublic(publicChannelCreateRequest);

		// Update/Delete Test
		try{
			System.out.println("before update: " + publicChannel);

			ChannelUpdateDto updateDto = new ChannelUpdateDto(publicChannel.id(), "aws", "aws 채널입니다.");
			channelService.update(updateDto);

			System.out.println("after update: " + channelService.find(publicChannel.id()));

			channelService.delete(publicChannel.id());
			System.out.println(channelService.find(publicChannel.id()));

		}catch(Exception e){
			System.out.println("삭제 후 채널 조회: " + e.getMessage());
		}

		// Test Setting
		PublicChannelCreateRequest request = new PublicChannelCreateRequest("messageTest", "messageTest 채널입니다.");
		PublicChannelCreateResponse channel = channelService.createPublic(request);

		UserCreateRequest userCreateRequest = new UserCreateRequest(userA.getUsername(), userA.getEmail(), userA.getPassword(), Optional.empty());
		UserCreateResponse messageTestUserA = userService.create(userCreateRequest);

		userCreateRequest = new UserCreateRequest(userB.getUsername(), userB.getEmail(), userB.getPassword(), Optional.empty());
		UserCreateResponse messageTestUserB = userService.create(userCreateRequest);

		// Message Create, FindAllByChannelId Test
		try{
			MessageCreateRequest messageCreateRequest = new MessageCreateRequest(channel.id(), messageTestUserA.id(), "messageA Content");
			MessageCreateResponse messageA = messageService.create(messageCreateRequest);
			System.out.println("\nmessageA: " + messageA);

			messageCreateRequest = new MessageCreateRequest(channel.id(), messageTestUserB.id(), "messageB Content");
			MessageCreateResponse messageB = messageService.create(messageCreateRequest);
			System.out.println("messageB: " + messageB);

			List<Message> messages = messageService.findAllByChannelId(channel.id());
			System.out.println("messages: " + messages);

			System.out.println(messageService.findAllByChannelId(UUID.randomUUID()));

		} catch (Exception e) {
			System.out.println("없는 channelId로 조회: " + e.getMessage());
		}

		// Test setting
		MessageCreateRequest messageCreateRequest = new MessageCreateRequest(channel.id(), messageTestUserA.id(), "TestMessage Content");
		MessageCreateResponse TestMessage = messageService.create(messageCreateRequest);

		// Update/Delete Test
		try{
			System.out.println("before update: " + TestMessage);

			MessageUpdateDto messageUpdateDto = new MessageUpdateDto(TestMessage.id(), "Update Content");
			messageUpdateDto = messageService.update(messageUpdateDto);
			System.out.println("after update: " + messageUpdateDto);

			System.out.println("before delete: " + messageService.findAllByChannelId(channel.id()));
			messageService.delete(messageUpdateDto.id());
			System.out.println("after delete: " + messageService.findAllByChannelId(channel.id()));

			messageUpdateDto = new MessageUpdateDto(UUID.randomUUID(), "Update Content");
			System.out.println(messageService.update(messageUpdateDto));

		} catch (Exception e) {
			System.out.println("없는 id로 업데이트: " + e.getMessage());
		}
	}

}
