package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ChannelService {

    @Transactional
    ChannelDto create(PublicChannelCreateRequest request);

    @Transactional
    ChannelDto create(PrivateChannelCreateRequest request);

    @Transactional(readOnly = true)
    ChannelDto find(UUID channelId);


    @Transactional(readOnly = true)
    PageResponse<ChannelDto> findAllByUserId(UUID userId, Pageable pageable);

    @Transactional
    Channel update(UUID channelId, PublicChannelUpdateRequest request);

    @Transactional
    void delete(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);
}
