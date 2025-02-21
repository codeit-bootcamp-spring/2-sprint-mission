package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    Map<String, Channel> channels = new HashMap<String, Channel>();

    @Override
    public void createChannel(String channelName) {
        channels.put(channelName, new Channel(channelName));
    }

    @Override
    public Channel getChannel(String channelName) {
        return channels.get(channelName);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<Channel>(channels.values());
    }

    @Override
    public void updateChannelName(Channel channel, String newChannelName) {
        channels.remove(channel.getChannelName());
        channel.updateChannelName(newChannelName);
        channels.put(newChannelName, channel);
    }

    @Override
    public void addUserToChannel(Channel channel, String userName) {
        channel.updateMembers(userName);
        channels.put(channel.getChannelName(), channel);

    }

    @Override
    public void addMessageToChannel(Channel channel, Message message) {
        channel.updateMessages(message);
        channels.put(channel.getChannelName(), channel);
    }

    @Override
    public void removeUserFromChannel(Channel channel, String userName) {
        channel.removeMember(userName);
        channels.put(channel.getChannelName(), channel);
    }

    @Override
    public void removeMessageFromChannel(Channel channel, Message message) {
        channel.removeMessage(message);
        channels.put(channel.getChannelName(), channel);
    }
}
