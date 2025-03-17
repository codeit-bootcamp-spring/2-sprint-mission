package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.controller.MainMenuController;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;


import java.util.Map;
import java.util.Scanner;

public class JavaApplication {
    public static void main(String[] args) {
        UserRepository userRepository = new FileUserRepository();
        UserService userService = new BasicUserService(userRepository);
        ChannelRepository channelRepository = new FileChannelRepository();
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageRepository messageRepository = new FileMessageRepository();
        MessageService messageService = new BasicMessageService(messageRepository);

        Scanner scanner = new Scanner(System.in);

        MainMenuController menuController = new MainMenuController(scanner,  userService, channelService, messageService);
        menuController.run();


    }
}
