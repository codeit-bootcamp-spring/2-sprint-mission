package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public interface ChannelService {

    @Transactional
    ChannelDto create(PublicChannelCreateRequest request);

    @Transactional
    ChannelDto create(PrivateChannelCreateRequest request);

    @Transactional(readOnly = true)
    ChannelDto find(UUID channelId);


    @Transactional
    ChannelDto update(UUID channelId, PublicChannelUpdateRequest request);

    @Transactional
    void delete(UUID channelId);

    @Transactional(readOnly = true)
    List<ChannelDto> findAllByUserId(UUID userId);
}
