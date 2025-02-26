package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;

    private JCFMessageService() {
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }

    @Override
    public void send(UUID targetId, String str) {

    }

    @Override
    public void send(UUID targetId, Message message) {

    }
}
