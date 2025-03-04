package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.application.ChannelDto;
import java.util.List;

public class InputView {
    private InputView() {
    }

    public static String readCommand() {
        System.out.println("""
                # 하고 싶은 기능 선택
                - 다른 채널 생성 : 1번
                - 현재 채널에 친구 추가 : 2번
                - 현재 채널 이름변경 : 3번
                - 현재 채널에 메세지 입력 : 4번
                - 다른 채널 이동 : 5번
                - 종료 : 6번
                """);

        return Console.readLine();
    }

    public static String readChannelName() {
        System.out.println("# 현재 채널을 어떤 이름으로 변경하시겠습니까? : ");

        return Console.readLine();
    }

    public static String readEmail() {
        System.out.println("# 친구 이메일 : ");

        return Console.readLine();
    }

    public static String readMessage() {
        System.out.println("# 메세지 입력 : ");

        return Console.readLine();
    }

    public static String readNewChannelName() {
        System.out.println("# 생성할 채널의 이름을 설정해주세요 : ");

        return Console.readLine();
    }

    public static int readChangeChannelNumber(List<ChannelDto> channels) {
        System.out.println("# 이동할 채널을 선택해주세요");
        int count = 1;
        for (ChannelDto channel : channels) {
            System.out.println("- " + channel.name() + " : " + count++ + "번");
        }

        return Integer.parseInt(Console.readLine());
    }
}
