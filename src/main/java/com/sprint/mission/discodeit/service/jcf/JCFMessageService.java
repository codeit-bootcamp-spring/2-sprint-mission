package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    Map<UUID, Message> data;
    private static JCFMessageService instance = new JCFMessageService();

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }

    private JCFMessageService() {
        data = new HashMap<>();
    }


    @Override
    public UUID createMessage() {
        Message message = new Message();
        data.put(message.getId(), message);
        System.out.println("메세지가 생성되었습니다: \n" + message);
        return message.getId();
    }

    @Override
    public void searchMessage(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("조회하신 메세지가 존재하지 않습니다.");
            return;
        }
        System.out.println("MESSAGE: " + data.get(id));
    }

    @Override
    public void searchAllMessages() {
        if (data.isEmpty()) {
            System.out.println("등록된 메세지가 존재하지 않습니다.");
            return;
        }
        for (Message message : data.values()) {
            System.out.println("MESSAGE: " + message);
        }
    }

    @Override
    public void updateMessage(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("업데이트할 메세지가 존재하지 않습니다.");
            return;
        }
        data.get(id).update();
        System.out.println(id + " 메세지 업데이트 완료되었습니다.");
    }

    @Override
    public void deleteMessage(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("삭제할 메세지가 존재하지 않습니다.");
            return;
        }
        data.remove(id);
        System.out.println(id + " 메세지 삭제 완료되었습니다.");

    }
}
