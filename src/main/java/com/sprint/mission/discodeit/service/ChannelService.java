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

    UUID create(UUID userId, CreateChannelRequestDTO channelCreateDTO);

    void join(UUID channelId, UUID userId);

    void quit( UUID channelId, UUID userId);

    ChannelFindDTO find(UUID channelId);

    List<ChannelFindDTO> findAllByServerAndUser(UUID serverId );

    UUID update(UUID channelId,UpdateChannelDTO updateChannelDTO);

    void delete(UUID channelId);

    void printChannels(UUID serverId);

    void printUsersInChannel(UUID channelId);


}
