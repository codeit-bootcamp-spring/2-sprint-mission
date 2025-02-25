package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;


public class JCFChannelService implements ChannelService {
    private static JCFChannelService instance;

    private JCFChannelService(){}

    public static JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }
        return instance;
    }

    @Override
    public Message write(String str) {
        JCFMessageService messageinstance = JCFMessageService.getInstance();
        Message message = messageinstance.write(str);
        return message;
    }
}
