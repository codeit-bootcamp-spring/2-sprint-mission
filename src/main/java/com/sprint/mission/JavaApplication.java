package com.sprint.mission;

import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.jcf.JCFMessageService;
import com.sprint.mission.discodeit.jcf.JCFUserService;
import com.sprint.mission.menu.ChannelMenu;
import com.sprint.mission.menu.MessageMenu;
import com.sprint.mission.menu.UserMenu;

import java.util.Scanner;

public class JavaApplication {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        JCFUserService userService = JCFUserService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance();

        userService.initializeServices(channelService);
        channelService.initializeServices(messageService, userService);
        messageService.initializeServices(channelService, userService);

        while(true){
            System.out.println("=============================");
            System.out.println("1. 유저");
            System.out.println("2. 채널");
            System.out.println("3. 메시지");
            System.out.println("4. 나가기");
            System.out.println("=============================");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1:
                    UserMenu.display(userService);
                    break;
                case 2:
                    ChannelMenu.display(channelService);
                    break;
                case 3:
                    MessageMenu.display(messageService);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }

        }
    }
}
