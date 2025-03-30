package com.sprint.mission.discodeit.controller.console;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.UserService.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserService.UserUpdateRequest;
import com.sprint.mission.discodeit.menus.UserMenu;
import com.sprint.mission.discodeit.service.UserService;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.BinaryContentType.IMAGE;

public class ConsoleUserMenuController {
    private final UserService userService;
    private final Scanner scanner;

    public ConsoleUserMenuController(UserService userService, Scanner scanner) {
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

    private byte[] getBinaryContentInput(String description) {
        try {
            System.out.print(description);
            return scanner.nextLine().getBytes();
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private void createUser() {
        System.out.println("생성할 유저명, 이메일, 비밀번호를 입력해주세요.");

        String userName = getUserInput("유저명: ");
        String email = getUserInput("이메일: ");
        String password = getUserInput("비밀번호: ");
        UserCreateRequest userCreateRequest = new UserCreateRequest(userName, email, password);
        System.out.println("프로필 이미지를 설정하시겠습니까?\n" +
                            "1. 예\n" +
                            "2. 아니오");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1", "예":
                byte[] binaryContent = getBinaryContentInput("이미지를 넣어주세요: ");
                BinaryContentDTO binaryContentDTO = new BinaryContentDTO(IMAGE, binaryContent);
                System.out.println("유저 생성 시도: \n" + userService.create(userCreateRequest,binaryContentDTO));
                return;
            case "2", "아니오":
                System.out.println("유저 생성 시도: \n" + userService.create(userCreateRequest,null));
                return;
            default:
                System.out.println("잘못 입력되어 건너뛰었습니다.");
        }
        System.out.println("유저 생성 시도: \n" + userService.create(userCreateRequest, null));

    }

    private void findUser() {
        UUID id = getUserIdFromInput("조회할 유저 ID를 입력해주세요: ");
        System.out.println("조회된 유저: " + userService.findWithStatus(id));
    }

    private void findAllUsers() {
        System.out.println(userService.findAllWithStatus());
    }

    private void updateUser() {
//        UUID id = getUserIdFromInput("업데이트할 유저 ID를 입력해주세요: ");
//        System.out.println("변경할 유저명, 이메일, 비밀번호를 입력하세요.");
//        String userName = getUserInput("유저명: ");
//        String email = getUserInput("이메일: ");
//        String password = getUserInput("비밀번호: ");
//        UserUpdateRequest userUpdateRequest;
//        System.out.println("프로필 사진을 업데이트 하시겠습니까?\n" +
//                            "1. 예\n" +
//                            "2. 아니오");
//        String choice = scanner.nextLine();
//        switch (choice) {
//            case "1", "예":
//                byte[] binaryContent = getBinaryContentInput("이미지를 넣어주세요: ");
//                userUpdateRequest = new UserUpdateRequest(id, userName, email, password, binaryContent);
//                System.out.println("업데이트 시도: \n" + userService.update(, userUpdateRequest));
//                return;
//            case "2", "아니오":
//                userUpdateRequest = new UserUpdateRequest(id, userName, email, password, null);
//                System.out.println("업데이트 시도: \n" + userService.update(, userUpdateRequest));
//                return;
//            default:
//                System.out.println("잘못 입력되어 건너뛰었습니다.");
//        }
//
//        userUpdateRequest = new UserUpdateRequest(id, userName, email, password, null);
//        System.out.println("업데이트 시도: \n" + userService.update(, userUpdateRequest));
    }

    private void deleteUser() {
        UUID id = getUserIdFromInput("삭제할 유저ID를 입력하세요: ");
        userService.delete(id);
    }
}
