package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto);

    Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto);

    ChannelResponseDto findById(UUID channelId);

    Channel update(UUID channelId, String newName, String newDescription);
    List<ChannelResponseDto> findAllByUserId(UUID userId);


    void delete(UUID channelId);
}