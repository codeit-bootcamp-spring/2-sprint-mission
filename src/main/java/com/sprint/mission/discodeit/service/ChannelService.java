package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Channel.*;

import java.util.List;
import java.util.UUID;


public interface ChannelService {

    void reset(boolean adminAuth);

    UUID create(ChannelCRUDDTO channelCRUDDTO);

    UUID join(ChannelCRUDDTO channelCRUDDTO);

    UUID quit(ChannelCRUDDTO channelCRUDDTO);

    ChannelDTO find(String channelId);

    List<ChannelDTO> findAllByServerAndUser(String serverId );

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean delete(ChannelCRUDDTO channelCRUDDTO);

    boolean update(ChannelCRUDDTO channelCRUDDTO, ChannelCRUDDTO channelUpdateDTO);


}
