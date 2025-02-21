package com.sprint.mission.discodeit.repo;

import com.sprint.mission.application.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    ChannelDto create(String name);

    ChannelDto findById(UUID id);

    List<ChannelDto> findAll();

    void updateName(UUID id, String name);

    void delete(UUID id);
}
