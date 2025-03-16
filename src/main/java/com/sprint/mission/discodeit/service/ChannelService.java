package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelIDDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelUpdateDTO;

import java.util.UUID;


public interface ChannelService {

    void reset(boolean adminAuth);

    UUID create(ChannelCreateDTO channelCreateDTO);

    UUID join(ChannelIDDTO channelIDDTO);

    UUID quit(ChannelIDDTO channelIDDTO);

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean delete(ChannelIDDTO channelIDDTO);

    boolean update(ChannelIDDTO channelIDDTO, ChannelUpdateDTO channelUpdateDTO);


}
