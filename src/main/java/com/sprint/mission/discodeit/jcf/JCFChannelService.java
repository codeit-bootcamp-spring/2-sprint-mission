package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<String, Channel> channels = new HashMap<String, Channel>();
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void createChannel(String channelName) {
        if(channels.containsKey(channelName)){
            throw new IllegalArgumentException("이미 존재하는 채널입니다.");
        }
        channels.put(channelName, new Channel(channelName));
    }

    @Override
    public Channel getChannel(String channelName) {
        if(!channels.containsKey(channelName)){
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        return channels.get(channelName);
    }

    @Override
    public List<Channel> getAllChannels() {
        if(channels.isEmpty()){
            throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        }
        return new ArrayList<Channel>(channels.values());
    }

    @Override
    public void updateChannelName(Channel channel, String newChannelName) {
        if(channels.containsKey(newChannelName)) {
            throw new IllegalArgumentException("이미 존재하는 채널명입니다.");
        }
        channels.remove(channel.getChannelName());
        channel.updateChannelName(newChannelName);
        channels.put(newChannelName, channel);
    }

    @Override
    public void addUserToChannel(Channel channel, String userName) {
        userService.validateUserExists(userName);

        if(channel.isUserInChannel(userName)){
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }
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
        if(!channel.isUserInChannel(userName)){
            throw new IllegalArgumentException("가입되어 있지 않은 유저입니다.");
        }
        channel.removeMember(userName);
        channels.put(channel.getChannelName(), channel);
    }

    @Override
    public void removeMessageFromChannel(Channel channel, Message message) {
        if(!channel.isMessageInChannel(message)){
            throw new IllegalArgumentException("존재하지 않은 메세지 입니다.");
        }
        channel.removeMessage(message);
        channels.put(channel.getChannelName(), channel);
    }

    public void validateChannelExists(String channelName){
        if (!channels.containsKey(channelName)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
