package com.sprint.mission.discodeit.menu;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ChannelMenu {
    static Scanner sc = new Scanner(System.in);

    public static void display(ChannelService channelService) {
        while (true) {
            int choice = crudMenu();

            if (choice == 7) {
                return;
            }
            switch (choice) {
                case 1:
                    try {
                        System.out.print("채널 ID 입력: ");
                        UUID channelId = UUID.fromString(sc.nextLine());
                        System.out.print(channelService.getChannel(channelId));
                    } catch (IllegalArgumentException e) {
                        System.out.println("채널 조회에 실패하였습니다.");
                    }
                    break;
                case 2:
                    System.out.println("<모든 채널의 정보 출력>\n");
                    try {
                        System.out.println(channelService.getAllChannels());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.print("채널을 생성할 사용자명 입력: ");
                        String userName = sc.nextLine();
                        System.out.print("채널명 입력: ");
                        channelService.registerChannel(sc.nextLine(), userName);
                        System.out.println("완료되었습니다.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("채널 등록에 실패하였습니다.");
                    }
                    break;
                case 4:
                    try {
                        System.out.print("채널ID 입력: ");
                        UUID channelId = UUID.fromString(sc.nextLine());
                        System.out.print("새로운 채널명 입력: ");
                        channelService.updateChannel(channelId, sc.nextLine());
                        System.out.println("완료되었습니다.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("<수정된 채널 정보 출력>\n");
                    List<Channel> updatedChannels = channelService.getUpdatedChannels();
                    if (updatedChannels.isEmpty()) {
                        System.out.println("수정된 채널 정보가 존재하지 않습니다.");
                        break;
                    }
                    System.out.println(updatedChannels);
                    break;
                case 6:
                    try {
                        System.out.print("채널ID 입력: ");
                        UUID channelId = UUID.fromString(sc.nextLine());
                        channelService.deleteChannel(channelId);
                        System.out.println("완료되었습니다.\n<모든 채널의 정보 출력>");
                        System.out.print(channelService.getAllChannels());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }

    public static int crudMenu() {
        System.out.println("=============================");
        System.out.println("1. 조회");
        System.out.println("2. 모든 데이터 조회");
        System.out.println("3. 등록");
        System.out.println("4. 수정");
        System.out.println("5. 수정된 데이터 조회");
        System.out.println("6. 삭제");
        System.out.println("7. 메뉴로 돌아가기");
        System.out.println("=============================");
        int choice = sc.nextInt();
        sc.nextLine();

        return choice;
    }
}
