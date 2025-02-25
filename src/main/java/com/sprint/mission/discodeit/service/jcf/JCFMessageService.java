package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageService implements MessageService {

    public final List<Message> messagesData;

    public JCFMessageService() {
        messagesData = new ArrayList<>();
    }



    // 메시지 생성
    @Override
    public void createMessage(Message message) {
        messagesData.add(message);
        System.out.println("-------------------[메시지 전달 결과]-----------------------");
        System.out.println("보내는 사람: " + message.getSender() + "\n내용: " + message.getMessage());
        System.out.println("업데이트 시간: " + message.getupdatedAttFormatted());
        System.out.println("---------------------------------------------------------");

    }

    // 메시지 단일 조회
    @Override
    public void getMessage(String sender){
        boolean found = false;
        for (Message messageList : messagesData) {
            if (messageList.getSender().equals(sender)) {
                System.out.println("-------------------[메시지 조회 결과]-----------------------");
                System.out.println("보낸 사람: " + messageList.getSender() + "\n보낸 내용: " + messageList.getMessage());
                System.out.println("업데이트 시간: " + messageList.getupdatedAttFormatted());
                System.out.println("---------------------------------------------------------");
                found = true;
            }
        }
        if (!found){
            System.out.println("---------------------------------------------------------");
            System.out.println("전달 된 메시지가 없습니다.");
            System.out.println("---------------------------------------------------------");
        }

    }

    // 메시지 전체 조회
    @Override
    public void getAllMessage() {
        System.out.println("-----------------[메시지 전체 조회 결과]---------------------");
        for (Message messageList : messagesData) {
            System.out.printf("보낸 사람: %-10s 보낸 내용: %s\n업데이트 시간: %s\n\n",
                    messageList.getSender(), messageList.getMessage(), messageList.getupdatedAttFormatted());
            System.out.println("---------------------------------------------------------");
        }
    }


    // 메시지 수정
    @Override
    public void updateMessage(String sender, String changeMessage) {
        String oldMessage;
        for (Message messageList : messagesData) {
            if (messageList.getSender().equals(sender)) {
                oldMessage = messageList.getMessage();
                messageList.updateMessage(changeMessage);
                System.out.println("---------------------[메시지 수정 결과]-------------------------");
                System.out.printf("보낸 사람: %s\n이전 내용: %s\n수정 내용: %s\n", messageList.getSender(), oldMessage, messageList.getMessage());
                System.out.println("업데이트 시간: " + messageList.getupdatedAttFormatted());
                System.out.println("---------------------------------------------------------");
                return;
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("전달 한 메시지가 존재하지 않습니다.");
        System.out.println("---------------------------------------------------------");
    }


    // 메시지 삭제
    @Override
    public void deleteMessage(String sender) {
        for (Message messageList : messagesData) {
            if (messageList.getSender().equals(sender)) {
                messagesData.remove(messageList);
                System.out.println("---------------------[메시지 삭제 결과]----------------------");
                System.out.printf("삭제 된 메시지: %s\n", messageList.getMessage());
                System.out.println("---------------------------------------------------------");
                return;
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("입력 한 메시지가 존재하지 않습니다");
        System.out.println("---------------------------------------------------------");
    }


}
