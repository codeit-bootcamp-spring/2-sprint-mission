package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.jcf.JCFUserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        JCFUserService userService = JCFUserService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();

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
                case 2:
                    channelMenu(channelService, userService);
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
                    System.out.println("<모든 사용자의 정보 출력>\n");
                    try{
                        System.out.print(userService.getAllUsers());
                    }catch(IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try{
                        System.out.print("고유한 사용자명 입력: ");
                        String userName = sc.nextLine();
                        System.out.print("별명 입력: ");
                        String nickName = sc.nextLine();
                        userService.registerUser(userName, nickName);
                        System.out.println("완료되었습니다.");
                    }catch(IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try{
                        System.out.print("사용자명 입력: ");
                        String oldUserName = sc.nextLine();
                        System.out.print("새로운 사용자명 입력: ");
                        String newUserName = sc.nextLine();
                        System.out.print("새로운 별명 입력: ");
                        String newNickName = sc.nextLine();
                        userService.updateName(oldUserName, newUserName, newNickName);
                        System.out.println("완료되었습니다.");
                    }catch(IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("<수정된 사용자 정보 출력>\n");
                    List<User> updatedUsers = userService.getUpdatedUsers();
                    if(updatedUsers.isEmpty()){
                        System.out.println("수정된 사용자 정보가 존재하지 않습니다.");
                        break;
                    }
                    System.out.println(updatedUsers);
                    break;
                case 6:
                    try {
                        System.out.print("사용자명 입력: ");
                        String deleteUserName = sc.nextLine();
                        userService.deleteUser(deleteUserName);
                        System.out.println("완료되었습니다.\n<모든 사용자의 정보 출력>");
                        System.out.print(userService.getAllUsers());
                    }catch(IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }

    public static void channelMenu(JCFChannelService channelService, JCFUserService userService) {
        while (true) {
            int choice = crudMenu();

            if(choice == 7) return;
            switch(choice){
                case 1:
                    try{
                        System.out.print("채널 ID 입력: ");
                        UUID channelId = UUID.fromString(sc.nextLine());
                        System.out.print(channelService.getChannel(channelId));
                    }catch(IllegalArgumentException e){
                        System.out.println("채널 조회에 실패하였습니다.");
                    }
                    break;
                case 2:
                    System.out.println("<모든 채널의 정보 출력>\n");
                    try{
                        System.out.println(channelService.getAllChannels());
                    }catch(IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try{
                        System.out.print("채널을 생성할 사용자 ID 입력: ");
                        UUID userId = UUID.fromString(sc.nextLine());
                        userService.uidExists(userId);
                        System.out.print("채널명 입력: ");
                        channelService.registerChannel(sc.nextLine(), userId);
                        System.out.println("완료되었습니다.");
                    }catch(IllegalArgumentException e){
                        System.out.println("채널 등록에 실패하였습니다.");
                    }
                    break;
                case 4:
                    try{
                        System.out.print("채널ID 입력: ");
                        UUID channelId = UUID.fromString(sc.nextLine());
                        System.out.print("새로운 채널명 입력: ");
                        channelService.updateChannel(channelId, sc.nextLine());
                        System.out.println("완료되었습니다.");
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("<수정된 채널 정보 출력>\n");
                    List<Channel> updatedChannels = channelService.getUpdatedChannels();
                    if(updatedChannels.isEmpty()){
                        System.out.println("수정된 채널 정보가 존재하지 않습니다.");
                        break;
                    }
                    System.out.println(updatedChannels);
                    break;
                case 6:
                    try{
                        System.out.print("채널ID 입력: ");
                        UUID channelId = UUID.fromString(sc.nextLine());
                        channelService.deleteChannel(channelId);
                        System.out.println("완료되었습니다.\n<모든 채널의 정보 출력>");
                        System.out.print(channelService.getAllChannels());
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
            }
        }
    }
}