package com.sprint.mission.discodeit.front;

import com.sprint.mission.discodeit.controller.UserController;

import java.util.Scanner;
import java.util.UUID;

public class AccessPage {

    UserController userController = new UserController();
    Scanner sc = new Scanner(System.in);

    public UUID initPage() {
        System.out.println("\n1. 로그인\n2. 회원 가입");
        System.out.print("입력: ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                return loginPage();
            case 2:
                registerPage();
                break;
        }
        return null;
    }

    public UUID loginPage() {
        System.out.print("유저 아이디 입력: ");
        UUID userUUID = UUID.fromString(sc.nextLine());
        System.out.print("비밀번호 입력: ");
        String password = sc.nextLine();
        return userController.login(userUUID, password);
    }

    private void registerPage() {
        System.out.print("닉네임 입력: ");
        String nickname = sc.nextLine();
        System.out.print("비밀번호 입력: ");
        String password = sc.nextLine();
        userController.register(nickname, password);
    }

}
