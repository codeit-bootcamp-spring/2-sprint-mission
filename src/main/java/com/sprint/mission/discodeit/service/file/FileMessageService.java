package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    public final List<Message> messagesData;
    public UserService userService;

    public FileMessageService(UserService userService) {
        messagesData = new ArrayList<>();
        this.userService = userService;
    }


    // 메시지 생성
    @Override
    public Message create(Message message) {
        if (!validateMessage(message)) {
            return null;
        }
        return createMessage(message);
    }

    private Message createMessage(Message message) {
        messagesData.add(message);
        System.out.println(message);
        return message;
    }

    private boolean validateMessage(Message message) {
        User user = userService.getUser(message.getSender());
        if (user != null && user.getName().equals(message.getSender())) {
            return true;
        }
        System.out.println("등록 된 사용자가 없습니다.");
        return false;
    }


    // 메시지 단일 조회
    @Override
    public List<Message> getMessage(String sender) {
        return findMessage(sender);
    }

    private List<Message> findMessage(String sender) {
        boolean find = false;
        List<Message> result = new ArrayList<>();
        for (Message messageList : messagesData) {
            if (messageList.getSender().equals(sender)) {
                result.add(messageList);
                find = true;
            }
        }
        if (!find) {
            System.out.println("메시지가 존재하지 않습니다.");
        }
        return result;
    }

    private Message find(String sender) {
        for (Message messageList : messagesData) {
            if (messageList.getSender().equals(sender)) {
                return messageList;
            }
        }
        return null;
    }


    // 메시지 전체 조회
    @Override
    public List<Message> getAllMessage() {
        return findAllMessage();
    }

    private List<Message> findAllMessage(){
        if (messagesData.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        for (Message messageList : messagesData) {
            System.out.println(messageList);
        }
        return messagesData;
    }



    // 메시지 수정
    @Override
    public Message update (String sender, UUID uuid, String changeMessage){
        return updateMessage(sender, uuid, changeMessage);
    }

    private Message updateMessage(String sender,UUID uuid, String changeMessage){
        Message senderName = find(sender);
        if (senderName != null && senderName.getId().equals(uuid)) {
            senderName.updateMessage(changeMessage);
            System.out.printf("보낸 내용이 [ %s ] 로 변경되었습니다.", senderName.getMessage());
            return senderName;
        }
        System.out.println("메시지가 존재하지 않습니다.");
        return null;
    }




    // 메시지 삭제
    @Override
    public Message delete (String sender){
        return deleteMessage(sender);
    }
    private Message deleteMessage(String sender){
        Message sendName = find(sender);
        if (sendName != null) {
            messagesData.remove(sendName);
            System.out.println("[ " + sendName.getSender() + " ] 이 삭제 되었습니다.");
            return sendName;
        }
        System.out.println("메시지가 존재하지 않습니다");
        return null;
    }

}