package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.TimeFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
     Map<UUID, Message> messageRepository = new HashMap<>();

    @Override
    public void saveMessage(Message message) {
        messageRepository.put(message.getId(), message);
    }

    @Override
    public void findAll() {
        List<Message> messageList = messageRepository.values().stream().toList();

        if(messageList.isEmpty()){
            System.out.println("등록된 메세지가 없습니다.");
        }else {
            for (Message message : messageList) {
                System.out.println("메세지 ID: " + message.getId());
                System.out.println("메세지 내용: " + message.getText());
                System.out.println("메세지 생성 일자: " + TimeFormatter.format(message.getCreatedAt()));
                System.out.println("메세지 수정 일자: " + TimeFormatter.format(message.getUpdatedAt()));
                System.out.println("----------------------------------");
            }
        }
    }

    @Override
    public void findById(UUID id) {
        if(!messageRepository.containsKey(id)){
            System.out.println("해당 ID를 가진 메세지가 없습니다.");
        }else{
            Message message = messageRepository.get(id);
            System.out.println("메세지 ID: " + message.getId());
            System.out.println("메세지 내용: " + message.getText());
            System.out.println("메세지 생성 일자: " + TimeFormatter.format(message.getCreatedAt()));
            System.out.println("메세지 수정 일자: " + TimeFormatter.format(message.getUpdatedAt()));
            System.out.println("----------------------------------");
        }
    }

    @Override
    public void update(UUID id, String message) {
        if(!messageRepository.containsKey(id)){
            System.out.println("업데이트 할 메세지가 없습니다.");
        }else{
            messageRepository.get(id).setText(message);
            messageRepository.get(id).setUpdatedAt(System.currentTimeMillis());
        }
    }

    @Override
    public void delete(UUID id) {
        messageRepository.remove(id);
    }
}
