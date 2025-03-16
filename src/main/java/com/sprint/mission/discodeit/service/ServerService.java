package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelIDDTO;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public interface ServerService {

    void reset(boolean adminAuth);

    UUID createChannel(ChannelCreateDTO channelCreateDTO);

    UUID joinChannel(ChannelIDDTO channelIDDTO, String ownerId);

    UUID quitChannel(ChannelIDDTO channelIDDTO);

    boolean printUsers(String serverId);

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean removeChannel(ChannelIDDTO channelIDDTO);

    boolean updateChannelName(ChannelIDDTO channelIDDTO, String replaceName);
}
