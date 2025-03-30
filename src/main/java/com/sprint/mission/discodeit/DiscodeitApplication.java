package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.controller.console.ConsoleMainMenuController;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
//        ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
//        UserService userService = context.getBean(UserService.class);
//        ChannelService channelService = context.getBean(ChannelService.class);
//        MessageService messageService = context.getBean(MessageService.class);
//        BasicAuthService basicAuthService = context.getBean(BasicAuthService.class);
//
//        Scanner scanner = new Scanner(System.in);

//        ConsoleMainMenuController menuController = new ConsoleMainMenuController(scanner,  userService, channelService, messageService, basicAuthService);
        SpringApplication.run(DiscodeitApplication.class, args);


    }

}
