package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublic(CreatePublicChannelDto requestDto);
    Channel createPrivate(CreatePrivateChannelDto requestDto);
    ReadChannelResponseDto read(ReadChannelRequestDto requestDto);
    List<ReadChannelResponseDto> readAllByUserKey(UUID userKey);
    UpdateChannelDto update(UpdateChannelDto requestDto);
    void delete(UUID channelKey);
}
