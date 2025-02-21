package com.sprint.mission;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.jcf.JCFUserService;

import java.util.List;
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
        System.out.println("5. 수정된 데이터 조회");
        System.out.println("6. 삭제");
        System.out.println("7. 메뉴로 돌아가기");
        System.out.println("=============================");
        int choice = sc.nextInt();
        sc.nextLine();

        return choice;
    }

    public static void userMenu(JCFUserService userService){
        while(true){
            int choice = crudMenu();

            if(choice == 7) return;
            switch(choice){
                case 1:
                    System.out.print("사용자명 입력: ");
                    String findUser = sc.nextLine();
                    System.out.print(userService.getUser(findUser));
                    break;
                case 2:
                    System.out.println("모든 사용자의 정보 출력\n");
                    System.out.println(userService.getAllUsers());
                    break;
                case 3:
                    System.out.print("고유한 사용자명 입력: ");
                    String userName = sc.nextLine();
                    System.out.print("별명 입력: ");
                    String nickName = sc.nextLine();
                    userService.registerUser(userName, nickName);
                    break;
                case 4:
                    System.out.print("사용자명 입력: ");
                    String oldUserName = sc.nextLine();
                    if(!(userService.userNameExists(oldUserName))){
                        System.out.println("존재하지 않는 사용자명입니다.");
                         break;
                    }
                    System.out.print("새로운 사용자명 입력: ");
                    String newUserName = sc.nextLine();
                    System.out.print("새로운 별명 입력: ");
                    String newNickName = sc.nextLine();
                    userService.updateName(oldUserName, newUserName, newNickName);
                    break;
                case 5:
                    System.out.println("수정된 사용자 정보 출력\n");
                    List<User> updatedUsers = userService.getUpdatedUsers();
                    if(updatedUsers.isEmpty()){
                        System.out.println("수정된 사용자 정보가 존재하지 않습니다.");
                        break;
                    }
                    System.out.println(updatedUsers);
                    break;
                case 6:
                    System.out.print("사용자명 입력: ");
                    String deleteUserName = sc.nextLine();
                    System.out.println(userService.deleteUser(deleteUserName));
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}