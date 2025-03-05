package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelData = new HashMap<>();

    private void checkById(UUID cid) {
        if (!(channelData.containsKey(cid))) {
            throw new IllegalArgumentException("존재하지 않는 채널ID입니다.");
        }
    }

    @Override
    public Channel findById(UUID channelId) {
        checkById(channelId);
        return channelData.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        if (channelData.isEmpty()) {
            throw new IllegalArgumentException("데이터가 존재하지 않습니다.");
        }
        return new ArrayList<>(channelData.values());
    }

    @Override
    public List<Channel> findUpdatedChannels() {
        return channelData.values().stream()
                .filter(entry -> entry.getUpdatedAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void createChannel(String channelName, User user) {
        Channel channel = new Channel(channelName, user);
        this.channelData.put(channel.getId(), channel);
    }

    @Override
    public void updateChannel(UUID channelId, String channelName) {
        checkById(channelId);
        channelData.get(channelId).updateChannel(channelName);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        checkById(channelId);
        channelData.remove(channelId);
    }

    @Override
    public List<UUID> channelListByuserId(UUID userId) {
        return channelData.values().stream()
                .filter(channel -> channel.getUser().getId().equals(userId))
                .map(Channel::getId)
                .toList();
    }

    @Override
    public void deleteChannelList(List<UUID> channelIdList) {
        channelIdList.forEach(channelData::remove);
    }
}
