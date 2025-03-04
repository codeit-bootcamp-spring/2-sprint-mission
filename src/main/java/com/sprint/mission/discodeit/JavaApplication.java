package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        JCFUserService jcfUserService = JCFUserService.getInstance();
        JCFChannelService jcfChannelService = JCFChannelService.getInstance();
        JCFMessageService jcfMessageService = JCFMessageService.getInstance(jcfUserService, jcfChannelService);

        UUID userToken = null;
        UUID channelToken = null;

        boolean run = true;

        while (run) {
            if (userToken == null) {
                System.out.println("\n ======== 메뉴 ======== ");
                System.out.println("1. 로그인\n2. 회원 가입");
                System.out.print("입력란: ");
                int initPage = sc.nextInt();
                sc.nextLine();
                switch (initPage) {
                    case 1:
                        userToken = loginPage(jcfUserService);
                        break;
                    case 2:
                        joinPage(jcfUserService);
                        break;
                }
            }
            if (userToken == null) continue;

            System.out.println(" ======== 메뉴 ======== ");
            System.out.println("1. 채널\n2. 조회(다건, 단건)\n3. 수정 \n4. 삭제 \n5. 로그아웃\n6. 나가기");
            System.out.print("입력란: ");

            int num = sc.nextInt();
            sc.nextLine();

            switch (num) {
                case 1:
                    System.out.println("===등록===");
                    System.out.println("1. 채널 개설\n2. 채널 선택\n3. 메뉴로 돌아가기");
                    System.out.print("입력란: ");
                    int createNum = sc.nextInt();
                    sc.nextLine();
                    switch (createNum) {
                        case 1:
                            createChannelPage(jcfChannelService);
                            break;
                        case 2:
                            channelToken = selectChannel(jcfChannelService);
                            if (channelToken == null) break;
                            jcfMessageService.findMessageByChannelId(channelToken);

                            sendMessageByChannel(jcfMessageService, channelToken, userToken);
                        case 3:
                            break;
                    }
                    break;

                case 2:
                    System.out.println("=== 조회 ===");
                    System.out.println("1. 사용자 조회\n2. 채널 조회\n3. 메세지 조회\n4. 메뉴로 돌아가기");
                    System.out.print("입력란: ");
                    int findNum = sc.nextInt();
                    switch (findNum) {
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
                                    System.out.println(jcfUserService.findByUser(UserUUID));
                                    break;
                                case 2:
                                    jcfUserService.findAll();
                                    break;
                            }
                            break;
                        case 2:
                            System.out.println("=== 조회 방법 ===");
                            System.out.println("1. 단건 조회\n2. 다건 조회\n3. 채널별 조회");
                            System.out.print("입력란: ");
                            int findNum2 = sc.nextInt();
                            sc.nextLine();
                            switch (findNum2) {
                                case 1:
                                    System.out.print("조회할 채널 아이디: ");
                                    UUID chennelUUID = UUID.fromString(sc.nextLine());

                                    System.out.println(jcfChannelService.findChannel(chennelUUID));

                                    break;
                                case 2:
                                    jcfChannelService.findChannelAll();
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
                                    jcfMessageService.findMessageById(MessageUUID);
                                    break;
                                case 2:
                                    jcfMessageService.findAllMessages();
                                    break;
                                case 3:
                                    System.out.print("조회할 채널 아이디: ");
                                    UUID channelUUID = UUID.fromString(sc.nextLine());
                                    jcfMessageService.findMessageByChannelId(channelUUID);
                            }
                            break;
                    }
                    break;
                case 3:
                    System.out.println("=== 수정 ===");
                    System.out.println("1. 사용자 이름 수정\n2. 채널명 수정\n3. 메세지 수정\n4. 메뉴로 돌아가기");
                    System.out.print("입력란: ");
                    int updateNum = sc.nextInt();
                    sc.nextLine();
                    switch (updateNum) {
                        case 1:
                            System.out.print("변경할 사용자 아이디 입력: ");
                            UUID userUUID = UUID.fromString(sc.nextLine());
                            System.out.print("원하는 닉네임 입력: ");
                            String nickname = sc.nextLine();
                            jcfUserService.update(userUUID, nickname);
                            break;
                        case 2:
                            System.out.print("변경할 채널 아이디 입력: ");
                            UUID channelUUID = UUID.fromString(sc.nextLine());
                            System.out.print("원하는 채널명 입력: ");
                            String channelName = sc.nextLine();
                            jcfChannelService.updateChannel(channelUUID, channelName);
                            break;
                        case 3:
                            System.out.print("수정할 메세지 아이디 입력: ");
                            UUID messageUUID = UUID.fromString(sc.nextLine());
                            System.out.print("수정 메시지 입력: ");
                            String message = sc.nextLine();
                            jcfMessageService.updateMessage(messageUUID, message);
                            break;
                        case 4:
                            break;
                    }
                    break;
                case 4:
                    System.out.println("=== 삭제 ===");
                    System.out.println("1. 사용자 삭제\n2. 채널 삭제\n3. 메시지 삭제\n4. 메뉴로 돌아가기");
                    System.out.print("입력란: ");
                    int deleteNum = sc.nextInt();
                    sc.nextLine();
                    switch (deleteNum) {
                        case 1:
                            System.out.print("삭제할 사용자 아이디: ");
                            UUID userUUID = UUID.fromString(sc.nextLine());
                            jcfUserService.delete(userUUID);
                            break;
                        case 2:
                            System.out.print("삭제할 채널 아이디: ");
                            UUID channelUUID = UUID.fromString(sc.nextLine());
                            jcfChannelService.deleteChannel(channelUUID);
                            break;
                        case 3:
                            System.out.print("삭제할 메시지 아이디: ");
                            UUID messageUUID = UUID.fromString(sc.nextLine());
                            jcfMessageService.deleteMessageById(messageUUID);
                            break;
                        case 4:
                            break;
                    }
                    break;
                case 5:
                    userToken = null;
                    break;
                case 6:
                    run = false;
            }
        }
    }

    private static UUID loginPage(JCFUserService jcfUserService) {
        Scanner sc = new Scanner(System.in);
        System.out.print("유저 아이디 입력: ");
        UUID userUUID = UUID.fromString(sc.nextLine());
        System.out.print("비밀번호 입력: ");
        String password = sc.nextLine();
        UUID userToken = jcfUserService.login(userUUID, password);
        return userToken;
    }

    private static void joinPage(JCFUserService jcfUserService) {
        System.out.print("닉네임 입력: ");
        String nickname = sc.nextLine();
        System.out.print("비밀번호 입력: ");
        String savePassword = sc.nextLine();
        jcfUserService.save(nickname, savePassword);
    }

    private static void createChannelPage(JCFChannelService jcfChannelService) {
        System.out.print("채널명 입력: ");
        String channelName = sc.nextLine();
        jcfChannelService.createChannel(channelName);
    }

    private static UUID selectChannel(JCFChannelService jcfChannelService) {
        System.out.print("채널 아이디 입력: ");
        UUID channelUUID = UUID.fromString(sc.nextLine());

        Channel channel = jcfChannelService.findChannel(channelUUID);
        if (channel == null) return null;
        return channel.getId();
    }

    private static void sendMessageByChannel(JCFMessageService jcfMessageService, UUID channelUUID, UUID userUUID) {
        while (true) {
            System.out.println("------ 메세지 보내기 ------");
            String content = sc.nextLine();
            if(content.equalsIgnoreCase("EXIT")) return;
            jcfMessageService.sendMessage(channelUUID, userUUID, content);
        }
    }
}
