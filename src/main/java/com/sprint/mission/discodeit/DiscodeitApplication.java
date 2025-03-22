package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.menu.ChannelMenu;
import com.sprint.mission.discodeit.menu.MainMenu;
import com.sprint.mission.discodeit.menu.MessageMenu;
import com.sprint.mission.discodeit.menu.UserMenu;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
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

        while (true) {
            System.out.println("\n===== 메뉴 선택 =====");
            System.out.println("1. 사용자 관리");
            System.out.println("2. 채널 관리");
            System.out.println("3. 메시지 관리");
            System.out.println("0. 종료");
            System.out.println("번호 입력: ");

            MainMenu mainChoice;
            try {
                mainChoice = MainMenu.from(sc.nextInt());
            } catch (IllegalArgumentException e) {
                System.out.println("다시 입력하세요.");
                continue;
            }
            sc.nextLine();

            switch (mainChoice) {
                case USER_MANAGEMENT:
                    while (true) {
                        System.out.println("\n[사용자 관리]");
                        System.out.println("1. 사용자 등록");
                        System.out.println("2. 사용자 조회");
                        System.out.println("0. 이전 메뉴로");
                        System.out.print("선택: ");

                        UserMenu userChoice;
                        try {
                            userChoice = UserMenu.from(sc.nextInt());
                        } catch (IllegalArgumentException e) {
                             System.out.println("다시 입력하세요.");
                            continue;
                        }
                        sc.nextLine();

                        if (userChoice == UserMenu.BACK) {
                            break;
                        }

                        switch (userChoice) {
                            case CREATE_USER:
                                System.out.print("사용자 이름 입력: ");
                                String userName = sc.nextLine();
                                System.out.print("사용자 비밀번호 입력: ");
                                String password = sc.nextLine();
                                System.out.print("사용자 이메일 입력: ");
                                String userEmail = sc.nextLine();
                                CreateUserRequest request = new CreateUserRequest(userName, password, userEmail, null, null);
                                User user = userService.createUser(request);
                                System.out.println("사용자 생성 완료: " + user);
                                break;

                            case GET_USER:
                                System.out.print("조회할 사용자 ID 입력: ");
                                UUID userId = UUID.fromString(sc.nextLine());
                                System.out.println("사용자 조회: " + userService.getUserById(userId)
                                        .map(UserResponse::toString).orElse("사용자를 찾을 수 없습니다."));
                                break;
                        }
                        break;
                    }
                    break;

                case CHANNEL_MANAGEMENT:
                    while (true) {
                        System.out.println("\n[채널 관리]");
                        System.out.println("1. 채널 등록");
                        System.out.println("2. 채널 조회");
                        System.out.println("3. 채널 수정");
                        System.out.println("4. 채널 삭제");
                        System.out.println("0. 이전 메뉴로");
                        System.out.print("선택: ");

                        ChannelMenu channelChoice;
                        try {
                            channelChoice = ChannelMenu.from(sc.nextInt());
                        } catch (IllegalArgumentException e) {
                            System.out.println("다시 입력하세요.");
                            continue;
                        }
                        sc.nextLine();

                        if (channelChoice == ChannelMenu.BACK) {
                            break;
                        }

                        switch (channelChoice) {
                            case CREATE_CHANNEL:
                                System.out.println("채널 이름 입력: ");
                                String channelName = sc.nextLine();
                                PublicChannelCreateRequest request = new PublicChannelCreateRequest(channelName);
                                Channel channel = channelService.createPublicChannel(request);
                                System.out.println("채널 생성 완료: " + channel);
                                break;

                            case GET_CHANNEL:
                                System.out.print("조회할 채널 ID 입력: ");
                                UUID channelId = UUID.fromString(sc.nextLine());
                                System.out.println("채널 조회: " + channelService.getChannelById(channelId)
                                        .map(ChannelResponse::toString).orElse("채널을 찾을 수 없습니다."));
                                break;

                            case UPDATE_CHANNEL:
                                System.out.print("수정할 채널 ID 입력: ");
                                UUID channelIdToUpdate = UUID.fromString(sc.nextLine());
                                System.out.print("새로운 채널 입력: ");
                                String newChannelName = sc.nextLine();
                                UpdateChannelRequest updateChannelRequest = new UpdateChannelRequest(channelIdToUpdate, newChannelName);
                                channelService.updateChannel(updateChannelRequest);
                                System.out.println("채널 수정 완료");
                                break;

                            case DELETE_CHANNEL:
                                System.out.println("삭제할 채널 ID 입력: ");
                                UUID channelIdToDelete = UUID.fromString(sc.nextLine());
                                channelService.deleteChannel(channelIdToDelete);
                                System.out.println("채널 삭제 완료");
                                break;
                        }
                        break;
                    }
                    break;

                case MESSAGE_MANAGEMENT:
                    while (true) {
                        System.out.println("\n[메시지 관리]");
                        System.out.println("1. 메시지 등록");
                        System.out.println("2. 메시지 조회");
                        System.out.println("3. 메시지 수정");
                        System.out.println("4. 메시지 삭제");
                        System.out.println("0. 이전 메뉴로");
                        System.out.print("선택: ");

                        MessageMenu messageChoice;
                        try {
                            messageChoice = MessageMenu.from(sc.nextInt());
                        } catch (IllegalArgumentException e) {
                            System.out.println("다시 입력하세요.");
                            continue;
                        }
                        sc.nextLine();

                        if (messageChoice == MessageMenu.BACK) {
                            break;
                        }

                        switch (messageChoice) {
                            case CREATE_MESSAGE:
                                System.out.print("사용자 ID 입력: ");
                                String userIdInput = sc.nextLine().trim();
                                UUID userId = UUID.fromString(userIdInput);

                                System.out.print("채널 ID 입력: ");
                                String channelIdInput = sc.nextLine().trim();
                                UUID channelId = UUID.fromString(channelIdInput);

                                System.out.print("메시지 내용 입력: ");
                                String messageText = sc.nextLine();

                                CreateMessageRequest request = new CreateMessageRequest(userId, channelId, messageText, new ArrayList<>());
                                Message message = messageService.createMessage(request);
                                System.out.println("메시지 생성 완료: " + message);
                                break;

                            case GET_MESSAGE:
                                System.out.print("조회할 메시지 ID 입력: ");
                                UUID messageId = UUID.fromString(sc.nextLine());
                                System.out.println("메시지 조회: " + messageService.getMessageById(messageId)
                                        .map(Message::toString).orElse("메시지를 찾을 수 없습니다."));
                                break;

                            case UPDATE_MESSAGE:
                                System.out.print("수정할 메시지 ID 입력: ");
                                UUID messageIdToUpdate = UUID.fromString(sc.nextLine());
                                System.out.println("새로운 메시지 내용 입력: ");
                                String newMessageText = sc.nextLine();
                                UpdateMessageRequest updateRequest = new UpdateMessageRequest(messageIdToUpdate, newMessageText);
                                messageService.updateMessage(updateRequest);
                                System.out.println("메시지 수정 완료");
                                break;

                            case DELETE_MESSAGE:
                                System.out.print("삭제할 메시지 ID 입력: ");
                                UUID messageIdToDelete = UUID.fromString(sc.nextLine());
                                messageService.deleteMessage(messageIdToDelete);
                                System.out.println("메시지 삭제 완료");
                                break;
                        }
                        break;
                    }
                    break;

                case EXIT:
                    System.out.println("프로그램 종료");
                    sc.close();
                    return;
            }
        }
    }
}
