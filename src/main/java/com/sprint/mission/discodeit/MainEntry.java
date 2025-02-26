package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.FrontEnd.DiscordRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFContainerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.List;
import java.util.Scanner;

public class MainEntry {
    //권한 부여
    JCFServerService serverService = JCFServerService.getInstance();

    JCFMessageService messageService = JCFMessageService.getInstance();

    public static int USERCOUNT = 0;
    public static int SERVERCOUNT = 0;
    public static int CHANNELCOUNT = 0;
    public static int MESSAGECOUNT = 0;

    private static User headUser = null;
    private static Server headserver = null;
    private static Container headContainer = null;

    public static boolean isUserNull() {
        if (headUser == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isServerNull() {
        if (headserver == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isContainerNull() {
        if (headContainer == null) {
            return true;
        } else {
            return false;
        }
    }

    public static void createUser() {
        DiscordRepository discordRepository = DiscordRepository.getInstance();

        headUser = new User("U" + USERCOUNT, "U" + USERCOUNT++);

        discordRepository.add(headUser);

    }

    public static void createServer() {
        JCFUserService userService = JCFUserService.getInstance();
        JCFUserRepository userRepository = JCFUserRepository.getInstance();

        if (isUserNull())
            return;

        headserver = userService.createServer("S" + SERVERCOUNT++);

        userRepository.add(headUser, headserver);
    }

    public static void createChannel() {
        if (isUserNull())
            return;
        JCFUserService userService = JCFUserService.getInstance();
        JCFUserRepository userRepository = JCFUserRepository.getInstance();
        JCFContainerRepository containerRepository = JCFContainerRepository.getInstance();

        headContainer = userService.createChannel("C" + CHANNELCOUNT++);
        containerRepository.add(headserver, headContainer);
    }

    public static void writeMessage() {
        if (isContainerNull()) {
            return;
        }
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFChannelRepository channelRepository = JCFChannelRepository.getInstance();

        channelRepository.add((Channel) headContainer, channelService.write("M" + MESSAGECOUNT++));

    }

    public static void removeUser() {
        if (isUserNull())
            return;
        DiscordRepository discordRepository = DiscordRepository.getInstance();
        discordRepository.remove();
    }

    public static void removeServer() {
        if (isUserNull()) {
            System.out.println("바라보고 있는 유저가 존재하지 않습니다.");
            return;
        }
        JCFUserRepository userRepository = JCFUserRepository.getInstance();
        List<Server> list = userRepository.repository(headUser);
        int i = list.indexOf(headserver);
        //로그
        printUserHead();
        userRepository.remove(headUser);
        //로그
        printUserHead();

    }

    public static void removeChannel() {
        if (isServerNull()) {
            System.out.println("바라보고 있는 서버가 존재하지 않습니다.");
            return;
        }
        JCFContainerRepository containerRepository = JCFContainerRepository.getInstance();

        containerRepository.remove(headserver);
    }

    public static void removeMessage() {
        if (isContainerNull()) {
            System.out.println("바라보고 있는 채널이 존재하지 않습니다.");
            return;
        }
        JCFChannelRepository channelRepository = JCFChannelRepository.getInstance();

        channelRepository.remove((Channel) headContainer);
    }

    public static void replaceUserHead() {
        DiscordRepository discordRepository = DiscordRepository.getInstance();
        List<User> list = discordRepository.repository();
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        printUserHead();
        System.out.print("바라볼 유저의 이름을 입력하시오. : ");
        String s = sc.next();
        for (User data : list) {
            if (data.getName().equals(s)) {
                headUser = data;
                System.out.println("변경된 유저 : " + data.getName());
                return;
            }
        }
        System.out.println("해당 유저가 존재하지 않습니다.");
    }

    public static void replaceServerHead() {
        JCFUserRepository userRepository = JCFUserRepository.getInstance();
        List<Server> list = userRepository.repository(headUser);
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        printServerHead();
        System.out.print("바라볼 서버의 이름을 입력하시오. : ");
        String s = sc.next();
        for (Server data : list) {
            if (data.getName().equals(s)) {
                headserver = data;
                System.out.println("변경된 서버 : " + data.getName());
                return;
            }
        }
        System.out.println("해당 서버가 존재하지 않습니다.");
    }

    public static void replaceChannelHead() {
        JCFContainerRepository containerRepository = JCFContainerRepository.getInstance();
        List<Container> list = containerRepository.repository(headserver);
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        printChannelHead();
        System.out.print("바라볼 채널의 이름을 입력하시오. : ");
        String s = sc.next();
        for (Container data : list) {
            if (data.getName().equals(s)) {
                headContainer = data;
                System.out.println("변경된 채널 : " + data.getName());
                return;
            }
        }
        System.out.println("해당 채널이 존재하지 않습니다.");
    }


    public static void printUser() {
        if (isUserNull()) {
            return;
        }
        DiscordRepository discordRepository = DiscordRepository.getInstance();
        discordRepository.print();
    }

    public static void printServer() {
        if (isServerNull()) {
            System.out.println("현재 서버에 아무것도 없습니다.");
            return;
        }
        JCFUserRepository userRepository = JCFUserRepository.getInstance();
        userRepository.print(headUser);
    }

    public static void printChannel() {
        if (isContainerNull()) {
            System.out.println("현재 채널에 아무것도 없습니다.");
            return;
        }
        JCFContainerRepository containerRepository = JCFContainerRepository.getInstance();

        containerRepository.print(headserver);
    }

    public static void printUserHead() {
        if (isUserNull()) {
            System.out.println("현재 유저의 헤드는 아무것도 바라보고 있지 않습니다.");
            return;
        }
        System.out.println("현재 바라보고 있는 유저 : " + headUser.getName());
    }

    public static void printServerHead() {
        if (isServerNull()) {
            System.out.println("현재 서버의 헤드는 아무것도 바라보고 있지 않습니다.");
            return;
        }
        System.out.println("현재 바라보고 있는 서버 : " + headserver.getName());
    }

    public static void printChannelHead() {
        if (isContainerNull()) {
            System.out.println("현재 채널의 헤드는 아무것도 바라보고 있지 않습니다.");
            return;
        }
        System.out.println("현재 바라보고 있는 채널 : " + headContainer.getName());
    }

    public static void printAllMessage() {
        if (isContainerNull()) {
            return;
        }
        JCFChannelRepository channelRepository = JCFChannelRepository.getInstance();

        channelRepository.print((Channel) headContainer);
    }

    public static void updateUser() {
        if (isUserNull())
            return;

    }

    public static void updateServer() {
        if (isUserNull())
            return;

    }

    public static void updateChannel() {
        if (isUserNull())
            return;

    }


}
