package com.sprint.mission.discodeit.infra;

import com.sprint.mission.application.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    ChannelDto save(String name);

    ChannelDto findById(UUID id);

    List<ChannelDto> findAll();

    void updateName(UUID id, String name);

    void delete(UUID id);
}
