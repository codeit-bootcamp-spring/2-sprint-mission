package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;

import java.util.*;


public class BasicChannelRepositoryImplement implements ChannelRepository {
    // 채널 데이터를 저장할 Map (채널 ID를 키로 사용)
    private final Map<UUID, Channel> channelRepository;
    
    // 싱글톤 인스턴스
    private static BasicChannelRepositoryImplement instance;
    
    // private 생성자로 변경
    private BasicChannelRepositoryImplement() {
        channelRepository = new HashMap<>();
    }
    
    // 싱글톤 인스턴스를 반환하는 정적 메소드
    public static synchronized BasicChannelRepositoryImplement getInstance() {
        if (instance == null) {
            instance = new BasicChannelRepositoryImplement();
        }
        return instance;
    }

    @Override
    public Channel register(Channel channel) {
        channelRepository.put(channel.getChannelId(), channel);
        return channel;
    }

    @Override
    public Set<UUID> allChannelIdList() {
        // 방어적 복사를 통해 원본 데이터 보호
        return new HashSet<>(channelRepository.keySet());
    }
    @Override
    public Optional<Channel> findChannelById(UUID channelId) {
        return Optional.ofNullable(channelRepository.get(channelId));
    }

    // ID로 채널 이름 조회
    @Override
    public Optional<String> findChannelNameById(UUID channelId) {
        return findChannelById(channelId)
                .map(Channel::getChannelName);
    }

    // 이름으로 채널 조회
    @Override
    public Optional<Channel> findChannelByName(String channelName) {
        return channelRepository.values().stream()
                .filter(channel -> channel.getChannelName().equals(channelName))
                .findFirst();
    }

    @Override
    public boolean deleteChannel(UUID channelId) {
        return channelRepository.remove(channelId) != null;
    }

    @Override
    public boolean updateChannel(Channel channel) {
        return  channelRepository.put(channel.getChannelId(),channel)!=null;
    }
}