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

    public void creatRequest() {
        System.out.println("사용자을 입력해주세요");
        String username = scanner.nextLine().trim();

        System.out.print("이메일을 입력하세요: ");
        String email = scanner.nextLine().trim();

        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine().trim();

        userService.createUser(username, email, password);
    }

    public void updateInput() {

        System.out.print("이메일 입력해주세요: ");
        String email = scanner.nextLine().trim();

        System.out.print("변경 할 이름을 입력해주세요: ");
        String username = scanner.nextLine().trim();

        System.out.print("변경 할 비밀번호 입력해주세요: ");
        String password = scanner.nextLine().trim();
        userService.updateUser(email, username, password);
    }

    /**
     *  TODO ID 으로  입력 받기 나중에 구현
     */
    public void getAllInput() {
        userService.getAllUser();
    }


    public void getEmailInput() {
        System.out.print("찾을 이메일을 입력하세요: ");
        String email = scanner.nextLine().trim();

        userService.getEmailUser(email);
    }


}
