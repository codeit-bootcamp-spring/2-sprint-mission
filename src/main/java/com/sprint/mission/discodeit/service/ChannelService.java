package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Request.ChannelCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.ChannelJoinQuitDTO;
import com.sprint.mission.discodeit.DTO.legacy.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.DTO.legacy.Channel.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ChannelService {

    void reset(boolean adminAuth);

    Channel create(ChannelCreateDTO channelCreateDTO);

    User join(ChannelJoinQuitDTO channelJoinQuitDTO);

    User quit(ChannelJoinQuitDTO channelJoinQuitDTO);

    ChannelDTO find(String channelId);

    List<ChannelDTO> findAllByServerAndUser(String serverId );

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean delete(String channelId);

    boolean update(ChannelCRUDDTO channelCRUDDTO, ChannelCRUDDTO channelUpdateDTO);

}
