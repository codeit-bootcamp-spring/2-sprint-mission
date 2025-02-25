package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

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
    public Message add(String str) {
        String s = String.valueOf(str.hashCode());
        Message message = new Message(s, str);
        return message;
    }
}
