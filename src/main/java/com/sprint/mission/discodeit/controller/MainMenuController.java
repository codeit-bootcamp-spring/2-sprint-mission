package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.enums.MainMenu;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;

public class MainMenuController {
    private final Scanner scanner;
    private final UserMenuController userMenuController;
    private final ChannelMenuController channelMenuController;
    private final MassegeMenuController massegeMenuController;

    public MainMenuController(Scanner scanner, JCFUserService jcfUserService, JCFChannelService jcfChannelService, JCFMessageService jcfMessageService) {
        this.scanner = scanner;
        this.userMenuController = new UserMenuController(jcfUserService,scanner);
        this.channelMenuController = new ChannelMenuController(jcfChannelService, scanner);
        this.massegeMenuController = new MassegeMenuController(jcfUserService,jcfChannelService,jcfMessageService, scanner);
    }

    public void run(){
        boolean run = true;
        while (run) {
            for (MainMenu option : MainMenu.values()) {
                System.out.println(option.getCode() + ". " + option.getDescription());
            }
            System.out.print("선택: ");
            String choice = scanner.nextLine();
            MainMenu selectedMenu = MainMenu.getByCode(choice);

            if (selectedMenu == null) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }
            run = execute(selectedMenu);
        }
    }

    private boolean execute(MainMenu selectedMenu) {
        switch (selectedMenu) {
            case USER:
                userMenuController.handleUserMenu();
                return true;
            case CHANNEL:
                channelMenuController.handleChannelMenu();
                return true;
            case MESSAGE:
                massegeMenuController.handleMessageMenu();
                return true;
            case EXIT:
                return false;
            default:
                System.out.println("잘못된 입력입니다.");
                return true;
        }
    }

}
