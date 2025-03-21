package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.UserFindDTO;
import com.sprint.mission.discodeit.dto.request.CreateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.request.JoinQuitChannelRequestDTO;
import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface ChannelService {

    void reset(boolean adminAuth);

    UUID create(CreateChannelRequestDTO channelCreateDTO);

    UserFindDTO join(JoinQuitChannelRequestDTO channelJoinQuitDTO);

    UserFindDTO quit(JoinQuitChannelRequestDTO channelJoinQuitDTO);

    ChannelFindDTO find(String channelId);

    List<ChannelFindDTO> findAllByServerAndUser(String serverId );

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean delete(String channelId);

//    boolean update(ChannelCRUDDTO channelCRUDDTO, ChannelCRUDDTO channelUpdateDTO);

}
