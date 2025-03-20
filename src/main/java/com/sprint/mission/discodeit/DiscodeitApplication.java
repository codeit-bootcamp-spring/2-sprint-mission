package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.dto.AuthServiceLoginDto;
import com.sprint.mission.discodeit.service.dto.channeldto.*;
import com.sprint.mission.discodeit.service.dto.messagedto.*;
import com.sprint.mission.discodeit.service.dto.userdto.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        AuthService authService = context.getBean(AuthService.class);

        boolean isLoggedIn = false;

        Scanner sc = new Scanner(System.in);

        loop:
        while (true) {
            User currentUser = null;
            if (!isLoggedIn) {
                System.out.println("\n===초기화면===");
                System.out.println("1. 로그인");
                System.out.println("2. 사용자 등록");
                System.out.println("3. 종료");
                System.out.print("번호 입력: ");

                String choice1 = sc.nextLine();

                switch (choice1) {
                    case "1":
                        System.out.print("사용자 이름 입력: ");
                        String loginName1 = sc.nextLine();
                        System.out.print("사용자 비밀번호 입력: ");
                        String loginPassword2 = sc.nextLine();
                        AuthServiceLoginDto authServiceLoginDto = new AuthServiceLoginDto(loginName1, loginPassword2);
                        try {
                            User loginUser = authService.login(authServiceLoginDto);
                            isLoggedIn = true;
                            currentUser = loginUser;
                        } catch (NoSuchElementException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case "2":
                        System.out.println("\n===사용자 등록 ===");
                        System.out.print("사용자 이름 입력: ");
                        String userName1 = sc.nextLine();
                        System.out.print("사용자 이메일 입력: ");
                        String userMail1 = sc.nextLine();
                        System.out.print("사용자 비밀번호 입력: ");
                        String userPassword1 = sc.nextLine();
                        System.out.print("프로필 사진 입력: ");
                        String userProfile = sc.nextLine();
                        Path userProfilePath = Paths.get(userProfile);
                        UserCreateDto userCreateDto = new UserCreateDto(userName1, userMail1, userPassword1, userProfilePath);
                        try {
                            userService.create(userCreateDto);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case "3":
                        System.out.println("[Info] 종료합니다.");
                        break loop;
                }
            } else {
                System.out.println("\n=== 메뉴 ===");
                System.out.println("1. 사용자");
                System.out.println("2. 채널");
                System.out.println("3. 메시지");
                System.out.println("4. 로그아웃");
                System.out.print("번호 입력: ");

                String choice11 = sc.nextLine();
                switch (choice11) {
                    case "1":
                        System.out.println("\n===사용자 메뉴 ===");
                        System.out.println("1. 사용자 조회");
                        System.out.println("2. 사용자 전체 조회");
                        System.out.println("3. 사용자 수정");
                        System.out.println("4. 사용자 삭제");
                        System.out.println("5. 뒤로 가기");
                        System.out.print("번호 입력: ");

                        String choice111 = sc.nextLine();

                        switch (choice111) {
                            case "1":
                                System.out.println("\n===사용자 조회 ===");
                                System.out.print("사용자 UUID 입력: ");
                                String userUuid1 = sc.nextLine();
                                UUID userUuid2 = UUID.fromString(userUuid1);
                                UserFindRequestDto userFindDto = new UserFindRequestDto(userUuid2);
                                try {
                                    UserFindResponseDto userPrint = userService.find(userFindDto);
                                    System.out.println(userPrint);
                                } catch (NoSuchElementException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "2":
                                System.out.println("\n===사용자 전체 조회 ===");
                                List<UserFindAllResponseDto> printUser1 = userService.findAllUser();
                                System.out.println();
                                System.out.println(printUser1);
                                break;

                            case "3":
                                System.out.println("\n===사용자 수정 ===");
                                System.out.print("변경 할 사용자 UUID 입력: ");
                                String userUuid5 = sc.nextLine();
                                UUID userUuid6 = UUID.fromString(userUuid5);
                                System.out.print("새로운 사용자 이름 입력: ");
                                String changedName = sc.nextLine();
                                System.out.print("새로운 사용자 이메일 입력: ");
                                String changedEmail = sc.nextLine();
                                System.out.print("새로운 사용자 비밀번호 입력: ");
                                String changedPassword = sc.nextLine();
                                System.out.print("새로운 프로필 사진 입력: ");
                                String changeProfile = sc.nextLine();
                                Path changeProfilePath = Paths.get(changeProfile);
                                UserUpdateDto userUpdateDto1 = new UserUpdateDto(userUuid6, changedName, changedEmail, changedPassword, changeProfilePath);
                                try {
                                    userService.update(userUpdateDto1);
                                } catch (NoSuchElementException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "4":
                                System.out.println("\n===사용자 삭제 ===");
                                System.out.print("삭제 할 사용자 UUID 입력: ");
                                String userUuid7 = sc.nextLine();
                                UUID userUuid8 = UUID.fromString(userUuid7);
                                UserDeleteDto userDeleteDto = new UserDeleteDto(userUuid8);
                                try {
                                    userService.delete(userDeleteDto);
                                } catch (NoSuchElementException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "5":
                                continue;
                        }
                        break;

                    case "2":
                        System.out.println("\n===채널 메뉴 ===");
                        System.out.println("1. 채널 생성");
                        System.out.println("2. 채널 조회");
                        System.out.println("3. 채널 전체 조회");
                        System.out.println("4. 채널 수정");
                        System.out.println("5. 채널 삭제");
                        System.out.println("6. 뒤로 가기");
                        System.out.print("번호 입력: ");

                        String choice112 = sc.nextLine();

                        switch (choice112) {
                            case "1":
                                System.out.println("\n===채널 생성 ===");
                                System.out.println("1. 공개 채널 생성");
                                System.out.println("2. 사설 채널 생성");
                                System.out.print("번호 입력: ");
                                String choice1121 = sc.nextLine();

                                switch (choice1121) {
                                    case "1":
                                        System.out.println("\n===공개 채널 생성===");
                                        System.out.print("채널 이름 입력: ");
                                        String channelName1 = sc.nextLine();
                                        System.out.print("채널 설명 입력: ");
                                        String channelDesc1 = sc.nextLine();
                                        ChannelCreatePublicDto channelCreatePublicDto1 = new ChannelCreatePublicDto(channelName1, channelDesc1);
                                        try {
                                           channelService.createPublic(channelCreatePublicDto1);
                                        } catch (IllegalArgumentException e) {
                                            System.out.println(e.getMessage());
                                        }
                                        break;

                                    case "2":
                                        System.out.println("\n===사설 채널 생성===");
                                        System.out.print("사용자 UUID 입력: ");
                                        String channelUserUuid1 = sc.nextLine();
                                        UUID channelUserUuid2 = UUID.fromString(channelUserUuid1);
                                        ChannelCreatePrivateDto channelCreatePrivateDto1 = new ChannelCreatePrivateDto(channelUserUuid2);
                                        try {
                                            channelService.createPrivate(channelCreatePrivateDto1);
                                        } catch (IllegalArgumentException e) {
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                break;

                            case "2":
                                System.out.println("\n===채널 조회===");
                                System.out.print("채널 UUID 입력: ");
                                String channelUuid1 = sc.nextLine();
                                UUID channelUuid2 = UUID.fromString(channelUuid1);
                                ChannelFindRequestDto findRequestDto = new ChannelFindRequestDto(channelUuid2);
                                try {
                                    ChannelFindResponseDto channelPrint = channelService.find(findRequestDto);
                                    System.out.println(channelPrint);
                                } catch (NoSuchElementException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "3":
                                System.out.println("\n===채널 전체 조회===");
                                System.out.print("사용자 UUID 입력: ");
                                String channelAllUuid1 = sc.nextLine();
                                UUID channelAllUuid2 = UUID.fromString(channelAllUuid1);
                                ChannelFindAllByUserIdRequestDto channelFindAllByUserIdRequestDto = new ChannelFindAllByUserIdRequestDto(channelAllUuid2);
                                List<ChannelFindAllByUserIdResponseDto> channelPrint2 = channelService.findAllByUserId(channelFindAllByUserIdRequestDto);
                                System.out.println();
                                System.out.println(channelPrint2);
                                break;

                            case "4":
                                System.out.println("\n===채널 수정===");
                                System.out.print("변경 할 채널 UUID 입력: ");
                                String channelUuid3 = sc.nextLine();
                                UUID channelUuid4 = UUID.fromString(channelUuid3);
                                System.out.print("새로운 채널 이름 입력: ");
                                String newChannelName = sc.nextLine();
                                System.out.print("새로운 채널 설명 입력: ");
                                String newChannelDesc = sc.nextLine();
                                ChannelUpdateDto channelUpdateDto1 = new ChannelUpdateDto(channelUuid4, newChannelName, newChannelDesc);
                                try {
                                    channelService.update(channelUpdateDto1);
                                } catch (NoSuchElementException | IllegalStateException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "5":
                                System.out.println("\n===채널 삭제===");
                                System.out.print("삭제할 채널 UUID 입력: ");
                                String channelUuid5 = sc.nextLine();
                                UUID channelUuid6 = UUID.fromString(channelUuid5);
                                ChannelDeleteDto channelDeleteDto1 = new ChannelDeleteDto(channelUuid6);
                                try {
                                    channelService.delete(channelDeleteDto1);
                                } catch (NoSuchElementException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "6":
                                continue;
                        }
                        break;

                    case "3":
                        System.out.println("\n===메시지 메뉴 ===");
                        System.out.println("1. 메시지 생성");
                        System.out.println("2. 메시지 조회");
                        System.out.println("3. 메시지 전체 조회");
                        System.out.println("4. 메시지 수정");
                        System.out.println("5. 메시지 삭제");
                        System.out.println("6. 뒤로 가기");
                        System.out.print("번호 입력: ");
                        String choice113 = sc.nextLine();

                        switch (choice113) {
                            case "1":
                                System.out.println("\n===메시지 생성===");
                                System.out.print("보낼 메시지 내용 입력: ");
                                String message = sc.nextLine();
                                System.out.print("보낼 채널 UUID 입력: ");
                                String messageUuid1 = sc.nextLine();
                                UUID channelUuid2 = UUID.fromString(messageUuid1);
                                System.out.print("보낼 사용자 UUID 입력: ");
                                String messageUuid3 = sc.nextLine();
                                UUID channelUuid4 = UUID.fromString(messageUuid3);

                                List<String> userProfiles1 = new ArrayList<>();
                                for (int i = 0; i < 3; i++) {
                                    System.out.print("첨부파일 경로 입력(최대 3개): ");
                                    String inputProfile = sc.nextLine();
                                    userProfiles1.add(inputProfile);
                                }
                                List<Path> userProfilePaths = new ArrayList<>();
                                for (String userProfile : userProfiles1) {
                                    Path userProfilePath = Paths.get(userProfile);
                                    userProfilePaths.add(userProfilePath);
                                }
                                MessageCreateDto messageCreateDto1 = new MessageCreateDto(message, channelUuid2, channelUuid4, userProfilePaths);
                                try {
                                   Message createdMessagePrint = messageService.create(messageCreateDto1);
                                   System.out.println(createdMessagePrint);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "2":
                                System.out.println("\n===메시지 조회===");
                                System.out.print("보낸 메시지 UUID 입력: ");
                                String messageUuid5 = sc.nextLine();
                                UUID messageUuid6 = UUID.fromString(messageUuid5);
                                MessageFindRequestDto messageFindRequestDto = new MessageFindRequestDto(messageUuid6);
                                try {
                                    MessageFindResponseDto messagePrint1 = messageService.find(messageFindRequestDto);
                                    System.out.println(messagePrint1);
                                } catch (NoSuchElementException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "3":
                                System.out.println("\n===메시지 전체 조회===");
                                System.out.print("채널 UUID 입력: ");
                                String messageUuid7 = sc.nextLine();
                                UUID messageUuid8 = UUID.fromString(messageUuid7);
                                MessageFindAllByChannelIdRequestDto messageFindAllByChannelIdRequestDto = new MessageFindAllByChannelIdRequestDto(messageUuid8);
                                List<MessageFindAllByChannelIdResponseDto> messagePrint2 = messageService.findAllByChannelId(messageFindAllByChannelIdRequestDto);
                                System.out.println(messagePrint2);
                                break;

                            case "4":
                                System.out.println("\n===메시지 수정===");
                                System.out.print("보낸 메시지 UUID 입력: ");
                                String messageUuid9 = sc.nextLine();
                                UUID messageUuid10 = UUID.fromString(messageUuid9);
                                System.out.print("수정 할 메시지 입력: ");
                                String newMessage = sc.nextLine();

                                List<String> userProfiles2 = new ArrayList<>();
                                for (int i = 0; i < 3; i++) {
                                    System.out.print("첨부파일 경로 입력(최대 3개): ");
                                    String inputProfile = sc.nextLine();
                                    userProfiles2.add(inputProfile);
                                }
                                List<Path> userProfilePaths2 = new ArrayList<>();
                                for (String userProfile : userProfiles2) {
                                    Path userProfilePath = Paths.get(userProfile);
                                    userProfilePaths2.add(userProfilePath);
                                }

                                MessageUpdateDto messageUpdateDto1 = new MessageUpdateDto(messageUuid10, newMessage, userProfilePaths2);
                                try {
                                    Message messagePrint3 = messageService.update(messageUpdateDto1);
                                    System.out.println(messagePrint3);
                                } catch (NoSuchElementException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "5":
                                System.out.println("\n===메시지 삭제===");
                                System.out.print("메시지 UUID 입력: ");
                                String messageUuid11 = sc.nextLine();
                                UUID messageUuid12 = UUID.fromString(messageUuid11);
                                MessageDeleteDto messageDeleteDto = new MessageDeleteDto(messageUuid12);
                                try {
                                    messageService.delete(messageDeleteDto);
                                } catch (NoSuchElementException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case "6":
                                continue;
                        }
                        break;

                    case "4":
                        isLoggedIn = false;
                        System.out.println("[Info] 로그아웃 합니다.");
                        continue;

                    default:
                        System.out.println("[Error] 잘못된 입력입니다.");
                }
            }
        }
        sc.close();
    }
}
