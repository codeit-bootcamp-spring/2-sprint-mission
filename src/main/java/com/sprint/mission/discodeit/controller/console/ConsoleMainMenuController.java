package com.sprint.mission.discodeit.controller.console;

import com.sprint.mission.discodeit.menus.MainMenu;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Scanner;

public class ConsoleMainMenuController {
    private final Scanner scanner;
    private final ConsoleUserMenuController consoleUserMenuController;
    private final ConsoleChannelMenuController consoleChannelMenuController;
    private final ConsoleMessageMenuController consoleMessageMenuController;

    public ConsoleMainMenuController(Scanner scanner, UserService userService, ChannelService channelService, MessageService messageService, BasicAuthService basicAuthService) {
        this.scanner = scanner;
        this.consoleUserMenuController = new ConsoleUserMenuController(userService,scanner);
        this.consoleChannelMenuController = new ConsoleChannelMenuController(channelService, userService, scanner);
        this.consoleMessageMenuController = new ConsoleMessageMenuController(userService, channelService, messageService, basicAuthService,scanner);
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
                consoleUserMenuController.handleUserMenu();
                return true;
            case CHANNEL:
                consoleChannelMenuController.handleChannelMenu();
                return true;
            case MESSAGE:
                consoleMessageMenuController.handleMessageMenu();
                return true;
            case EXIT:
                return false;
            default:
                System.out.println("잘못된 입력입니다.");
                return true;
        }
    }

}
