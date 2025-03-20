package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        //SpringApplication.run(DiscodeitApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        final UserService userService = context.getBean(UserService.class);
        final AuthService authService = context.getBean(BasicAuthService.class);
        final ChannelService channelService = context.getBean(ChannelService.class);
        final MessageService messageService = context.getBean(MessageService.class);
        final UserStatusService userStatusService = context.getBean(UserStatusService.class);
        final BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);

        Scanner sc = new Scanner(System.in);

        UUID userToken = null;
        UUID channelToken = null;

        while (true) {
            if (userToken == null) {
                userToken = initPage(userService, authService, binaryContentService);
            }
            if (userToken == null) continue;
            userStatusService.updateByUserId(userToken);
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
                            System.out.println("======== 채널 타입 ========");
                            System.out.print("1. public(공개 채널)\n2. private(비공개 채널) : ");
                            int typeChoice = sc.nextInt();
                            sc.nextLine();
                            System.out.print("채널명 입력: ");
                            String channelName = sc.nextLine();
                            switch (typeChoice) {
                                case 1:
                                    System.out.println(channelService.createPublicChannel(channelName, ChannelType.PUBLIC));
                                    break;
                                case 2:
                                    List<UUID> userList = new ArrayList<>();
                                    userList.add(userToken);
                                    while (true) {
                                        System.out.println();
                                        System.out.print("추가할 사용자 아이디\n입력란:");
                                        String appendUser = sc.nextLine();
                                        if (appendUser.equalsIgnoreCase("EXIT")) break;
                                        UUID userUUID = UUID.fromString(appendUser);
                                        if (userService.findByUser(userUUID) != null) {
                                            userList.add(userUUID);
                                        }
                                    }
                                    System.out.println(channelService.createPrivateChannel(channelName, ChannelType.PRIVATE, userList));
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            System.out.print("채널 아이디 입력: ");
                            UUID channelUUID = UUID.fromString(sc.nextLine());
                            channelToken = selectChannel(channelService, channelUUID);
                            if (channelToken == null) break;
                            messageService.findMessageByChannelId(channelToken)
                                    .forEach(System.out::println);
                            sendMessageByChannel(messageService, binaryContentService, channelToken, userToken);
                            break;
                        case 3:
                            System.out.println(channelService.findAllByUserId(userToken));
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
                    updateByNum(userService, channelService, messageService, binaryContentService,updateNum);
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

    private static UUID initPage(UserService userService, AuthService authService, BinaryContentService binaryContentService) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n1. 로그인\n2. 회원 가입");
        System.out.print("입력: ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                return loginPage(authService);
            case 2:
                registerPage(userService, binaryContentService);
                break;
        }
        return null;
    }

    private static UUID loginPage(AuthService authService) {
        Scanner sc = new Scanner(System.in);
        System.out.print("유저 아이디 입력: ");
        String username = sc.nextLine();
        System.out.print("비밀번호 입력: ");
        String password = sc.nextLine();

        User user = authService.login(username, password);
        if (user == null) {
            return null;
        }
        return authService.login(username, password).getId();
    }

    private static void registerPage(UserService userService, BinaryContentService binaryContentService) {
        try {
            Scanner sc = new Scanner(System.in);
            UUID profileId = null;

            System.out.print("아이디 입력: ");
            String username = sc.nextLine();
            System.out.print("비밀번호 입력: ");
            String password = sc.nextLine();
            System.out.print("닉네임 입력: ");
            String nickname = sc.nextLine();
            System.out.print("이메일 입력: ");
            String email = sc.nextLine();
            System.out.print("이미지 경로 (넣고싶지 않다면 엔터): ");
            String profilePath = sc.nextLine();
            if (!profilePath.isEmpty()) {
                byte[] profile = Files.readAllBytes(Path.of(profilePath));
                BinaryContent binaryContent = binaryContentService.save(new SaveBinaryContentParamDto(SubDirectory.PROFILE, profilePath, profile));
                profileId = binaryContent.getId();
            }
            userService.save(new SaveUserParamDto(username, password, nickname, email, profileId));
        } catch (IOException e) {
            System.out.println("[실패] 잘못된 경로의 파일");
            e.printStackTrace();
        }
    }

    private static UUID selectChannel(ChannelService channelService, UUID channelUUID) {
        FindChannelDto findChannelDto = channelService.findChannel(channelUUID);
        if (findChannelDto == null) return null;
        return findChannelDto.channelUUID();
    }

    private static void sendMessageByChannel(MessageService messageService, BinaryContentService binaryContentService,UUID channelUUID, UUID userUUID) {
        try {
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("------ 메세지 보내기 ------");
                System.out.println("1. 이미지 넣기\n2. 문자열 입력\n3. 채널 나가기");
                int commentType = sc.nextInt();
                sc.nextLine();

                String content;
                List<UUID> AttachIdList = new ArrayList<>();

                switch (commentType) {
                    case 1:
                        while (true) {
                            System.out.print("첨부할 파일 경로 입력 (종료: EXIT): ");
                            String filePath = sc.nextLine();
                            if (filePath.equalsIgnoreCase("EXIT")) break;
                            try {
                                byte[] file = Files.readAllBytes(Path.of(filePath));
                                BinaryContent binaryContent = binaryContentService.save(new SaveBinaryContentParamDto(SubDirectory.FILE, filePath, file));
                                AttachIdList.add(binaryContent.getId());
                            } catch (IOException e) {
                                System.out.println("파일 읽기 실패: " + e.getMessage());
                            }
                        }
                        System.out.print("전송할 내용 입력: ");
                        content = sc.nextLine();
                        break;
                    case 2:
                        System.out.print("전송할 내용 입력: ");
                        content = sc.nextLine();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("잘못된 입력");
                        continue;
                }
                SaveMessageParamDto saveMessageParamDto = new SaveMessageParamDto(channelUUID, userUUID, content, AttachIdList);
                messageService.sendMessage(saveMessageParamDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void searchByNum(UserService userService, ChannelService channelService, MessageService messageService, int searchNum) {
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
                        System.out.print("조회할 사용자 아이디: ");
                        UUID userUUID = UUID.fromString(sc.nextLine());
                        channelService.findAllByUserId(userUUID).forEach(System.out::println);
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

    private static void updateByNum(UserService userService, ChannelService channelService, MessageService messageService, BinaryContentService binaryContentService,int updateNum) {
        Scanner sc = new Scanner(System.in);
        switch (updateNum) {
            case 1:
                try {
                    System.out.print("변경할 사용자 아이디 입력: ");
                    UUID userUUID = UUID.fromString(sc.nextLine());
                    System.out.print("원하는 닉네임 입력: ");
                    String nickname = sc.nextLine();

                    UUID profileId = userService.findByUser(userUUID).profileId();
                    System.out.print("이미지 경로 (수정을 원하지 않을 시 엔터): ");
                    String profilePath = sc.nextLine();
                    if (!profilePath.isEmpty()) {
                        byte[] profile = Files.readAllBytes(Path.of(profilePath));
                        BinaryContent binaryContent = binaryContentService.save(new SaveBinaryContentParamDto(SubDirectory.PROFILE, profilePath, profile));
                        profileId = binaryContent.getId();
                    }

                    UpdateUserParamDto updateUserDto = new UpdateUserParamDto(userUUID, nickname, profileId);
                    userService.update(updateUserDto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            case 2:
                System.out.print("변경할 채널 아이디 입력: ");
                UUID channelUUID = UUID.fromString(sc.nextLine());
                System.out.print("원하는 채널명 입력: ");
                String channelName = sc.nextLine();
                ChannelUpdateParamDto channelUpdateParamDto = new ChannelUpdateParamDto(channelUUID, channelName);
                channelService.updateChannel(channelUpdateParamDto);
                return;
            case 3:
                System.out.print("수정할 메세지 아이디 입력: ");
                UUID messageUUID = UUID.fromString(sc.nextLine());
                System.out.print("수정 메시지 입력: ");
                String message = sc.nextLine();
                UpdateMessageParamDto updateMessageParamDto = new UpdateMessageParamDto(messageUUID, message);
                messageService.updateMessage(updateMessageParamDto);
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
