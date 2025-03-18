package com.sprint.discodeit.service;

import com.sprint.discodeit.domain.dto.channelDto.ChannelCreateRequestDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.domain.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelServiceV1 {
    ChannelResponseDto create(ChannelCreateRequestDto channelCreateRequestDto);
    Channel update(ChannelUpdateRequestDto channelUpdateRequestDto);
    void delete(UUID channelId);
}
