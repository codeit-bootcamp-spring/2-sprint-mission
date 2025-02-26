package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto create(String name, UserDto owner);

    ChannelDto findById(UUID id);

    List<ChannelDto> findAll();

    void updateName(UUID id, String name);

    void delete(UUID id);
}
