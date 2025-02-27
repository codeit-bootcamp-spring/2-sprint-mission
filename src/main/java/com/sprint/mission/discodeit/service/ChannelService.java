package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능
    // 어떤 기능이 필요한지 명시
    Channel createChannel(String channelName); //채널 이름으로 방 생성
    void getChannelInfo(String channelName); //채널 이름으로 정보 조회
    void getAllChannelData(); //전체 채팅방 정보 조회
    void updateChannelName(String oldChannelName, String newChannelName);
    void deleteChannelName(String channelName);
    Channel getChannelById(UUID channelId);
}
