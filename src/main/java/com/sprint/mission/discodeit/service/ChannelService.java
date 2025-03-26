package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.create.CreateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface ChannelService {

    void reset(boolean adminAuth);

    UUID create(CreateChannelRequestDTO channelCreateDTO);

//    UserFindDTO join(JoinQuitChannelRequestDTO channelJoinQuitDTO);
//
//    UserFindDTO quit(JoinQuitChannelRequestDTO channelJoinQuitDTO);

    ChannelFindDTO find(UUID channelId);

    List<ChannelFindDTO> findAllByServerAndUser(UUID serverId );

    void printChannels(UUID serverId);

    void printUsersInChannel(UUID channelId);

    void delete(UUID channelId);

    UUID update(UUID channelId, UpdateChannelDTO updateChannelDTO);

}
