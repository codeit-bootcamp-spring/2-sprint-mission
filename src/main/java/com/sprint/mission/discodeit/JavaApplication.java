package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.jcf.JCFMessageService;
import com.sprint.mission.discodeit.jcf.JCFUserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // 서비스 초기화
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== 채팅 시스템 메뉴 =====");
            System.out.println("1. 유저 생성");
            System.out.println("2. 채널 생성");
            System.out.println("3. 유저를 채널에 추가");
            System.out.println("4. 메시지 보내기");
            System.out.println("5. 특정 채널의 메시지 보기");
            System.out.println("6. 특정 메시지 삭제");
            System.out.println("7. 채널에서 유저 삭제");
            System.out.println("8. 모든 유저 및 채널 조회");
            System.out.println("9. 종료");
            System.out.print("메뉴를 선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 제거

            switch (choice) {
                case 1:
                    try {
                        System.out.print("생성할 유저 이름 입력: ");
                        String username = scanner.nextLine();
                        userService.createUser(username);
                        System.out.println("유저 생성 완료: " + username);
                    } catch (Exception   e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("생성할 채널 이름 입력: ");
                        String channelName = scanner.nextLine();
                        channelService.createChannel(channelName);
                        System.out.println("채널 생성 완료: " + channelName);
                    } catch (Exception   e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.print("채널 이름 입력: ");
                        String channelToJoin = scanner.nextLine();
                        System.out.print("추가할 유저 이름 입력: ");
                        String userToJoin = scanner.nextLine();
                        Channel channel = channelService.getChannel(channelToJoin);
                        if (channel != null) {
                            userService.validateUserExists(userToJoin);
                            channelService.addUserToChannel(channel, userToJoin);
                            System.out.println(userToJoin + "님이 " + channelToJoin + " 채널에 추가됨.");
                        } else {
                            System.out.println("채널이 존재하지 않습니다.");
                        }
                    } catch (Exception   e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        System.out.print("유저 이름 입력: ");
                        String sender = scanner.nextLine();
                        userService.validateUserExists(sender);

                        System.out.print("채널 이름 입력: ");
                        String targetChannel = scanner.nextLine();
                        channelService.validateChannelExists(targetChannel);

                        System.out.print("메시지 입력: ");
                        String messageContent = scanner.nextLine();
                        messageService.createMessage(sender, targetChannel, messageContent);
                        System.out.println("메시지 전송 완료.");
                    } catch (Exception   e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        System.out.print("메시지를 조회할 채널 이름 입력: ");
                        String channelToView = scanner.nextLine();
                        List<Message> messages = messageService.getChannelMessages(channelToView);
                        if (messages.isEmpty()) {
                            System.out.println("해당 채널에 메시지가 없습니다.");
                        } else {
                            System.out.println("\n===== 채널 메시지 (" + channelToView + ") =====");
                            for (Message msg : messages) {
                                System.out.println(msg.getSender() + ": " + msg.getContent() + " [" + msg.getUuid() + "]");
                            }
                        }
                    } catch (Exception   e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                    break;

                case 6:
                    try {
                        System.out.print("삭제할 메시지 ID 입력: ");
                        String messageIdInput = scanner.nextLine();
                        UUID messageId;
                        try {
                            messageId = UUID.fromString(messageIdInput);
                        } catch (Exception  e) {
                            System.out.println("오류 발생: 잘못된 UUID 형식입니다. 올바른 메시지 ID를 입력하세요.");
                            break;
                        }
                        messageService.deleteMessage(messageId);
                        System.out.println("메시지 삭제 완료.");
                    } catch (Exception   e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                    break;

                case 7:
                    try {
                        System.out.print("채널 이름 입력: ");
                        String channelToRemove = scanner.nextLine();

                        System.out.print("삭제할 유저 이름 입력: ");
                        String userToRemove = scanner.nextLine();


                        Channel channelForRemoval = channelService.getChannel(channelToRemove);
                        if (channelForRemoval != null) {
                            channelService.removeUserFromChannel(channelForRemoval, userToRemove);
                            System.out.println(userToRemove + "님이 " + channelToRemove + " 채널에서 삭제됨.");
                        } else {
                            System.out.println("해당 채널이 존재하지 않습니다.");
                        }
                    } catch (Exception   e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                    break;

                case 8:
                    try {
                        System.out.println("\n===== 모든 유저 및 채널 조회 =====");
                        System.out.println("유저 목록: " + userService.getAllUsers());
                        System.out.println("채널 목록: " + channelService.getAllChannels());
                    } catch (Exception   e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                    break;

                case 9:
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    return;

                default:
                    System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }
}
