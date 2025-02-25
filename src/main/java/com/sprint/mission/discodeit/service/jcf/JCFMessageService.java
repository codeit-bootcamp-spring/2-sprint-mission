package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

public class JCFMessageService implements MessageService {
    private static int count = 0;
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
        Message message = new Message("M" + count, "M" + count, str);
        count++;
        return message;
    }
}
