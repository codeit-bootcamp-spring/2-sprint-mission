package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelIDSDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelUpdateDTO;

import java.util.UUID;


public interface ChannelService {

    void reset(boolean adminAuth);

    UUID create(ChannelCreateDTO channelCreateDTO);

    UUID join(ChannelIDSDTO channelIDSDTO);

    UUID quit(ChannelIDSDTO channelIDSDTO);

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean delete(ChannelIDSDTO channelIDSDTO);

    boolean update(ChannelIDSDTO channelIDSDTO, ChannelUpdateDTO channelUpdateDTO);


}
