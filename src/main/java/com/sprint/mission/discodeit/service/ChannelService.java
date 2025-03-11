package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    // 채널 생성
    void createChannel(String channelName, UUID userId);
    
    // 채널 이름 변경
    void setChannelName(UUID channelId, String newChannelName, UUID userId);
    String getChannelName(UUID channelId);
    // 전체 채널 목록 조회
    Set<UUID> findByAllChannel();
    // 선택한 채널의 유저목록 조회
    Set<UUID> getChannelUserList(UUID channelId);
    // 채널 유저 가입
    void joinChannel(UUID channelId, UUID userId);
    
    // 채널에서 유저 탈퇴
    void leaveChannel(UUID channelId, UUID userId);
}

