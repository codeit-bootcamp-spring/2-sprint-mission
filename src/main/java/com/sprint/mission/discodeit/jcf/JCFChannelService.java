package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


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
    public void write(String str) {
        Message message = messageinstance.write(str);

    }

    @Override
    public void print() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void update() {

    }
}
