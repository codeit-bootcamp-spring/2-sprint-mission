package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public interface ChannelService {

    ChannelDto create(PublicChannelCreateRequest request);

    ChannelDto create(PrivateChannelCreateRequest request);

    ChannelDto find(UUID channelId);

    ChannelDto update(UUID channelId, PublicChannelUpdateRequest request);

    void delete(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);
}
