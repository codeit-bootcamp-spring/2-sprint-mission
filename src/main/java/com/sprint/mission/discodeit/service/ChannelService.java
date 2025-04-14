package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
  Channel createPrivateChannel(CreatePrivateChannelRequest request);

  Channel createPublicChannel(CreatePublicChannelRequest request);

  Channel findChannelById(UUID channelId); // 채널 조회

  String findChannelNameById(UUID channelId); //채널 이름 조회

  List<ChannelDto> getAllChannels(); //모든 채널 조회

  List<ChannelDto> findAllByUserId(UUID userId);

  Channel updateChannel(UUID channelId, UpdateChannelRequest request);

  void deleteChannel(UUID channelId); //채널 삭제

  void validateChannelExists(UUID channelId); // 채널 존재 확인

  ChannelDto mapToDto(Channel channel);
}
