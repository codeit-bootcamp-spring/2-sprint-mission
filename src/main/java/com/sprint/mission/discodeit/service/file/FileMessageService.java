package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    @Override
    public MessageDto create(String context, UUID channelId, UUID userId) {
        return null;
    }

    @Override
    public MessageDto findById(UUID id) {
        return null;
    }

    @Override
    public List<MessageDto> findAll() {
        return null;
    }

    @Override
    public List<MessageDto> findByChannelId(UUID channelId) {
        return null;
    }

    @Override
    public void updateContext(UUID id, String context) {

    }

    @Override
    public void delete(UUID id) {

    }
}
