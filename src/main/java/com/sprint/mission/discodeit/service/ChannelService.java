package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Channel.*;

import java.util.List;
import java.util.UUID;


public interface ChannelService {

    void reset(boolean adminAuth);

    UUID create(ChannelDTO channelDTO);

    UUID join(ChannelDTO channelDTO);

    UUID quit(ChannelDTO channelDTO);

    ChannelDTO find(String channelId);

    List<ChannelDTO> findAllByServerAndUser(String serverId );

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean delete(ChannelDTO channelDTO);

    boolean update(ChannelDTO channelDTO, ChannelDTO replaceDTO);


}
