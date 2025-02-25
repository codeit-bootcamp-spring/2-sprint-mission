package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String name); // 채널 생성
    Optional<Channel> getChannelById(UUID channelId); // 특정 채널 조회
    List<Channel> getChannelsByName(String name); // 이름으로 채널 조회
    List<Channel> getAllChannels(); // 모든 채널 조회
    void updateChannel(UUID channelId, String newName); // 채널 이름 수정
    void deleteChannel(UUID channelId); // 채널 삭제
}
