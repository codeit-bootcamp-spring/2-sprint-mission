package com.sprint.mission.discodeit.core.channel.usecase;


import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PrivateChannelCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PublicChannelCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.UpdateChannelDTO;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface ChannelService {

  void reset(boolean adminAuth);

  Channel create(UUID userId, UUID serverId, PublicChannelCreateRequestDTO requestDTO);

  Channel create(UUID userId, UUID serverId, PrivateChannelCreateRequestDTO requestDTO);

  void join(UUID channelId, UUID userId);

  void quit(UUID channelId, UUID userId);

  ChannelFindDTO find(UUID channelId);

  List<ChannelFindDTO> findAllByUserId(UUID userID);

  UUID update(UUID channelId, UpdateChannelDTO requestDTO);

  void delete(UUID channelId);

//    void printChannels(UUID serverId);

//    void printUsersInChannel(UUID channelId);


}
