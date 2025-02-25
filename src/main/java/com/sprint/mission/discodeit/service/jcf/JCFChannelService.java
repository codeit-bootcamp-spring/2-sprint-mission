package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;


public class JCFChannelService implements ChannelService {
    JCFMessageService messageinstance = JCFMessageService.getInstance();
    private static JCFChannelService channelinstance;

    private JCFChannelService(){}

    public JCFChannelService getInstance() {
        if (channelinstance == null) {
            channelinstance = new JCFChannelService();
        }
        return channelinstance;
    }

    @Override
    public Message write(String str) {
        Message message = messageinstance.write(str);
        return message;
    }
}
