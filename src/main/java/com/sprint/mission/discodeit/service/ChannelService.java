package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.legacy.request.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.requestToService.ChannelJoinQuitDTO;
import com.sprint.mission.discodeit.dto.legacy.channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.dto.legacy.channel.ChannelDTO;
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
