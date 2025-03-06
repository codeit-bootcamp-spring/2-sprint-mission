package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new FileUserService();
        ChannelService channelService = new FileChannelService();
        MessageService messageService = new FileMessageService(userService);


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
                            User user = new User(userName1, userMail1);
                            userService.create(user);
                            break;

                        case "2":
                            System.out.println("\n===사용자 조회 ===");
                            System.out.print("사용자 이름 입력: ");
                            String userName2 = sc.nextLine();
                            userService.getUser(userName2);
                            User userPrint = userService.getUser(userName2);
                            System.out.println(userPrint);
                            break;

                        case "3":
                            System.out.println("\n===사용자 전체 조회 ===");
                            List<User> printUser1 = userService.getAllUser();
                            System.out.println();
                            System.out.println(printUser1);
                            break;

                        case "4":
                            System.out.println("\n===사용자 수정 ===");
                            System.out.print("변경 할 사용자 이름 입력: ");
                            String username3 = sc.nextLine();
                            System.out.print("새로운 사용자 이름 입력: ");
                            String changename = sc.nextLine();
                            System.out.print("새로운 사용자 이메일 입력: ");
                            String changeemail = sc.nextLine();
                            userService.update(username3, changename, changeemail);
                            break;

                        case "5":
                            System.out.println("\n===사용자 삭제 ===");
                            System.out.print("삭제 할 사용자 이름 입력: ");
                            String username4 = sc.nextLine();
                            userService.delete(username4);
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
                            Channel c = new Channel(channelName1, channelDesc);
                            channelService.create(c);
                            break;

                        case "2":
                            System.out.println("\n===채널 조회===");
                            System.out.print("채널 이름 입력: ");
                            String channelName2  =sc.nextLine();
                            Channel channelPrint = channelService.getChannel(channelName2);
                            System.out.println(channelPrint);
                            break;

                        case "3":
                            System.out.println("\n===채널 전체 조회===");
                            List<Channel> channelPrint2 = channelService.getAllChannel();
                            System.out.println();
                            System.out.println(channelPrint2);
                            break;

                        case "4":
                            System.out.println("\n===채널 수정===");
                            System.out.print("변경 할 채널 이름 입력: ");
                            String oldChannelName = sc.nextLine();
                            System.out.print("새로운 채널 이름 입력: ");
                            String newChannelName = sc.nextLine();
                            System.out.print("새로운 채널 설명 입력: ");
                            String newChannelDesc = sc.nextLine();
                            channelService.update(oldChannelName, newChannelName, newChannelDesc);
                            break;

                        case "5":
                            System.out.println("\n===채널 삭제===");
                            System.out.print("삭제할 채널 이름 입력: ");
                            String deleteChannelName = sc.nextLine();
                            channelService.delete(deleteChannelName);
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
                            System.out.print("보내는 사람 입력: ");
                            String sender1 = sc.nextLine();
                            System.out.print("보낼 메시지 내용 입력: ");
                            String sendMessage = sc.nextLine();
                            Message m1= new Message(sender1, sendMessage);
                            messageService.create(m1);
                            break;

                        case "2":
                            System.out.println("\n===메시지 조회===");
                            System.out.print("보낸 사람 입력: ");
                            String sender2  =sc.nextLine();
                            List<Message> messagePrint1 = messageService.getMessage(sender2);
                            if (messagePrint1.size() < 0) {
                                break;
                            } else{
                                System.out.println(messagePrint1);
                            }
                            break;

                        case "3":
                            System.out.println("\n===메시지 전체 조회===");
                            List<Message> messagePrint2 = messageService.getAllMessage();
                            System.out.println(messagePrint2);
                            break;

                        case "4":
                            System.out.println("\n===메시지 수정===");
                            System.out.print("보낸 사람 입력: ");
                            String sender3  =sc.nextLine();
                            System.out.print("메시지 UUID 입력: ");
                            String uuid  =sc.nextLine();
                            UUID uuid2 = UUID.fromString(uuid);
                            System.out.print("수정 할 메시지 입력: ");
                            String newMessage = sc.nextLine();
                            messageService.update(sender3, uuid2, newMessage);
                            System.out.println();
                            break;

                        case "5":
                            System.out.println("\n===메시지 삭제===");
                            System.out.print("보낸 사람 입력: ");
                            String sender4  =sc.nextLine();
                            System.out.print("메시지 UUID 입력: ");
                            String uuid3  =sc.nextLine();
                            UUID uuid4 = UUID.fromString(uuid3);
                            messageService.delete(sender4, uuid4);
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
