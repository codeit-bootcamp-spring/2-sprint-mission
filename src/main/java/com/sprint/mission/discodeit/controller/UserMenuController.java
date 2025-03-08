package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.UserMenu;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class UserMenuController {
    private final JCFUserService userService;
    private final Scanner scanner;

    public UserMenuController(JCFUserService userService, Scanner scanner) {
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

            run = excute(selectedMenu);
        }
    }

    private boolean excute(UserMenu selectedMenu) {
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

    private void createUser() {
        System.out.println("생성할 유저명, 이메일, 비밀번호를 입력해주세요.");
        System.out.print("유저명: ");
        String userName = scanner.nextLine();
        System.out.print("이메일: ");
        String email = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        try {
            System.out.println("유저 생성 완료: \n" +
                    userService.create(userName, email, password));
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    private void findUser() {
        System.out.print("조회할 유저ID를 입력해주세요: ");
        UUID id = UUID.fromString(scanner.nextLine());
        try {
            System.out.println("조회된 유저: " + userService.find(id));
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }
    private void findAllUsers() {
        System.out.println(userService.findAll());
    }

    private void updateUser() {
        System.out.print("업데이트를 할 유저ID를 입력해주세요: ");
        UUID id = UUID.fromString(scanner.nextLine());
        System.out.println("변경할 유저명, 이메일, 비밀번호를 입력하세요.");
        System.out.print("유저명: ");
        String userName = scanner.nextLine();
        System.out.print("이메일: ");
        String email = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();
        try {
            System.out.println("업데이트 완료: \n"
                    + userService.update(id, userName, email, password));
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.print("삭제할 유저ID를 입력하세요: ");
        UUID id = UUID.fromString(scanner.nextLine());
        try {
            userService.delete(id);
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }
}
