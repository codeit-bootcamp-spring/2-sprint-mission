package com.sprint.mission.view.input;

import com.sprint.mission.controller.UserService;
import java.util.Scanner;

public class UserInput {


    private final Scanner scanner;

    private final UserService userService;


    public UserInput(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void creatInput() {
        System.out.println("사용자을 입력해주세요");
        String username = scanner.nextLine().trim();

        System.out.print("이메일을 입력하세요: ");
        String email = scanner.nextLine().trim();

        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine().trim();

        userService.createUser(username, email, password);
    }
}
