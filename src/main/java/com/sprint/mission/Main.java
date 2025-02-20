package com.sprint.mission;
import com.sprint.mission.discodeit.jcf.JCFUserService;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        JCFUserService userService = JCFUserService.getInstance();

        while(true){
            System.out.println("=============================");
            System.out.println("1. 유저");
            System.out.println("2. 채널");
            System.out.println("3. 메시지");
            System.out.println("4. 나가기");
            System.out.println("=============================");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    userMenu(userService);
                    break;
                case 4:
                    return;
            }

        }
    }

    public static int crudMenu(){
        System.out.println("=============================");
        System.out.println("1. 조회");
        System.out.println("2. 모든 데이터 조회");
        System.out.println("3. 등록");
        System.out.println("4. 수정");
        System.out.println("5. 삭제");
        System.out.println("6. 메뉴로 돌아가기");
        System.out.println("=============================");
        int choice = sc.nextInt();
        sc.nextLine();

        return choice;
    }

    public static void userMenu(JCFUserService userService){
        while(true){
            int choice = crudMenu();

            if(choice == 6) return;
            switch(choice){
                case 1:
                    System.out.println("사용자명 입력: ");
                    String findUser = sc.nextLine();
                    System.out.println(userService.getUser(findUser));
                    break;
                case 2:
                    System.out.println("모든 사용자의 정보 출력");
                    System.out.println(userService.getAllUsers());
                    break;
                case 3 :
                    System.out.print("고유한 사용자명 입력: ");
                    String userName = sc.nextLine();
                    System.out.print("별명 입력: ");
                    String nickName = sc.nextLine();
                    userService.registerUser(userName, nickName);
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}