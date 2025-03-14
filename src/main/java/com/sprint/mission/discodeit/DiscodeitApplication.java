package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		//SpringApplication.run(DiscodeitApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		final UserService userService = context.getBean(UserService.class);
		final ChannelService channelService = context.getBean(ChannelService.class);
		final MessageService messageService = context.getBean(MessageService.class);

		Scanner sc = new Scanner(System.in);

		UUID userToken = null;
		UUID channelToken = null;

		while (true) {
			if (userToken == null) {
				userToken = initPage(userService);
			}
			if (userToken == null) continue;

			System.out.println(" ======== 메뉴 ======== ");
			System.out.println("1. 채널 이용\n2. 조회\n3. 수정\n4. 삭제\n5. 로그아웃\n6. 나가기");
			System.out.print("입력란: ");

			int num = sc.nextInt();
			sc.nextLine();

			switch (num) {
				case 1:
					System.out.println("===채널 이용===");
					System.out.println("1. 채널 개설\n2. 채널 선택\n3. 개설된 채널 조회\n4. 메뉴로 돌아가기");
					System.out.print("입력란: ");
					int createNum = sc.nextInt();
					sc.nextLine();
					switch (createNum) {
						case 1:
							System.out.print("채널명 입력: ");
							String channelName = sc.nextLine();
							channelService.createChannel(channelName);
							break;
						case 2:
							System.out.print("채널 아이디 입력: ");
							UUID channelUUID = UUID.fromString(sc.nextLine());
							channelToken = selectChannel(channelService, channelUUID);
							if (channelToken == null) break;
							messageService.findMessageByChannelId(channelToken)
									.forEach(System.out::println);
							System.out.println("------ 메세지 보내기 ------");
							String content = sc.nextLine();
							sendMessageByChannel(messageService ,channelToken, userToken, content);
							break;
						case 3:
							System.out.println(channelService.findAllChannel());
						case 4:
							break;
					}
					break;
				case 2:
					System.out.println("=== 조회 ===");
					System.out.println("1. 사용자 조회\n2. 채널 조회\n3. 메세지 조회\n4. 메뉴로 돌아가기");
					System.out.print("입력란: ");
					int searchNum = sc.nextInt();
					searchByNum(userService, channelService, messageService, searchNum);
					break;
				case 3:
					System.out.println("=== 수정 ===");
					System.out.println("1. 사용자 이름 수정\n2. 채널명 수정\n3. 메세지 수정\n4. 메뉴로 돌아가기");
					System.out.print("입력란: ");
					int updateNum = sc.nextInt();
					sc.nextLine();
					updateByNum(userService, channelService, messageService, updateNum);
					break;
				case 4:
					System.out.println("=== 삭제 ===");
					System.out.println("1. 사용자 삭제\n2. 채널 삭제\n3. 메시지 삭제\n4. 메뉴로 돌아가기");
					System.out.print("입력란: ");
					int deleteNum = sc.nextInt();
					sc.nextLine();
					deleteByNum(userService, channelService, messageService, deleteNum);
					break;
				case 5:
					userToken = null;
					break;
				default:
					return;
			}
		}
	}

	private static UUID initPage(UserService userService) {
		Scanner sc = new Scanner(System.in);
		System.out.println("\n1. 로그인\n2. 회원 가입");
		System.out.print("입력: ");
		int choice = sc.nextInt();
		sc.nextLine();
		switch (choice) {
			case 1:
				return loginPage(userService);
			case 2:
				registerPage(userService);
				break;
		}
		return null;
	}

	private static UUID loginPage(UserService userService) {
		Scanner sc = new Scanner(System.in);
		System.out.print("유저 아이디 입력: ");
		UUID userUUID = UUID.fromString(sc.nextLine());
		System.out.print("비밀번호 입력: ");
		String password = sc.nextLine();
		return userService.login(userUUID, password);
	}

	private static void registerPage(UserService userService) {
		Scanner sc = new Scanner(System.in);
		System.out.print("닉네임 입력: ");
		String nickname = sc.nextLine();
		System.out.print("비밀번호 입력: ");
		String password = sc.nextLine();
		userService.save(nickname, password);
	}

	private static UUID selectChannel(ChannelService channelService, UUID channelUUID) {
		Channel channel = channelService.findChannel(channelUUID);
		if (channel == null) return null;
		return channel.getId();
	}

	private static void sendMessageByChannel(MessageService messageService, UUID channelUUID, UUID userUUID, String content) {
		while (true) {
			if (content.equalsIgnoreCase("EXIT")) return;
			messageService.sendMessage(channelUUID, userUUID, content);
		}
	}

	private static void searchByNum(UserService userService, ChannelService channelService, MessageService messageService,int searchNum) {
		Scanner sc = new Scanner(System.in);
		switch (searchNum) {
			case 1:
				System.out.println("=== 조회 방법 ===");
				System.out.println("1. 단건 조회\n2. 다건 조회");
				System.out.print("입력란: ");
				int findNum1 = sc.nextInt();
				sc.nextLine();
				switch (findNum1) {
					case 1:
						System.out.print("조회할 사용자 아이디: ");
						UUID UserUUID = UUID.fromString(sc.nextLine());
						System.out.println(userService.findByUser(UserUUID));
						break;
					case 2:
						userService.findAllUser().forEach(System.out::println);
						break;
				}
				break;
			case 2:
				System.out.println("=== 조회 방법 ===");
				System.out.println("1. 단건 조회\n2. 다건 조회");
				System.out.print("입력란: ");
				int findNum2 = sc.nextInt();
				sc.nextLine();
				switch (findNum2) {
					case 1:
						System.out.print("조회할 채널 아이디: ");
						UUID chennelUUID = UUID.fromString(sc.nextLine());
						System.out.println(channelService.findChannel(chennelUUID));
						break;
					case 2:
						channelService.findAllChannel().forEach(System.out::println);
				}
				break;
			case 3:
				System.out.println("=== 조회 방법 ===");
				System.out.println("1. 단건 조회\n2. 다건 조회\n3. 채널별 조회");
				System.out.print("입력란: ");
				int findNum3 = sc.nextInt();
				sc.nextLine();
				switch (findNum3) {
					case 1:
						System.out.print("조회할 메시지 아이디: ");
						UUID MessageUUID = UUID.fromString(sc.nextLine());
						System.out.println(messageService.findMessageById(MessageUUID));
						break;
					case 2:
						messageService.findAllMessages()
								.forEach(System.out::println);
						break;
					case 3:
						System.out.print("조회할 채널 아이디: ");
						UUID channelUUID = UUID.fromString(sc.nextLine());
						messageService.findMessageByChannelId(channelUUID)
								.forEach(System.out::println);
				}
				break;
		}
	}

	private static void updateByNum(UserService userService, ChannelService channelService, MessageService messageService, int updateNum) {
		Scanner sc = new Scanner(System.in);
		switch (updateNum) {
			case 1:
				System.out.print("변경할 사용자 아이디 입력: ");
				UUID userUUID = UUID.fromString(sc.nextLine());
				System.out.print("원하는 닉네임 입력: ");
				String nickname = sc.nextLine();
				userService.update(userUUID, nickname);
				return;
			case 2:
				System.out.print("변경할 채널 아이디 입력: ");
				UUID channelUUID = UUID.fromString(sc.nextLine());
				System.out.print("원하는 채널명 입력: ");
				String channelName = sc.nextLine();
				channelService.updateChannel(channelUUID, channelName);
				return;
			case 3:
				System.out.print("수정할 메세지 아이디 입력: ");
				UUID messageUUID = UUID.fromString(sc.nextLine());
				System.out.print("수정 메시지 입력: ");
				String message = sc.nextLine();
				messageService.updateMessage(messageUUID, message);
				return;
			default:
				System.out.println("잘못된 입력값");
		}
	}

	private static void deleteByNum(UserService userService, ChannelService channelService, MessageService messageService, int deleteNum) {
		Scanner sc = new Scanner(System.in);
		switch (deleteNum) {
			case 1:
				System.out.print("삭제할 사용자 아이디: ");
				UUID userUUID = UUID.fromString(sc.nextLine());
				userService.delete(userUUID);
				return;
			case 2:
				System.out.print("삭제할 채널 아이디: ");
				UUID channelUUID = UUID.fromString(sc.nextLine());
				channelService.deleteChannel(channelUUID);
				return;
			case 3:
				System.out.print("삭제할 메시지 아이디: ");
				UUID messageUUID = UUID.fromString(sc.nextLine());
				messageService.deleteMessageById(messageUUID);
				return;
			default:
				System.out.println("잘못된 입력값");
		}
	}
}
