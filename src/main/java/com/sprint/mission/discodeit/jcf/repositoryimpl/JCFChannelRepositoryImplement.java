package com.sprint.mission.discodeit.jcf.repositoryimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;

import java.util.*;

public class JCFChannelRepositoryImplement implements ChannelRepository {
    // 채널 데이터를 저장할 Map (채널 ID를 키로 사용)
    private final Map<UUID, Channel> channelRepository;
    
    public JCFChannelRepositoryImplement() {
        channelRepository = new HashMap<>();
    }
    
    @Override
    public Channel registerChannel(Channel channel) {
        channelRepository.put(channel.getChannelId(), channel);
        return channel;
    }
    
    @Override
    public Set<UUID> AllChannelUserList() {
        // 방어적 복사를 통해 원본 데이터 보호
        return new HashSet<>(channelRepository.keySet());
    }

    @Override
    public Optional<Channel> findByChannelId(UUID channelId) {
        return Optional.ofNullable(channelRepository.get(channelId));
    }

    @Override
    public Optional<Channel> findByChannelName(String channelName) {
        return channelRepository.values().stream()
                .filter(channel -> channel.getChannelName().equals(channelName))
                .findFirst();
    }
    
    @Override
    public boolean removeChannel(UUID channelId) {
        return channelRepository.remove(channelId) != null;
    }
}
