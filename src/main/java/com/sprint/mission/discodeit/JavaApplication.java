package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.Arrays;
import java.util.Scanner;

public class JavaApplication {

    public static void main(String[] args) {
        // 서비스 초기화
//        UserService userService = JCFUserService.getInstance();
//        ChannelService channelService = JCFChannelService.getInstance(userService);
//        MessageService  messageService = JCFMessageService.getInstance(userService, channelService);

        UserService userService = FileUserService.getInstance();
        ChannelService channelService = FileChannelService.getInstance(userService);
        MessageService messageService = FileMessageService.getInstance(userService, channelService);


        Scanner scanner = new Scanner(System.in);

        while (true) {
            ChatSystemMenu.printMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 제거

            MainMenuOption option = MainMenuOption.fromCode(choice);
            if (option == null) {
                System.out.println("올바른 메뉴를 선택하세요.");
                continue;
            }

            switch (option) {
                case CREATE:
                    ChatSystemMenu.processCreateMenu(scanner, userService, channelService, messageService);
                    break;
                case SINGLE_LOOKUP:
                    ChatSystemMenu.processSingleLookupMenu(scanner, userService, channelService, messageService);
                    break;
                case MULTIPLE_LOOKUP:
                    ChatSystemMenu.processMultipleLookupMenu(scanner, userService, channelService, messageService);
                    break;
                case UPDATE:
                    ChatSystemMenu.processUpdateMenu(scanner, userService, channelService, messageService);
                    break;
                case DELETE:
                    ChatSystemMenu.processDeleteMenu(scanner, userService, channelService, messageService);
                    break;
                case ALL_LOOKUP:
                    ChatSystemMenu.processAllLookup(scanner, userService, channelService, messageService);
                    break;
                case EXIT:
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    return;
            }
        }
    }

    public enum MainMenuOption {
        CREATE(1, "유저, 채널, 메세지 생성"),
        SINGLE_LOOKUP(2, "정보 조회(단건)"),
        MULTIPLE_LOOKUP(3, "정보 조회(다건)"),
        UPDATE(4, "정보 수정"),
        DELETE(5, "데이터 삭제"),
        ALL_LOOKUP(6, "모든 유저 및 채널 조회"),
        EXIT(7, "종료");

        private final int code;
        private final String description;

        MainMenuOption(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static MainMenuOption fromCode(int code) {
            return Arrays.stream(MainMenuOption.values())
                    .filter(option -> option.getCode() == code)
                    .findFirst()
                    .orElse(null);
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
