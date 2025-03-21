package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.ChannelRegisterDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto addMember(UUID id, String friendEmail);

    ChannelDto create(ChannelRegisterDto channelRegisterDto);

    ChannelDto findById(UUID id);

    List<ChannelDto> findAll();

    void updateName(UUID id, String name);

    void delete(UUID id);
}
