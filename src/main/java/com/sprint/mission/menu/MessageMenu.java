package com.sprint.mission.menu;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.jcf.JCFMessageService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class MessageMenu {
    static Scanner sc = new Scanner(System.in);
    public static void display(JCFMessageService messageService) {
        while(true){
            int choice = crudMenu();

            if(choice == 7) return;
            switch(choice){
                case 1:
                    try{
                        System.out.print("메시지 ID 입력: ");
                        UUID messageId = UUID.fromString(sc.nextLine());
                        System.out.print(messageService.getMessage(messageId));
                    }catch(IllegalArgumentException e){
                        System.out.println("메시지 조회에 실패하였습니다.");
                    }
                    break;
                case 2:
                    System.out.println("<모든 메시지의 정보 출력>\n");
                    try{
                        System.out.println(messageService.getAllMessage());
                    }catch(IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try{
                        System.out.print("메시지를 작성할 채널ID 입력: ");
                        UUID channelId = UUID.fromString(sc.nextLine());
                        System.out.print("사용자명 입력: ");
                        String userName = sc.nextLine();
                        System.out.print("메시지 내용 입력: ");
                        messageService.registerMessage(channelId, userName, sc.nextLine());
                        System.out.println("완료되었습니다.");
                    }catch(IllegalArgumentException e){
                        System.out.println("채널 등록에 실패하였습니다.");
                    }
                    break;
                case 4:
                    try{
                        System.out.print("메시지ID 입력: ");
                        UUID messageId = UUID.fromString(sc.nextLine());
                        System.out.print("새로운 메시지 내용 입력: ");
                        messageService.updateMessage(messageId, sc.nextLine());
                        System.out.println("완료되었습니다.");
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("<수정된 메시지 정보 출력>\n");
                    List<Message> updatedMessages = messageService.getUpdatedMessages();
                    if(updatedMessages.isEmpty()){
                        System.out.println("수정된 메시지 정보가 존재하지 않습니다.");
                        break;
                    }
                    System.out.println(updatedMessages);
                    break;
                case 6:
                    try{
                        System.out.print("메시지ID 입력: ");
                        UUID messageId = UUID.fromString(sc.nextLine());
                        messageService.deleteMessage(messageId);
                        System.out.println("완료되었습니다.\n<모든 메시지의 정보 출력>");
                        System.out.print(messageService.getAllMessage());
                    }catch (IllegalArgumentException e){
                        System.out.println("메시지 삭제를 실패하였습니다.");
                    }
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }

    public static int crudMenu(){
        System.out.println("\n=============================");
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
