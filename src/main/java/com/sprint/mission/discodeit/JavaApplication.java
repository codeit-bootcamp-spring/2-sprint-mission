package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.menu.ChannelMenu;
import com.sprint.mission.discodeit.menu.MessageMenu;
import com.sprint.mission.discodeit.menu.UserMenu;
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
import java.util.Scanner;

public class JavaApplication {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        UserService userService = new BasicUserService(userRepository, channelRepository, messageRepository);
        ChannelService channelService = new BasicChannelService(userRepository, channelRepository, messageRepository);
        MessageService messageService = new BasicMessageService(userRepository, channelRepository, messageRepository);

        while (true) {
            System.out.println("=============================");
            System.out.println("1. 유저");
            System.out.println("2. 채널");
            System.out.println("3. 메시지");
            System.out.println("4. 나가기");
            System.out.println("=============================");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
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
