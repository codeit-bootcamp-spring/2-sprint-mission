package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
  ChannelDto createPrivateChannel(CreatePrivateChannelRequest request);

  ChannelDto createPublicChannel(CreatePublicChannelRequest request);

  ChannelDto findChannelById(UUID channelId); // 채널 조회

  List<ChannelDto> getAllChannels(); //모든 채널 조회

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto updateChannel(UUID channelId, UpdateChannelRequest request);

  void deleteChannel(UUID channelId); //채널 삭제

  void validateChannelExists(UUID channelId); // 채널 존재 확인
}
