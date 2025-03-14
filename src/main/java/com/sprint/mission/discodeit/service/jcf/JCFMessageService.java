package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final List<Message> messagesData;
    private UserService userService;

    public JCFMessageService(UserService userService) {
        messagesData = new ArrayList<>();
        this.userService = userService;
    }


    // 메시지 생성
    @Override
    public Message create(String message, UUID channelId, UUID senderId) {
        Message messages = new Message(message, channelId, senderId);
        if (!validateMessage(channelId)) {
            return null;
        }
        return createMessage(messages);
    }

    private Message createMessage(Message message) {
        messagesData.add(message);
        System.out.println(message);
        return message;
    }

    private boolean validateMessage(UUID messageId) {
        User user = userService.getUser(messageId);
        if (user != null && user.getId().equals(messageId)) {
            return true;
        }
        System.out.println("등록 된 사용자가 없습니다.");
        return false;
    }


    // 메시지 단일 조회
    @Override
    public Message getMessage(UUID messageId) {
        Optional<Message> message = findMessage(messageId).stream().findAny();
        return message.orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));
    }

    private List<Message> findMessage(UUID messageId) {
        boolean find = false;
        List<Message> result = new ArrayList<>();
        for (Message messageList : messagesData) {
            if (messageList.getId().equals(messageId)) {
                result.add(messageList);
                find = true;
            }
        }
        if (!find) {
            System.out.println("메시지가 존재하지 않습니다.");
        }
        return result;
    }

    private Message find(UUID messageIdr) {
        for (Message messageList : messagesData) {
            if (messageList.getId().equals(messageIdr)) {
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
    public Message update (UUID messageId, String changeMessage){
        return updateMessage(messageId, changeMessage);
    }

    private Message updateMessage(UUID messageId, String changeMessage){
        Message senderName = find(messageId);
        if (senderName != null && senderName.getId().equals(messageId)) {
            senderName.updateMessage(changeMessage);
            System.out.printf("보낸 내용이 [ %s ] 로 변경되었습니다.", senderName.getMessage());
            return senderName;
        }
        System.out.println("메시지가 존재하지 않습니다.");
        return null;
    }


    // 메시지 삭제
    @Override
    public void delete (UUID messageId){
        Message sendName = find(messageId);
        if (sendName != null && sendName.getId().equals(messageId)) {
            messagesData.remove(sendName);
            System.out.println("[ " + sendName.getId() + " ] 이 삭제 되었습니다.");
        }
        System.out.println("메시지가 존재하지 않습니다");
    }
}
