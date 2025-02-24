package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        Scanner sc = new Scanner(System.in);

        boolean run = true;
        while (run) {
            System.out.println("\n=== 메뉴 ===");
            System.out.println("1. 사용자");
            System.out.println("2. 채널");
            System.out.println("3. 메시지[구현중]");
            System.out.println("4. 종료");
            System.out.print("번호 입력: ");

            String choice_1 = sc.nextLine();


            switch (choice_1) {
                case "1":
                    System.out.println("\n===사용자 메뉴 ===");
                    System.out.println("1. 사용자 등록");
                    System.out.println("2. 사용자 조회");
                    System.out.println("3. 사용자 전체 조회");
                    System.out.println("4. 사용자 수정");
                    System.out.println("5. 사용자 삭제");
                    System.out.print("번호 입력: ");
                    String choice_1_1 = sc.nextLine();

                    switch (choice_1_1) {
                        case "1":
                            System.out.println("\n===사용자 등록 ===");
                            System.out.print("사용자 이름 입력: ");
                            String username1 = sc.nextLine();
                            System.out.print("사용자 이메일 입력: ");
                            String usermail1 = sc.nextLine();
                            User u = new User(username1, usermail1);
                            userService.createUser(u);
                            System.out.println("[사용자 등록이 완료되었습니다] ");
                            break;

                        case "2":
                            System.out.println("\n===사용자 조회 ===");
                            System.out.print("사용자 이름 입력: ");
                            String username2 = sc.nextLine();
                            userService.foundUser(username2);
                            break;

                        case "3":
                            System.out.println("\n===사용자 전체 조회 ===");
                            userService.getAllUsers();
                            break;

                        case "4":
                            System.out.println("\n===사용자 수정 ===");
                            System.out.print("변경 할 사용자 이름 입력: ");
                            String username3 = sc.nextLine();
                            System.out.print("새로운 사용자 이름 입력: ");
                            String changename = sc.nextLine();
                            System.out.print("새로운 사용자 이메일 입력: ");
                            String changeemail = sc.nextLine();
                            userService.updateUser(username3, changename, changeemail);
                            break;

                        case "5":
                            System.out.println("\n===사용자 삭제 ===");
                            System.out.print("삭제 할 사용자 이름 입력: ");
                            String username4 = sc.nextLine();
                            userService.deleteUser(username4);
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
                    String choice_1_2 = sc.nextLine();

                    switch (choice_1_2) {
                        case "1":
                            System.out.println("채널 생성");
                            System.out.print("채널 이름 입력: ");
                            String channelName1  =sc.nextLine();
                            System.out.print("채널 설명 입력: ");
                            String channelDesc  =sc.nextLine();
                            Channel c = new Channel(channelName1, channelDesc);
                            channelService.createChannel(c);
                            System.out.println("[채널 생성이 완료되었습니다] ");
                            break;

                        case "2":
                            System.out.println("채널 조회");
                            System.out.print("채널 이름 입력: ");
                            String channelName2  =sc.nextLine();
                            channelService.findChannel(channelName2);
                            System.out.println("[채널 조회가 완료되었습니다] ");
                            break;

                        case "3":
                            System.out.println("채널 전체 조회");
                            channelService.getAllChannels();
                            System.out.println("[채널 전체 조회가 완료되었습니다.]");
                            break;

                        case "4":
                            System.out.println("채널 수정");
                            System.out.print("변경 할 채널 이름 입력: ");
                            String oldChannelName = sc.nextLine();
                            System.out.print("새로운 채널 이름 입력: ");
                            String newChannelName = sc.nextLine();
                            System.out.print("새로운 채널 설명 입력: ");
                            String newChannelDesc = sc.nextLine();
                            channelService.updateChannel(oldChannelName, newChannelName, newChannelDesc);
                            System.out.println("[채널 수정이 완료되었습니다.]");
                            break;

                        case "5":
                            System.out.println("채널 삭제");
                            System.out.print("삭제할 채널 이름 입력: ");
                            String deleteChannelName = sc.nextLine();
                            channelService.deleteChannel(deleteChannelName);
                            System.out.println("[채널 삭제가 완료되었습니다.]");
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
                    String choice_1_3 = sc.nextLine();

                    switch (choice_1_3) {
                        case "1":
                            System.out.println("메시지 생성");
                            System.out.print("보내는 사람 입력: ");
                            String sender1 = sc.nextLine();
                            System.out.print("보낼 메시지 내용 입력: ");
                            String sendMessage = sc.nextLine();
                            Message m1= new Message(sender1, sendMessage);
                            messageService.createMessage(m1);
                            System.out.println("[메시지 전송이 완료되었습니다.]");
                            break;

                        case "2":
                            System.out.println("메시지 조회");
                            System.out.print("보낸 사람 입력: ");
                            String sender2  =sc.nextLine();
                            Message m2 = new Message(sender2);
                            messageService.findMessage(m2);
                            System.out.println("[채널 조회가 완료되었습니다] ");
                            break;

                        case "3":
                            System.out.println("메시지 전체 조회");
                            messageService.getAllMessage();
                            System.out.println("[메시지 조회가 완료되었습니다] ");
                            break;

                        case "4":
                            System.out.println("메시지 수정");
                            System.out.print("보낸 사람 입력: ");
                            String sender3  =sc.nextLine();
                            System.out.print("수정 할 메시지 입력: ");
                            String newMessage = sc.nextLine();
                            messageService.updateMessage(sender3, newMessage);
                            System.out.println("[메시지 수정이 완료되었습니다] ");
                            break;

                        case "5":
                            System.out.println("메시지 삭제");
                            System.out.print("보낸 사람 입력: ");
                            String sender4  =sc.nextLine();
                            messageService.deleteMessage(sender4);
                            System.out.println("[메시지 삭제가 완료되었습니다] ");
                            break;
                    }
                    continue;

                case "4":
                    run = false;
                    System.out.println("[Info] 종료합니다.");
                    break;

                default:
                    System.out.println("[Error] 잘못된 입력입니다.");

            }
        }
        sc.close();
    }
}
