package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<String, Channel> channels = new HashMap<String, Channel>();

    private static final JCFChannelService INSTANCE = new JCFChannelService();

    private JCFChannelService() { }

    public static JCFChannelService getInstance() {
        return INSTANCE;
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
        validateChannelExists(channelName);
        return channels.get(channelName);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<Channel>(channels.values());
    }

    @Override
    public void updateChannelName(Channel channel, String newChannelName) {
        if(channels.containsKey(newChannelName)) {
            throw new IllegalArgumentException("이미 존재하는 채널명입니다.");
        }

        String oldChannelName = channel.getChannelName();
        channel.updateChannelName(newChannelName);
        channels.put(newChannelName, channels.remove(oldChannelName));

        for (User user : channel.getMembers()) {
            user.removeJoinedChannel(oldChannelName);
            user.addJoinedChannel(newChannelName);
        }
    }

    @Override
    public void addUserToChannel(Channel channel, User member) {
        if(channel.isUserInChannel(member)){
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }
        channel.addMembers(member);
        member.addJoinedChannel(channel.getChannelName());
    }

    @Override
    public void addMessageToChannel(Channel channel, Message message) {
        channel.addMessages(message);
    }

    @Override
    public void removeChannel(Channel channel) {
        validateChannelExists(channel.getChannelName());
        for (User user : channel.getMembers()) {
            user.removeJoinedChannel(channel.getChannelName());
        }
        channels.remove(channel.getChannelName());
    }

    @Override
    public void removeUserFromChannel(Channel channel, User member) {
        if(!channel.isUserInChannel(member)){
            throw new IllegalArgumentException("가입되지 않는 유저입니다.");
        }
        member.removeJoinedChannel(channel.getChannelName());
        channel.removeMember(member);
    }

    @Override
    public void removeMessageFromChannel(Channel channel, Message message) {
        if(!channel.isMessageInChannel(message)){
            throw new IllegalArgumentException("존재하지 않는 메세지 입니다.");
        }
        channel.removeMessage(message);
    }

    public void validateChannelExists(String channelName){
        if (!channels.containsKey(channelName)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
