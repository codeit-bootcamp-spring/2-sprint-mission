package com.sprint.mission.discodeit.view;

import java.util.Scanner;

public class InputView {
    private static final Scanner scanner = new Scanner(System.in);

    private InputView() {
    }

    public static String readUserChoice() {
        System.out.println("""
                # 하고 싶은 기능 선택
                - 다른 채널 생성 : 1번
                - 현재 채널에 유저 등록 : 2번
                - 현재 채널 이름변경 : 3번
                - 현재 채널에 친구 추가 : 4번
                - 현재 채널에 메세지 입력 : 5번
                - 다른 채널 이동 : 6번
                - 종료 : 7번
                """);

        return scanner.nextLine();
    }

    public static String readChannelName() {
        System.out.println("# 현재 채널을 어떤 이름으로 변경하시겠습니까? : ");

        return scanner.nextLine();
    }

    public static String readEmail() {
        System.out.println("# 친구 이메일 : ");

        return scanner.nextLine();
    }

    public static String readMessage() {
        System.out.println("# 메세지 입력 : ");

        return scanner.nextLine();
    }

    public static String readNewChannelName() {
        System.out.println("# 생성할 채널의 이름을 설정해주세요 : ");

        return scanner.nextLine();
    }
}
