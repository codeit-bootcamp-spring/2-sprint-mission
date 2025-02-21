package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();

        Scanner sc = new Scanner(System.in);

        boolean run = true;
        while (run) {
            System.out.println("\n=== 메뉴 ===");
            System.out.println("1. 사용자");
            System.out.println("2. 채널[구현중]");
            System.out.println("3. 메시지[미구현]");
            System.out.println("4. 종료");
            System.out.print("번호 입력: ");

            String choice_1 = sc.nextLine();


            switch (choice_1) {
                case "1":
                    System.out.println("\n===사용자 메뉴 ===");
                    System.out.println("1. 사용자 생성");
                    System.out.println("2. 사용자 조회");
                    System.out.println("3. 사용자 전체 조회");
                    System.out.println("4. 사용자 수정");
                    System.out.println("5. 사용자 삭제");
                    System.out.print("번호 입력: ");
                    String choice_2 = sc.nextLine();

                    switch (choice_2) {
                        case "1":
                            System.out.println("\n===사용자 등록 ===");
                            System.out.print("사용자 이름 입력: ");
                            String username1 = sc.nextLine();
                            System.out.print("사용자 이메일 입력: ");
                            String usermail1 = sc.nextLine();
                            userService.createUser(username1, usermail1);
                            break;

                        case "2":
                            System.out.println("\n===사용자 조회 ===");
                            System.out.print("사용자 이름 입력: ");
                            String username2 = sc.nextLine();
                            userService.foundUser(username2);
                            break;

                        case "3":
                            System.out.println("\n===사용자 전체 조회 ===");
                            userService.getAllUsers();
                            break;

                        case "4":
                            System.out.println("\n===사용자 수정 ===");
                            System.out.println("변경 할 사용자 이름 입력: ");
                            String username3 = sc.nextLine();
                            System.out.println("새로운 사용자 이름 입력: ");
                            String changename = sc.nextLine();
                            System.out.println("새로운 사용자 이메일 입력: ");
                            String changeemail = sc.nextLine();
                            userService.updateUser(username3, changename, changeemail);
                            break;

                        case "5":
                            System.out.println("\n===사용자 삭제 ===");
                            System.out.println("삭제 할 사용자 이름 입력: ");
                            String username4 = sc.nextLine();
                            userService.deleteUser(username4);
                            break;

                    }
                    continue;

                case "2":
                    run = false;
                    System.out.println("[Info] 종료합니다.");
                    break;

                default:
                    System.out.println("[Error] 잘못된 입력입니다.");

            }
        }
        sc.close();
    }
}
