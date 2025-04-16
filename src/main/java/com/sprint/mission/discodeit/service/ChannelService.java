package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public interface ChannelService {

    @Transactional
    Channel create(PublicChannelCreateRequest request);

    @Transactional
    Channel create(PrivateChannelCreateRequest request);

    @Transactional(readOnly = true)
    ChannelDto find(UUID channelId);

    @Transactional(readOnly = true)
    List<ChannelDto> findAllByUserId(UUID userId);

    @Transactional
    Channel update(UUID channelId, PublicChannelUpdateRequest request);

    @Transactional
    void delete(UUID channelId);
}
