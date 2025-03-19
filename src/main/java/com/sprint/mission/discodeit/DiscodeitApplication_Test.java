package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentDeleteDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentFindDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentUpdateDto;
import com.sprint.mission.discodeit.service.dto.channeldto.*;
import com.sprint.mission.discodeit.service.dto.messagedto.*;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusFindDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserDeleteDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserFindDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserUpdateDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusFindDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusUpdateDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication_Test {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication_Test.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);

        Scanner sc = new Scanner(System.in);

        loop:
        while (true) {
            System.out.println("\n=== 메뉴 ===");
            System.out.println("1. 사용자");
            System.out.println("2. 채널");
            System.out.println("3. 메시지");
            System.out.println("4. 테스트");
            System.out.println("5. 종료");
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
                            System.out.print("프로필 사진 입력: ");
                            String userProfile = sc.nextLine();
                            Path userProfilePath = Paths.get(userProfile);

                            UserCreateDto userCreateDto = new UserCreateDto(userName1, userMail1, userPassword1, userProfilePath);
                            BinaryContentCreateDto binaryContentCreateDto1 = new BinaryContentCreateDto(userProfilePath);

                            try {
                                User saveUser = userService.create(userCreateDto);
                                binaryContentService.create(binaryContentCreateDto1);
                                UserStatusCreateDto userStatusCreateDto = new UserStatusCreateDto(saveUser.getId());
                                userStatusService.create(userStatusCreateDto);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            break;

                        case "2":
                            System.out.println("\n===사용자 조회 ===");
                            System.out.print("사용자 UUID 입력: ");
                            String userUuid1 = sc.nextLine();
                            UUID userUuid2 = UUID.fromString(userUuid1);
                            System.out.print("채널 UUID 입력: ");
                            String channelUuid1 = sc.nextLine();
                            UUID channelUuid2 = UUID.fromString(channelUuid1);
                            System.out.print("프로필 UUID 입력: ");
                            String userUuid3 = sc.nextLine();
                            UUID userUuid4 = UUID.fromString(userUuid3);
                            System.out.print("read status UUID 입력: ");
                            String readStatusUuid3 = sc.nextLine();
                            UUID readStatusUuid4 = UUID.fromString(readStatusUuid3);
                            UserFindDto userFindDto = new UserFindDto(userUuid2, null, null, null, false);
                            UserStatusFindDto userStatusFindDto = new UserStatusFindDto(userUuid2, null, null, null, false);
                            BinaryContentFindDto binaryContentFindDto = new BinaryContentFindDto(userUuid4);
                            ReadStatusFindDto readStatusFindDto1 = new ReadStatusFindDto(readStatusUuid4, userUuid2);


                            try {
                                User userPrint = userService.getUser(userFindDto);
                                System.out.println(userPrint);
                                UserStatus userStatusPrint = userStatusService.getUser(userStatusFindDto);
                                System.out.println(userStatusPrint);
                                BinaryContent binaryContentPrint = binaryContentService.getUser(binaryContentFindDto);
                                System.out.println(binaryContentPrint);
                                ReadStatus print1 = readStatusService.find(readStatusFindDto1);
                                System.out.println(print1);
                                List<ReadStatus> print2 = readStatusService.findAllByUserId(readStatusFindDto1);
                                System.out.println(print2);
                            } catch (NoSuchElementException e) {
                                System.out.println(e.getMessage());
                            }
                            break;

                        case "3":
                            System.out.println("\n===사용자 전체 조회 ===");
                            List<User> printUser1 = userService.getAllUser();
                            System.out.println();
                            System.out.println(printUser1);
                            List<UserStatus> printUser2 = userStatusService.getAllUser();
                            System.out.println();
                            System.out.println(printUser2);
                            List<BinaryContent> printUser3 = binaryContentService.getAllUser();
                            System.out.println();
                            System.out.println(printUser3);
                            break;

                        case "4":
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
                            System.out.print("변경 할 프로필 UUID 입력: ");
                            String profileUuid1 = sc.nextLine();
                            UUID profileUuid2 = UUID.fromString(profileUuid1);
                            System.out.print("새로운 프로필 사진 입력: ");
                            String changeProfile = sc.nextLine();
                            Path changeProfilePath = Paths.get(changeProfile);
                            UserUpdateDto userUpdateDto = new UserUpdateDto(userUuid6, changedName, changedEmail, changedPassword, null);
                            UserStatusUpdateDto userStatusUpdateDto = new UserStatusUpdateDto(userUuid6);
                            BinaryContentUpdateDto binaryContentUpdateDto = new BinaryContentUpdateDto(profileUuid2, changeProfilePath);
                            try {
                                userService.update(userUpdateDto);
                                userStatusService.updateByUserId(userStatusUpdateDto);
                                binaryContentService.updateByUserId(binaryContentUpdateDto);
                            } catch (NoSuchElementException e) {
                                System.out.println(e.getMessage());
                            }
                            break;

                        case "5":
                            System.out.println("\n===사용자 삭제 ===");
                            System.out.print("삭제 할 사용자 UUID 입력: ");
                            String userUuid7 = sc.nextLine();
                            UUID userUuid8 = UUID.fromString(userUuid7);
                            System.out.print("삭제 할 프로필 UUID 입력: ");
                            String profileUuid3 = sc.nextLine();
                            UUID profileUuid4 = UUID.fromString(profileUuid3);
                            UserDeleteDto userDeleteDto = new UserDeleteDto(userUuid8, null, null, null);
                            UserStatusDeleteDto userStatusDeleteDto = new UserStatusDeleteDto(userUuid8);
                            BinaryContentDeleteDto binaryContentDeleteDto = new BinaryContentDeleteDto(profileUuid4);
                            try {
                                userService.delete(userDeleteDto);
                                userStatusService.delete(userStatusDeleteDto);
                                binaryContentService.delete(binaryContentDeleteDto);
                            } catch (NoSuchElementException e) {
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
                            ChannelCreatePublicDto channelCreatePublicDto1 = new ChannelCreatePublicDto(channelName1, channelDesc);
                            try {
                                channelService.createPublic(channelCreatePublicDto1);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            break;

                        case "2":
                            System.out.println("\n===채널 조회===");
                            System.out.print("채널 UUID 입력: ");
                            String channelUuid1 = sc.nextLine();
                            UUID channelUuid2 = UUID.fromString(channelUuid1);
                            ChannelFindRequestDto findRequestDto = new ChannelFindRequestDto(channelUuid2);
                            try {
                                ChannelFindResponseDto channelPrint = channelService.getChannel(findRequestDto);
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
                            List<ChannelFindAllByUserIdResponseDto> channelPrint2 = channelService.getAllChannel(channelFindAllByUserIdRequestDto);
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
                            String messageUuid1 = sc.nextLine();
                            UUID channelUuid2 = UUID.fromString(messageUuid1);
                            System.out.print("보낼 사용자 UUID 입력: ");
                            String messageUuid3 = sc.nextLine();
                            UUID channelUuid4 = UUID.fromString(messageUuid3);
                            MessageCreateDto messageCreateDto1 = new MessageCreateDto(message, channelUuid2, channelUuid4);
                            try {
                                messageService.create(messageCreateDto1);
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
                            List<MessageFindAllByChannelIdResponseDto> messagePrint2 = messageService.findallByChannelId(messageFindAllByChannelIdRequestDto);
                            System.out.println(messagePrint2);
                            break;

                        case "4":
                            System.out.println("\n===메시지 수정===");
                            System.out.print("보낸 메시지 UUID 입력: ");
                            String messageUuid9 = sc.nextLine();
                            UUID messageUuid10 = UUID.fromString(messageUuid9);
                            System.out.print("수정 할 메시지 입력: ");
                            String newMessage = sc.nextLine();
                            MessageUpdateDto messageUpdateDto1 = new MessageUpdateDto(messageUuid10, newMessage);
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
                    }
                    continue;
                case "4":
                    System.out.println("\n===테스트===");
                    System.out.println("===삭제===");
                    System.out.print("Read Status UUID 입력: ");
                    String Test1 = sc.nextLine();
                    UUID Test2 = UUID.fromString(Test1);
                    ReadStatusDeleteDto readStatusDeleteDto = new ReadStatusDeleteDto(Test2);
                    try {
                        readStatusService.delete(readStatusDeleteDto);
                    } catch (NoSuchElementException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case "5":
                    System.out.println("[Info] 종료합니다.");
                    break loop;

                default:
                    System.out.println("[Error] 잘못된 입력입니다.");
            }
        }
        sc.close();
    }
}
