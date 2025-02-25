package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.jcf.JCFMessageService;
import com.sprint.mission.discodeit.jcf.JCFUserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ChatSystemMenu {

    // 메인 메뉴 출력 (JavaApplication의 MainMenuOption enum 사용)
    public static void printMainMenu() {
        System.out.println("\n===== 채팅 시스템 메뉴 =====");
        for (JavaApplication.MainMenuOption option : JavaApplication.MainMenuOption.values()) {
            System.out.println(option.getCode() + ". " + option.getDescription());
        }
        System.out.print("메뉴를 선택하세요: ");
    }

    // 1번 메뉴: 생성 서브 메뉴
    public static void processCreateMenu(Scanner scanner, JCFUserService userService,
                                         JCFChannelService channelService, JCFMessageService messageService) {
        System.out.println("\n--- 생성 메뉴 ---");
        System.out.println("1. 유저 생성");
        System.out.println("2. 채널 생성");
        System.out.println("3. 채널에 유저 추가");
        System.out.println("4. 메시지 전송");
        System.out.print("선택: ");
        int createChoice = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 제거

        switch (createChoice) {
            case 1:
                try {
                    System.out.print("생성할 유저 이름 입력: ");
                    String username = scanner.nextLine();
                    userService.createUser(username);
                    System.out.println("유저 생성 완료: " + username);
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.print("생성할 채널 이름 입력: ");
                    String channelName = scanner.nextLine();
                    channelService.createChannel(channelName);
                    System.out.println("채널 생성 완료: " + channelName);
                } catch (Exception e) {
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
                    channelService.addUserToChannel(channel, userToJoin);
                    System.out.println(userToJoin + "님이 " + channelToJoin + " 채널에 추가되었습니다.");
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            case 4:
                try {
                    System.out.print("유저 이름 입력: ");
                    String sender = scanner.nextLine();
                    System.out.print("채널 이름 입력: ");
                    String targetChannel = scanner.nextLine();
                    System.out.print("메시지 입력: ");
                    String messageContent = scanner.nextLine();
                    messageService.createMessage(sender, targetChannel, messageContent);
                    System.out.println("메시지 전송 완료.");
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }

    // 2번 메뉴: 단건 정보 조회 서브 메뉴
    public static void processSingleLookupMenu(Scanner scanner, JCFUserService userService,
                                               JCFChannelService channelService, JCFMessageService messageService) {
        System.out.println("\n--- 단건 정보 조회 메뉴 ---");
        System.out.println("1. 유저 정보 조회");
        System.out.println("2. 채널 정보 조회");
        System.out.println("3. 메시지 정보 조회");
        System.out.print("선택: ");
        int lookupChoice = scanner.nextInt();
        scanner.nextLine();

        switch (lookupChoice) {
            case 1:
                try {
                    System.out.print("조회할 유저 이름 입력: ");
                    String username = scanner.nextLine();
                    System.out.println("유저 정보: " + userService.getUser(username));
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.print("조회할 채널 이름 입력: ");
                    String channelName = scanner.nextLine();
                    System.out.println("채널 정보: " + channelService.getChannel(channelName));
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            case 3:
                try {
                    System.out.print("조회할 메시지 ID 입력: ");
                    String messageIdInput = scanner.nextLine();
                    UUID messageId = UUID.fromString(messageIdInput);
                    System.out.println("메시지 정보: " + messageService.getMessageById(messageId));
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }

    // 3번 메뉴: 다건 정보 조회 서브 메뉴
    public static void processMultipleLookupMenu(Scanner scanner, JCFUserService userService,
                                                 JCFChannelService channelService, JCFMessageService messageService) {
        System.out.println("\n--- 다건 정보 조회 메뉴 ---");
        System.out.println("1. 채널의 모든 메시지 조회");
        System.out.print("선택: ");
        int multipleChoice = scanner.nextInt();
        scanner.nextLine();

        if (multipleChoice == 1) {
            try {
                System.out.print("메시지를 조회할 채널 이름 입력: ");
                String channelToView = scanner.nextLine();
                List<Message> messages = messageService.getChannelMessages(channelToView);
                if (messages.isEmpty()) {
                    System.out.println("해당 채널에 메시지가 없습니다.");
                } else {
                    System.out.println("\n===== 채널 메시지 (" + channelToView + ") =====");
                    for (Message msg : messages) {
                        System.out.println(msg.getSender() + ": " + msg.getContent() + " [" + msg.getId() + "]");
                    }
                }
            } catch (Exception e) {
                System.out.println("오류 발생: " + e.getMessage());
            }
        } else {
            System.out.println("잘못된 선택입니다.");
        }
    }

    // 4번 메뉴: 정보 수정 서브 메뉴
    public static void processUpdateMenu(Scanner scanner, JCFUserService userService,
                                         JCFChannelService channelService, JCFMessageService messageService) {
        System.out.println("\n--- 정보 수정 메뉴 ---");
        System.out.println("1. 유저 이름 수정");
        System.out.println("2. 채널 이름 수정");
        System.out.println("3. 메시지 내용 수정");
        System.out.print("선택: ");
        int updateChoice = scanner.nextInt();
        scanner.nextLine();

        switch (updateChoice) {
            case 1:
                try {
                    System.out.print("수정할 유저 이름 입력: ");
                    String oldUsername = scanner.nextLine();
                    System.out.print("새로운 유저 이름 입력: ");
                    String newUsername = scanner.nextLine();
                    userService.updateUser(oldUsername, newUsername);
                    System.out.println("유저 이름이 수정되었습니다.");
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.print("수정할 채널 이름 입력: ");
                    String oldChannelName = scanner.nextLine();
                    System.out.print("새로운 채널 이름 입력: ");
                    String newChannelName = scanner.nextLine();
                    channelService.updateChannel(oldChannelName, newChannelName);
                    System.out.println("채널 이름이 수정되었습니다.");
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            case 3:
                try {
                    System.out.print("수정할 메시지 ID 입력: ");
                    String messageIdInput = scanner.nextLine();
                    UUID messageId = UUID.fromString(messageIdInput);
                    System.out.print("새로운 메시지 내용 입력: ");
                    String newMessageContent = scanner.nextLine();
                    messageService.updateMessage(messageId, newMessageContent);
                    System.out.println("메시지 내용이 수정되었습니다.");
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }

    // 5번 메뉴: 데이터 삭제 서브 메뉴
    public static void processDeleteMenu(Scanner scanner, JCFUserService userService,
                                         JCFChannelService channelService, JCFMessageService messageService) {
        System.out.println("\n--- 데이터 삭제 메뉴 ---");
        System.out.println("1. 특정 메시지 삭제");
        System.out.println("2. 채널에서 유저 삭제");
        System.out.print("선택: ");
        int deleteChoice = scanner.nextInt();
        scanner.nextLine();

        switch (deleteChoice) {
            case 1:
                try {
                    System.out.print("삭제할 메시지 ID 입력: ");
                    String messageIdInput = scanner.nextLine();
                    UUID messageId = UUID.fromString(messageIdInput);
                    messageService.deleteMessage(messageId);
                    System.out.println("메시지 삭제 완료.");
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.print("채널 이름 입력: ");
                    String channelToRemove = scanner.nextLine();
                    System.out.print("삭제할 유저 이름 입력: ");
                    String userToRemove = scanner.nextLine();
                    Channel channelForRemoval = channelService.getChannel(channelToRemove);
                    channelService.removeUserFromChannel(channelForRemoval, userToRemove);
                    System.out.println(userToRemove + "님이 " + channelToRemove + " 채널에서 삭제되었습니다.");
                } catch (Exception e) {
                    System.out.println("오류 발생: " + e.getMessage());
                }
                break;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }

    // 6번 메뉴: 모든 유저 및 채널 조회
    public static void processAllLookup(Scanner scanner, JCFUserService userService,
                                        JCFChannelService channelService, JCFMessageService messageService) {
        System.out.println("\n===== 모든 유저 및 채널 조회 =====");
        try {
            System.out.println("유저 목록: " + userService.getAllUsers());
            System.out.println("채널 목록: " + channelService.getAllChannels());
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }
}
