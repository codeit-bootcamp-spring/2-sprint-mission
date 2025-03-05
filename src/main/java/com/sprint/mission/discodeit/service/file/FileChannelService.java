package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    @Override
    public ChannelDto addMember(UUID id, String friendEmail) {
        return null;
    }

    @Override
    public ChannelDto create(String name, UserDto owner) {
        return null;
    }

    @Override
    public ChannelDto findById(UUID id) {
        return null;
    }

    @Override
    public List<ChannelDto> findAll() {
        return null;
    }

    @Override
    public void updateName(UUID id, String name) {

    }

    @Override
    public void delete(UUID id) {

    }
}
