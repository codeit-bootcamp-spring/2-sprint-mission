package com.sprint.mission.discodeit.view;

import java.util.Scanner;

public class InputView {
    private final Scanner scanner;

    public InputView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readCommand() {
        System.out.println("""
                # 하고 싶은 기능 선택
                - 다른 채널 생성 : 1번
                - 현재 채널에 친구 추가 : 2번
                - 현재 채널 이름변경 : 3번
                - 현재 채널에 메세지 입력 : 4번
                - 다른 채널 이동 : 5번
                - 종료 : 6번
                """);

        return scanner.nextLine();
    }

    public String readChangeChannelName() {
        System.out.println("# 현재 채널을 어떤 이름으로 변경하시겠습니까? : ");

        return scanner.nextLine();
    }

    public String readEmail() {
        System.out.println("# 친구 이메일 : ");

        return scanner.nextLine();
    }

    public String readMessage() {
        System.out.println("# 메세지 입력 : ");

        return scanner.nextLine();
    }

    public String readCreationChannelName() {
        System.out.println("# 생성할 채널의 이름을 설정해주세요 : ");

        return scanner.nextLine();
    }

    public int readChangeNumber() {
        int number = scanner.nextInt();
        scanner.nextLine();

        return number - 1;
    }
}
