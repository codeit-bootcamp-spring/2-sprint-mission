package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelFindDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.CreatePublicChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    Channel createPrivateChannel(CreatePrivateChannelDto dto);

    Channel createPublicChannel(CreatePublicChannelDto dto);

    ChannelFindDto findChannelById(UUID channelId); // 채널 조회

    String findChannelNameById(UUID channelId); //채널 이름 조회

    List<ChannelFindDto> getAllChannels(); //모든 채널 조회

    List<ChannelFindDto> findAllByUserId(UUID userId);

    void updateChannel(ChannelUpdateDto dto);

    void addUser(UUID channelId, UUID userId); //유저 추가

    void addMessage(UUID channelId, UUID messageId);   //메세지 추가

    void deleteChannel(UUID channelId); //채널 삭제

    void removeUser(UUID channelId, UUID userId); //유저 삭제

    void removeMessage(UUID channelId, UUID messageId); //메세지 삭제

    void validateChannelExists(UUID channelId); // 채널 존재 확인
}
