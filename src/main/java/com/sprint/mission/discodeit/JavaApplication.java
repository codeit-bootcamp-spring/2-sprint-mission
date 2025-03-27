package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.controller.MainMenuController;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.yaml.snakeyaml.Yaml;


import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

public class JavaApplication {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        InputStream inputStream = JavaApplication.class
                .getClassLoader()
                .getResourceAsStream("application.yaml");

        Map<String, Object> yamlMap = yaml.load(inputStream);
        Map<String, Object> discodeit = (Map<String, Object>) yamlMap.get("discodeit");
        Map<String, String> fileConfig = (Map<String, String>) ((Map<String, Object>) discodeit.get("repository")).get("file");

        String userFile = fileConfig.get("user");
        String channelFile = fileConfig.get("channel");
        String messageFile = fileConfig.get("message");
        String binaryContentFile = fileConfig.get("binaryContent");
        String readStatusFile = fileConfig.get("readStatus");
        String userStatusFile = fileConfig.get("userStatus");

        SaveLoadHandler saveLoadHandler = new SaveLoadHandler<>();

        // 4. 리포지토리 생성자에 파일명 주입
        UserRepository userRepository = new FileUserRepository(userFile, saveLoadHandler);
        BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository(binaryContentFile, saveLoadHandler);
        UserStatusRepository userStatusRepository = new FileUserStatusRepository(userStatusFile, saveLoadHandler);
        UserService userService = new BasicUserService(userRepository, binaryContentRepository, userStatusRepository);

        ChannelRepository channelRepository = new FileChannelRepository(channelFile, saveLoadHandler);
        ReadStatusRepository readStatusRepository = new FileReadStatusRepository(readStatusFile, saveLoadHandler);
        MessageRepository messageRepository = new FileMessageRepository(messageFile, saveLoadHandler);

        ChannelService channelService = new BasicChannelService(channelRepository, readStatusRepository, messageRepository);
        MessageService messageService = new BasicMessageService(messageRepository);
        BasicAuthService basicAuthService = new BasicAuthService(userRepository);

        Scanner scanner = new Scanner(System.in);
        MainMenuController menuController = new MainMenuController(scanner, userService, channelService, messageService, basicAuthService);
        menuController.run();


    }
}
