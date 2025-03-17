package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.menus.UserMenu;
import com.sprint.mission.discodeit.service.UserService;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class UserMenuController {
    private final UserService userService;
    private final Scanner scanner;

    public UserMenuController(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }


    public void handleUserMenu() {
        boolean run = true;
        while (run) {
            for(UserMenu option : UserMenu.values()) {
                System.out.println(option.getCode() + ". " + option.getDescription());
            }
            System.out.print("선택: ");

            String choice = scanner.nextLine();
            UserMenu selectedMenu = UserMenu.getByCode(choice);

            if (selectedMenu == null) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }

            try {
                run = execute(selectedMenu);
            }
            catch(NoSuchElementException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean execute(UserMenu selectedMenu) {
        switch (selectedMenu) {
            case CREATE:
                createUser();
                return true;
            case FIND:
                findUser();
                return true;
            case FINDALL:
                findAllUsers();
                return true;
            case UPDATE:
                updateUser();
                return true;
            case DELETE:
                deleteUser();
                return true;
            case BACK:
                return false;
            default:
                System.out.println("잘못된 입력입니다.");
                return true;
        }
    }

    private UUID getUserIdFromInput(String description) {
        try {
            System.out.print(description);
            return UUID.fromString(scanner.nextLine());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private String getUserInput(String description) {
        try {
            System.out.print(description);
            return scanner.nextLine();
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private void createUser() {
        System.out.println("생성할 유저명, 이메일, 비밀번호를 입력해주세요.");

        String userName = getUserInput("유저명: ");
        String email = getUserInput("이메일: ");
        String password = getUserInput("비밀번호: ");

        System.out.println("유저 생성 완료: \n" + userService.create(userName, email, password));
    }

    private void findUser() {
        UUID id = getUserIdFromInput("조회할 유저 ID를 입력해주세요: ");
        System.out.println("조회된 유저: " + userService.find(id));
    }
    private void findAllUsers() {
        System.out.println(userService.findAll());
    }

    private void updateUser() {
        UUID id = getUserIdFromInput("업데이트할 유저 ID를 입력해주세요: ");
        System.out.println("변경할 유저명, 이메일, 비밀번호를 입력하세요.");
        String userName = getUserInput("유저명: ");
        String email = getUserInput("이메일: ");
        String password = getUserInput("비밀번호: ");
        System.out.println("업데이트 완료: \n" + userService.update(id, userName, email, password));
    }

    private void deleteUser() {
        UUID id = getUserIdFromInput("삭제할 유저ID를 입력하세요: ");
        userService.delete(id);
    }
}
