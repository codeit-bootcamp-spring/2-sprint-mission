package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.ChannelCreateDto;
import com.sprint.mission.discodeit.service.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.service.dto.ChannelUpdateDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    void createPrivateChannel(ChannelCreateDto createDto);
    void createPublicChannel(ChannelCreateDto createDto);
    void addMembers(UUID id, Set<UUID> userMembers, UUID userId);
    void removeMembers(UUID id, Set<UUID> userMembers, UUID userId);
    ChannelResponseDto findChannelById(UUID id);
    List<ChannelResponseDto> findAllByUserId(UUID userId);
    void updateChannel(ChannelUpdateDto updateDto);
    void deleteChannel(UUID id, UUID userId);
}
