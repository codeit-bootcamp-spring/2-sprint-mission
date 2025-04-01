package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.dto.ChannelCreateDto;
import com.sprint.mission.discodeit.service.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.service.dto.ChannelUpdateDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivateChannel(ChannelCreateDto createDto);
    Channel createPublicChannel(ChannelCreateDto createDto);
    ChannelResponseDto findChannelById(UUID id);
    List<ChannelResponseDto> findAllByUserId(UUID userId);
    Channel updateChannel(ChannelUpdateDto updateDto);
    void deleteChannel(UUID id, UUID userId);

//    void addMembers(UUID id, Set<UUID> userMembers, UUID userId);
//    void removeMembers(UUID id, Set<UUID> userMembers, UUID userId);
}
