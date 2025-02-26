package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    void createChannel(String channelName); //채널 생성

    Channel getChannelById(UUID channelId); // 채널 조회
    String getChannelNameById(UUID channelId); //채널 이름 조회
    List<Channel> getAllChannels(); //모든 채널 조회

    void updateChannelName(UUID channelId, String newChannelName); //채널 이름 변경
    void addUserToChannel(UUID channelId, UUID userId); //유저 추가
    void addMessageToChannel(UUID channelId, UUID messageId);   //메세지 추가

    void removeChannel(UUID channelId); //채널 삭제
    void removeUserFromChannel(UUID channelId, UUID userId); //유저 삭제
    void removeMessageFromChannel(UUID channelId, UUID messageId); //메세지 삭제

    void validateChannelExists(UUID channelId); // 채널 존재 확인
}
