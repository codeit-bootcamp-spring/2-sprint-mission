package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    Map<UUID, Message> messagesRepository = new HashMap<>();
    //메세지를 저장하는 곳 (메세지 id, 메세지 객체) 형태로 되어있음
    @Override
    public boolean sendMessage(Message message) {
        //같은 서버여야하고 서버안에 수신인 발신인 둘다 있어야함
        //User user,String content,User receiver,UUID toChannelId => 메세지
        //String userName,Channel channel => 유저

        if(message.getContent()==null) {
            return false;
        }
        messagesRepository.put(message.getMessageId(),message);
        return true;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        if(messagesRepository.containsKey(messageId)) {
            messagesRepository.remove(messageId);
            return true;
        }
        return false;
    }

//    @Override
//    public List<String> findAllMessages() {
//        return messagesRepository.values().stream()
//                .map(Message::getContent)
//                .toList();//메세지 객체만 주기
//    }

    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(messagesRepository.values());
    }

    @Override
    public String createAllMessageContents() {
        String result = "";
        for (Message message : findAllMessages()) {
            result += message.getContent() + " (" + message.getMessageId() + ")" + "\n";
        }
        return result;
    }

    @Override
    public Message findOneMessage(UUID messageId) {
        return messagesRepository.get(messageId);
    }

    @Override
    public void editMessage(UUID messageId, String content) {
        if(messagesRepository.containsKey(messageId)) {
            messagesRepository.get(messageId).setContent(content);
        }
    }
    BiPredicate<Message,Message> equalUpdate=(createdAtM, updatedAtM)->
            createdAtM.getCreatedAt()==updatedAtM.getUpdatedAt();
    
    
    @Override
    public String displayEditmessages() {//출력 안됨
        String result = "";
        for (Message message : findEditedMessages()) {
            result += message.getContent() + " (" + message.getMessageId() + ")" + "\n";
        }
        return result;
    }

    @Override
    public List<Message> findEditedMessages() {
        return messagesRepository.values().stream()
                .filter(message -> equalUpdate.test(message,message))
                .toList();
    }
}
