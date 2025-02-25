package com.sprint.mission.view.input;

import com.sprint.mission.service.MessageService;
import java.util.Scanner;

public class MessageInput {

    private final Scanner scanner;
    private final MessageService messageService;

    public MessageInput(MessageService messageService) {
        this.messageService = messageService;
        this.scanner = new Scanner(System.in);
    }

    public void checkUser(){
        System.out.print("사용 중인 이메일을 입력 헤주세요 : ");
        String email = scanner.nextLine().trim();
        messageService.validateUserAndStartChat(email);
    }


    public void messageText(String email) {
        while (true) {
            System.out.print("메세지를 입력 해주세요! : ");
            String text = scanner.nextLine().trim();

            if (text.equalsIgnoreCase("종료")) {
                System.out.println("채팅이 종료되었습니다.");
                break;
            }

            messageService.creatMessage(text, email);
        }
    }
}
