package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.util.Collections;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    static Scanner sc = new Scanner(System.in);
    static UserService userService = new FileUserService();
    static ChannelService channelService = new FileChannelService();
    static MessageService messageService = JCFMessageService.getInstance(userService, channelService);

    public static void main(String[] args) {

        UUID userToken = null;
        UUID channelToken = null;

        while (true) {
            if (userToken == null) {
                System.out.println("\n ======== 메뉴 ======== ");
                System.out.println("1. 로그인\n2. 회원 가입");
                System.out.print("입력란: ");
                int initPage = sc.nextInt();
                sc.nextLine();
                switch (initPage) {
                    case 1:
                        userToken = loginPage();
                        break;
                    case 2:
                        joinPage();
                        break;
                }
            }
            if (userToken == null) continue;

            System.out.println(" ======== 메뉴 ======== ");
            System.out.println("1. 채널\n2. 조회\n3. 수정\n4. 삭제\n5. 로그아웃\n6. 나가기");
            System.out.print("입력란: ");

            int num = sc.nextInt();
            sc.nextLine();

            switch (num) {
                case 1:
                    System.out.println("===등록===");
                    System.out.println("1. 채널 개설\n2. 채널 선택\n3. 개설된 채널 조회\n4. 메뉴로 돌아가기");
                    System.out.print("입력란: ");
                    int createNum = sc.nextInt();
                    sc.nextLine();
                    switch (createNum) {
                        case 1:
                            createChannelPage();
                            break;
                        case 2:
                            channelToken = selectChannel();
                            if (channelToken == null) break;
                            messageService.findMessageByChannelId(channelToken)
                                    .orElse(Collections.emptyList())
                                    .forEach(message -> {
                                        System.out.println(userService.findByUser(message.getUserUUID()).getNickname());
                                        System.out.println(message.getContent());
                                    });

                            sendMessageByChannel(channelToken, userToken);
                            channelToken = null;
                            break;
                        case 3:
                            channelService.findAllChannel()
                                    .orElse(Collections.emptyList())
                                    .forEach(System.out::println);
                        case 4:
                            break;
                    }
                    break;

                case 2:
                    System.out.println("=== 조회 ===");
                    System.out.println("1. 사용자 조회\n2. 채널 조회\n3. 메세지 조회\n4. 메뉴로 돌아가기");
                    System.out.print("입력란: ");
                    int searchNum = sc.nextInt();
                    serchByNum(searchNum);
                    break;
                case 3:
                    System.out.println("=== 수정 ===");
                    System.out.println("1. 사용자 이름 수정\n2. 채널명 수정\n3. 메세지 수정\n4. 메뉴로 돌아가기");
                    System.out.print("입력란: ");
                    int updateNum = sc.nextInt();
                    sc.nextLine();
                    updateByNum(updateNum);
                    break;
                case 4:
                    System.out.println("=== 삭제 ===");
                    System.out.println("1. 사용자 삭제\n2. 채널 삭제\n3. 메시지 삭제\n4. 메뉴로 돌아가기");
                    System.out.print("입력란: ");
                    int deleteNum = sc.nextInt();
                    sc.nextLine();
                    deleteByNum(deleteNum);
                    break;
                case 5:
                    userToken = null;
                    break;
                default:
                    return;
            }
        }
    }

    private static UUID loginPage() {
        Scanner sc = new Scanner(System.in);
        System.out.print("유저 아이디 입력: ");
        UUID userUUID = UUID.fromString(sc.nextLine());
        System.out.print("비밀번호 입력: ");
        String password = sc.nextLine();
        UUID userToken = userService.login(userUUID, password);
        return userToken;
    }

    private static void joinPage() {
        System.out.print("닉네임 입력: ");
        String nickname = sc.nextLine();
        System.out.print("비밀번호 입력: ");
        String savePassword = sc.nextLine();
        userService.save(nickname, savePassword);
    }

    private static void createChannelPage() {
        System.out.print("채널명 입력: ");
        String channelName = sc.nextLine();
        channelService.createChannel(channelName);
    }

    private static UUID selectChannel() {
        System.out.print("채널 아이디 입력: ");
        UUID channelUUID = UUID.fromString(sc.nextLine());

        Channel channel = channelService.findChannel(channelUUID);
        if (channel == null) return null;
        return channel.getId();
    }

    private static void sendMessageByChannel(UUID channelUUID, UUID userUUID) {
        while (true) {
            System.out.println("------ 메세지 보내기 ------");
            String content = sc.nextLine();
            if (content.equalsIgnoreCase("EXIT")) return;
            messageService.sendMessage(channelUUID, userUUID, content);
        }
    }

    private static void serchByNum(int serchdNum) {
        switch (serchdNum) {
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
                        userService.findAllUser()
                                .orElse(Collections.emptyList())
                                .forEach(System.out::println);
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
                        channelService.findAllChannel()
                                .orElse(Collections.emptyList())
                                .forEach(System.out::println);
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
                        messageService.findMessageById(MessageUUID);
                        break;
                    case 2:
                        messageService.findAllMessages()
                                .orElse(Collections.emptyList())
                                .forEach(System.out::println);
                        break;
                    case 3:
                        System.out.print("조회할 채널 아이디: ");
                        UUID channelUUID = UUID.fromString(sc.nextLine());
                        messageService.findMessageByChannelId(channelUUID)
                                .orElse(Collections.emptyList())
                                .forEach(System.out::println);
                }
                break;
        }
    }

    private static void updateByNum(int updateNum) {
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
                return;
        }
    }

    private static void deleteByNum(int deleteNum) {
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
                return;
        }
    }
}
