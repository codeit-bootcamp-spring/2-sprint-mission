package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    Channel createChannel(String channelName); //채널 생성

    Channel findChannelById(UUID channelId); // 채널 조회
    String findChannelNameById(UUID channelId); //채널 이름 조회
    List<Channel> getAllChannels(); //모든 채널 조회

    void updateChannelData();
    void updateChannelName(UUID channelId, String newChannelName); //채널 이름 변경
    void addUser(UUID channelId, UUID userId); //유저 추가
    void addMessage(UUID channelId, UUID messageId);   //메세지 추가

    void deleteChannel(UUID channelId); //채널 삭제
    void removeUser(UUID channelId, UUID userId); //유저 삭제
    void removeMessage(UUID channelId, UUID messageId); //메세지 삭제

    void validateChannelExists(UUID channelId); // 채널 존재 확인
}
