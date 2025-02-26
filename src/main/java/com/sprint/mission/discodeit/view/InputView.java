package com.sprint.mission.discodeit.view;

import java.util.Scanner;

public class InputView {
    private static final Scanner scanner = new Scanner(System.in); // TODO: 2/26/25 자원을 static 하게 사용하면 발생하는 문제 확인 필요

    private InputView() {
    }

    public static String readUserChoice() {
        System.out.println("""
                # 하고 싶은 기능 선댁
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
}
