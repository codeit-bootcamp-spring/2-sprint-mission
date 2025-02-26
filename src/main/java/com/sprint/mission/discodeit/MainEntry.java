package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.FrontEnd.DiscordRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFContainerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.*;

public class MainEntry {
    //카테고리와 채널 관리
    JCFCategoryService categoryService = JCFCategoryService.getInstance();

    //권한 부여
    JCFServerService serverService = JCFServerService.getInstance();

    JCFMessageService messageService = JCFMessageService.getInstance();

    public static int USERCOUNT = 0;
    public static int SERVERCOUNT = 0;
    public static int CHANNELCOUNT = 0;
    public static int MESSAGECOUNT = 0;

    private static User headUser = null;
    private static Server headserver = null;
    private static Channel headContainer = null;

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
        USERCOUNT++;
        headUser = new User("U" + USERCOUNT, "U" + USERCOUNT);;
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
        containerRepository.add(userRepository.getHead(), headContainer);
    }

    public static void removeUser() {

        if (isUserNull())
            return;
        DiscordRepository discordRepository = DiscordRepository.getInstance();
        discordRepository.remove();

    }

    public static void removeServer() {

        if (isUserNull()) {
            return;
        }
        JCFUserRepository userRepository = JCFUserRepository.getInstance();
        userRepository.remove(headUser);
    }

    public static void removeChannel() {
        if (isServerNull())
            return;
        JCFContainerRepository containerRepository = JCFContainerRepository.getInstance();

        containerRepository.remove(headserver);
    }


    public static void printUser() {
        DiscordRepository discordRepository = DiscordRepository.getInstance();

        if (isUserNull())
            return;
        discordRepository.print();
    }

    public static void printServer() {
        if (isUserNull())
            return;
        JCFUserRepository userRepository = JCFUserRepository.getInstance();
        userRepository.print(headUser);
    }

    public static void printChannel() {
        if (isUserNull())
            return;
        JCFContainerRepository containerRepository = JCFContainerRepository.getInstance();

        containerRepository.print(headserver);
    }

    public static void writeMessage() {
        if (isContainerNull()) {
            return;
        }
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFChannelRepository channelRepository = JCFChannelRepository.getInstance();

        channelRepository.add(headContainer, channelService.write("M" + MESSAGECOUNT++));

    }

    public static void removeMessage() {
        if (isContainerNull()) {
            return;
        }
        JCFChannelRepository channelRepository = JCFChannelRepository.getInstance();

        channelRepository.remove(headContainer);
    }

    public static void printAllMessage() {
        if (isContainerNull()) {
            return;
        }
        JCFChannelRepository channelRepository = JCFChannelRepository.getInstance();

        channelRepository.print(headContainer);
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
