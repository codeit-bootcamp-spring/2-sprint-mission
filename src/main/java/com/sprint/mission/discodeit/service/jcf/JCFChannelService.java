package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {
    private final List<Channel> channelList;
    private final MessageService messageService;

    public JCFChannelService(JCFMessageService messageService) {
        this.channelList = new ArrayList<>();
        this.messageService = messageService;
    }

    @Override
    public Channel create(Channel channel) {
        this.channelList.add(channel);
        return channel;
    }

    @Override
    public void addUserToChannel(String channelName, User user) {
        Channel channel = findByChannelName(channelName);
        if (channel == null) {
            throw new IllegalArgumentException("해당 채널이 존재하지 않습니다.");
        }
        channel.addUser(user);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelList);
    }

    @Override
    public Channel findByChannelName(String channelName) {
        for (Channel channel : this.channelList) {
            if (channel.getChannelName().equals(channelName)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> find(String identifier) {
        List<Channel> channelsWithUser = new ArrayList<>();
        for (Channel channel : this.channelList) {
            for (User user : channel.getUsers()) {
                if (user.getUserId().equals(identifier) || user.getUserName().equals(identifier)) {
                    channelsWithUser.add(channel);
                    break;
                }
            }
        }
        return channelsWithUser;
    }

    @Override
    public Channel update(String channelName, Channel channel) {
        for (int i = 0; i < channelList.size(); i++) {
            Channel channelForm = channelList.get(i);
            if (channelForm.getChannelName().equals(channelName)) {
                channelForm.setChannelName(channel.getChannelName());
                return channelForm;
            }
        }
        return null;
    }

    @Override
    public void delete(String channelName) {
        channelList.removeIf(channel -> channel.getChannelName().equals(channelName));
    }

    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        List<Message> messagesForChannel = new ArrayList<>();
        for (Message message : messageService.findAll()) {
            if (message.getChannel().getChannelName().equals(channel.getChannelName())) {
                messagesForChannel.add(message);
            }
        }
        return messagesForChannel;
    }
}
