package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		Scanner sc = new Scanner(System.in);

		loop: while (true) {
			System.out.println("\n=== 메뉴 ===");
			System.out.println("1. 사용자");
			System.out.println("2. 채널");
			System.out.println("3. 메시지");
			System.out.println("4. 종료");
			System.out.print("번호 입력: ");

			String choice1 = sc.nextLine();


			switch (choice1) {
				case "1":
					System.out.println("\n===사용자 메뉴 ===");
					System.out.println("1. 사용자 등록");
					System.out.println("2. 사용자 조회");
					System.out.println("3. 사용자 전체 조회");
					System.out.println("4. 사용자 수정");
					System.out.println("5. 사용자 삭제");
					System.out.print("번호 입력: ");
					String choice11 = sc.nextLine();

					switch (choice11) {
						case "1":
							System.out.println("\n===사용자 등록 ===");
							System.out.print("사용자 이름 입력: ");
							String userName1 = sc.nextLine();
							System.out.print("사용자 이메일 입력: ");
							String userMail1 = sc.nextLine();
							System.out.print("사용자 비밀번호 입력: ");
							String userPassword1 = sc.nextLine();
							try {
								userService.create(userName1, userMail1, userPassword1);
							} catch (IllegalArgumentException e) {
								System.out.println(e.getMessage());
							}
							break;

						case "2":
							System.out.println("\n===사용자 조회 ===");
							System.out.print("사용자 UUID 입력: ");
							String userUuid1  =sc.nextLine();
							UUID userUuid2 = UUID.fromString(userUuid1);
							try {
								User userPrint = userService.getUser(userUuid2);
								System.out.println(userPrint);
							} catch (NoSuchElementException e) {
								System.out.println(e.getMessage());
							}
							break;

						case "3":
							System.out.println("\n===사용자 전체 조회 ===");
							List<User> printUser1 = userService.getAllUser();
							System.out.println();
							System.out.println(printUser1);
							break;

						case "4":
							System.out.println("\n===사용자 수정 ===");
							System.out.print("변경 할 사용자 UUID 입력: ");
							String userUuid3  =sc.nextLine();
							UUID userUuid4 = UUID.fromString(userUuid3);
							System.out.print("새로운 사용자 이름 입력: ");
							String changedName = sc.nextLine();
							System.out.print("새로운 사용자 이메일 입력: ");
							String changedEmail = sc.nextLine();
							System.out.print("새로운 사용자 비밀번호 입력: ");
							String changedPassword = sc.nextLine();
							try {
								userService.update(userUuid4, changedName, changedEmail, changedPassword);
							} catch (NoSuchElementException e){
								System.out.println(e.getMessage());
							}
							break;

						case "5":
							System.out.println("\n===사용자 삭제 ===");
							System.out.print("삭제 할 사용자 UUID 입력: ");
							String userUuid5  =sc.nextLine();
							UUID userUuid6 = UUID.fromString(userUuid5);
							try {
								userService.delete(userUuid6);
							} catch (NoSuchElementException e){
								System.out.println(e.getMessage());
							}
							break;

					}
					continue;

				case "2":
					System.out.println("\n===채널 메뉴 ===");
					System.out.println("1. 채널 생성");
					System.out.println("2. 채널 조회");
					System.out.println("3. 채널 전체 조회");
					System.out.println("4. 채널 수정");
					System.out.println("5. 채널 삭제");
					System.out.print("번호 입력: ");
					String choice12 = sc.nextLine();

					switch (choice12) {
						case "1":
							System.out.println("\n===채널 생성===");
							System.out.print("채널 이름 입력: ");
							String channelName1 = sc.nextLine();
							System.out.print("채널 설명 입력: ");
							String channelDesc = sc.nextLine();
							try {
								channelService.create(channelName1, channelDesc);
							} catch (IllegalArgumentException e) {
								System.out.println(e.getMessage());
							}
							break;

						case "2":
							System.out.println("\n===채널 조회===");
							System.out.print("채널 UUID 입력: ");
							String channelUuid1  =sc.nextLine();
							UUID channelUuid2 = UUID.fromString(channelUuid1);
							try {
								Channel channelPrint = channelService.getChannel(channelUuid2);
								System.out.println(channelPrint);
							} catch (NoSuchElementException e) {
								System.out.println(e.getMessage());
							}
							break;

						case "3":
							System.out.println("\n===채널 전체 조회===");
							List<Channel> channelPrint2 = channelService.getAllChannel();
							System.out.println();
							System.out.println(channelPrint2);
							break;

						case "4":
							System.out.println("\n===채널 수정===");
							System.out.print("변경 할 채널 UUID 입력: ");
							String channelUuid3  =sc.nextLine();
							UUID channelUuid4 = UUID.fromString(channelUuid3);
							System.out.print("새로운 채널 이름 입력: ");
							String newChannelName = sc.nextLine();
							System.out.print("새로운 채널 설명 입력: ");
							String newChannelDesc = sc.nextLine();
							try {
								channelService.update(channelUuid4, newChannelName, newChannelDesc);
							} catch (NoSuchElementException e) {
								System.out.println(e.getMessage());
							}
							break;

						case "5":
							System.out.println("\n===채널 삭제===");
							System.out.print("삭제할 채널 UUID 입력: ");
							String channelUuid5  =sc.nextLine();
							UUID channelUuid6 = UUID.fromString(channelUuid5);
							try {
								channelService.delete(channelUuid6);
							} catch (NoSuchElementException e) {
								System.out.println(e.getMessage());
							}
							break;

					}
					continue;

				case "3":
					System.out.println("\n===메시지 메뉴 ===");
					System.out.println("1. 메시지 생성");
					System.out.println("2. 메시지 조회");
					System.out.println("3. 메시지 전체 조회");
					System.out.println("4. 메시지 수정");
					System.out.println("5. 메시지 삭제");
					System.out.print("번호 입력: ");
					String choice13 = sc.nextLine();

					switch (choice13) {
						case "1":
							System.out.println("\n===메시지 생성===");
							System.out.print("보낼 메시지 내용 입력: ");
							String message = sc.nextLine();
							System.out.print("보낼 채널 UUID 입력: ");
							String messageUuid1  =sc.nextLine();
							UUID channelUuid2 = UUID.fromString(messageUuid1);
							System.out.print("보낼 사용자 UUID 입력: ");
							String messageUuid3  =sc.nextLine();
							UUID channelUuid4 = UUID.fromString(messageUuid3);
							try {
								messageService.create(message, channelUuid2, channelUuid4);
							} catch (IllegalArgumentException e) {
								System.out.println(e.getMessage());
							}
							break;

						case "2":
							System.out.println("\n===메시지 조회===");
							System.out.print("보낸 메시지 UUID 입력: ");
							String messageUuid5  =sc.nextLine();
							UUID messageUuid6 = UUID.fromString(messageUuid5);
							try {
								Message messagePrint1 = messageService.getMessage(messageUuid6);
								System.out.println(messagePrint1);
							} catch (NoSuchElementException e) {
								System.out.println(e.getMessage());
							}
							break;

						case "3":
							System.out.println("\n===메시지 전체 조회===");
							List<Message> messagePrint2 = messageService.getAllMessage();
							System.out.println(messagePrint2);
							break;

						case "4":
							System.out.println("\n===메시지 수정===");
							System.out.print("보낸 메시지 UUID 입력: ");
							String messageUuid7  =sc.nextLine();
							UUID messageUuid8 = UUID.fromString(messageUuid7);
							System.out.print("수정 할 메시지 입력: ");
							String newMessage = sc.nextLine();
							try {
								messageService.update(messageUuid8, newMessage);
								System.out.println();
							} catch (NoSuchElementException e) {
								System.out.println(e.getMessage());
							}
							break;

						case "5":
							System.out.println("\n===메시지 삭제===");
							System.out.print("메시지 UUID 입력: ");
							String messageUuid9  =sc.nextLine();
							UUID messageUuid10 = UUID.fromString(messageUuid9);
							try {
								messageService.delete(messageUuid10);
							} catch (NoSuchElementException e) {
								System.out.println(e.getMessage());
							}
							break;
					}
					continue;

				case "4":
					System.out.println("[Info] 종료합니다.");
					break loop;

				default:
					System.out.println("[Error] 잘못된 입력입니다.");
			}
		}
		sc.close();
	}
}
