package com.sprint.mission.discodeit.menu;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Scanner;

public class UserMenu {
    static Scanner sc = new Scanner(System.in);

    public static void display(UserService userService) {
        while (true) {
            int choice = crudMenu();

            if (choice == 7) {
                return;
            }
            switch (choice) {
                case 1:
                    System.out.print("사용자명 입력: ");
                    String findUser = sc.nextLine();
                    try {
                        System.out.println(userService.getUser(findUser));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("<모든 사용자의 정보 출력>\n");
                    try {
                        System.out.print(userService.getAllUsers());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.print("고유한 사용자명 입력: ");
                        String userName = sc.nextLine();
                        System.out.print("별명 입력: ");
                        String nickName = sc.nextLine();
                        userService.registerUser(userName, nickName);
                        System.out.println("완료되었습니다.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        System.out.print("사용자명 입력: ");
                        String oldUserName = sc.nextLine();
                        System.out.print("새로운 사용자명 입력: ");
                        String newUserName = sc.nextLine();
                        System.out.print("새로운 별명 입력: ");
                        String newNickName = sc.nextLine();
                        userService.updateName(oldUserName, newUserName, newNickName);
                        System.out.println("완료되었습니다.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("<수정된 사용자 정보 출력>\n");
                    List<User> updatedUsers = userService.getUpdatedUsers();
                    if (updatedUsers.isEmpty()) {
                        System.out.println("수정된 사용자 정보가 존재하지 않습니다.");
                        break;
                    }
                    System.out.println(updatedUsers);
                    break;
                case 6:
                    try {
                        System.out.print("사용자명 입력: ");
                        userService.deleteUser(sc.nextLine());
                        System.out.println("완료되었습니다.\n<모든 사용자의 정보 출력>");
                        System.out.print(userService.getAllUsers());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }

    public static int crudMenu() {
        System.out.println("=============================");
        System.out.println("1. 조회");
        System.out.println("2. 모든 데이터 조회");
        System.out.println("3. 등록");
        System.out.println("4. 수정");
        System.out.println("5. 수정된 데이터 조회");
        System.out.println("6. 삭제");
        System.out.println("7. 메뉴로 돌아가기");
        System.out.println("=============================");
        int choice = sc.nextInt();
        sc.nextLine();

        return choice;
    }
}
