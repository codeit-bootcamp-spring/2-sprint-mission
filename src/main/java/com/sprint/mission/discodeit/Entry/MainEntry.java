package com.sprint.mission.discodeit.Entry;

import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFContainerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.*;

public class MainEntry {
    static JCFUserService userService;
    static JCFServerService serverService;
    static JCFCategoryService categoryService;
    static JCFChannelService channelService;
    static JCFMessageService messageService;
    static JCFUserRepository userRepository;
    static JCFContainerRepository containerRepository;
    static JCFChannelRepository channelRepository;
    static JCFDiscordRepository discordRepository;

    public MainEntry() {
        userService = JCFUserService.getInstance();
        serverService = JCFServerService.getInstance();
        categoryService = JCFCategoryService.getInstance();
        channelService = JCFChannelService.getInstance();
        messageService = JCFMessageService.getInstance();

        userRepository = JCFUserRepository.getInstance();
        containerRepository = JCFContainerRepository.getInstance();
        channelRepository = JCFChannelRepository.getInstance();
        discordRepository = JCFDiscordRepository.getInstance();

    }

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

        User user = new User("U" + USERCOUNT, "U" + USERCOUNT);
        USERCOUNT++;
        headUser = user;
        discordRepository.add(user);

    }

    public static void createServer() {
        if (isUserNull())
            return;
        userRepository.add(headUser, userService.createServer("S" + SERVERCOUNT++));
    }

    public static void createChannel() {
        if (isUserNull())
            return;
        containerRepository.add(userRepository.getHead(), userService.createChannel("C" + CHANNELCOUNT++));
    }

    public static void removeUser() {
        if (isUserNull())
            return;
        discordRepository.remove();

    }

    public static void removeServer() {
        if (isUserNull())
            return;
        userRepository.remove(headUser);
    }

    public static void removeChannel() {
        if (isServerNull())
            return;
        containerRepository.remove(headserver);
    }


    public static void printUser() {
        if (isUserNull())
            return;
        discordRepository.print();
    }

    public static void printServer() {
        if (isUserNull())
            return;
        userRepository.print(headUser);
    }

    public static void printChannel() {
        if (isUserNull())
            return;
        containerRepository.print(headserver);
    }

    public static void writeMessage() {
        if (isContainerNull()) {
            return;
        }
        channelRepository.add(headContainer, channelService.write("M" + MESSAGECOUNT++));

    }

    public static void removeMessage() {
        if (isContainerNull()) {
            return;
        }
        channelRepository.remove(headContainer);
    }

    public static void printAllMessage() {
        if (isContainerNull()) {
            return;
        }
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
