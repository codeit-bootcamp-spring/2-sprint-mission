package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channels = new HashMap<>();

    public JCFChannelService() {}

    @Override
    public void create(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public Channel find(UUID id) {
        return channels.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void update(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }

    @Override
    public void addMember(UUID channelId, User user) {
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channel.addMember(user);
        update(channel);
        System.out.println("유저 [" + user.getName() + "]가 채널 [" + channel.getChannelName() + "]에 추가되었습니다.");
    }

    @Override
    public void removeMember(UUID channelId, User user) {
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channel.removeMember(user);
        update(channel);
        System.out.println("유저 [" + user.getName() + "]가 채널 [" + channel.getChannelName() + "]에서 제거되었습니다.");
    }

    @Override
    public List<User> findMembers(UUID channelId) {
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        ArrayList<User> members = new ArrayList<>(channel.getMembers());
        System.out.println("채널 [" + channel.getChannelName() + "]에 등록된 유저 목록: " + members);
        return members;
    }
}