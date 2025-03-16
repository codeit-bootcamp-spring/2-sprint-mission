package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Channel.*;

import java.util.List;
import java.util.UUID;


public interface ChannelService {

    void reset(boolean adminAuth);

    UUID create(ChannelCreateDTO channelCreateDTO);

    UUID join(ChannelJoinDTO channelJoinDTO);

    UUID quit(ChannelJoinDTO channelJoinDTO);

    ChannelFindDTO find(String channelId);

    List<ChannelFindDTO> findAllByServerAndUser(String serverId );

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean delete(ChannelIDSDTO channelIDSDTO);

    boolean update(ChannelIDSDTO channelIDSDTO, ChannelUpdateDTO channelUpdateDTO);


}
