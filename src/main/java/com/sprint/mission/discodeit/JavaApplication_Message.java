package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;
import java.util.UUID;

/*
1. 사용자 등록
2. 채널 생성
3. 메시지 보내기
4. 모든 메시지 보기
0. 종료
번호 선택.
 */

public class JavaApplication_Message {
    public static void main(String[] args) {
        JCFUserService jcfUserService = new JCFUserService();
        UserService UserService = new JCFUserService();
        ChannelService ChannelService = new JCFChannelService();
        JCFMessageService jcfMessageService = new JCFMessageService(UserService, ChannelService);
        JCFChannelService jcfChannelService = new JCFChannelService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("💬***** 테무 디스코드 *****💬");
                System.out.println("1. 사용자 등록");
                System.out.println("2. 채널 생성");
                System.out.println("3. 메시지 보내기");
                System.out.println("4. 모든 메시지 보기");
                System.out.println("0. 모든 메시지 보기");
                System.out.print("원하시는 메뉴의 숫자를 입력하세요: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> {
                        System.out.print("사용자 이름 입력: ");
                        String username = scanner.nextLine().trim();
                        if (username.isEmpty()) {
                            System.out.println("⚠️이름을 입력해야 합니다.");
                            continue;
                        }
                        User newUser = new User(username);
                        jcfUserService.create(newUser);
                        System.out.println("등록 완료! 이름: " + newUser.getUserName() + " ID: " + newUser.getId());
                    }
                    case 2 -> {
                        System.out.print("원하는 채널 이름을 입력: ");
                        String channelName = scanner.nextLine().trim();
                        Channel channel = new Channel(channelName);
                        jcfChannelService.create(channel);
                        System.out.println("✅채널을 생성하였습니다. 채널 이름은: " + channel.getName() + "입니다." + " ID: " + channel.getId());
                    }
                    case 3 -> {
                        System.out.print("보낼 사용자 ID 입력: ");
                        UUID userID = UUID.fromString(scanner.nextLine().trim());

                        System.out.print("채널 ID 입력: ");
                        UUID channelID = UUID.fromString(scanner.nextLine().trim());

                        System.out.print("메시지 내용 입력: ");
                        String content = scanner.nextLine().trim();

                        Message message = new Message(content, userID, channelID);
                        jcfMessageService.create(message);
                        System.out.println("메시지가 성공적으로 전송되었습니다!");
                        System.out.println("메시지 내용 : " + message.getContent());
                    }
                    case 4 -> {
                        jcfMessageService.readAll().forEach(message -> System.out.println("[" + message.getChannelId() + "] "
                                + message.getUserId() + ": " + message.getContent()));
                    }
                    case 0 -> {
                        System.out.println("프로그램 종료.");
                        return;
                    }
                    default -> System.out.println("올바른 번호를 입력하세요");
                }

            } catch (Exception e) {
                System.out.println("😱입력 오류 발생. 다시 시도하세요.");
            }
        }
    }
}
