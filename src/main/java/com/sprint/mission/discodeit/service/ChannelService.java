package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelInfoDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
  Channel createPrivateChannel(CreatePrivateChannelRequest request);

  Channel createPublicChannel(CreatePublicChannelRequest request);

  Channel findChannelById(UUID channelId); // 채널 조회

  String findChannelNameById(UUID channelId); //채널 이름 조회

  List<ChannelInfoDto> getAllChannels(); //모든 채널 조회

  List<ChannelInfoDto> findAllByUserId(UUID userId);

  void updateChannel(UUID channelId, UpdateChannelRequest request);

  void deleteChannel(UUID channelId); //채널 삭제

  void validateChannelExists(UUID channelId); // 채널 존재 확인

  ChannelInfoDto mapToDto(Channel channel);
}
