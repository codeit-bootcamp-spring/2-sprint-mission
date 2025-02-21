package com.sprint.mission.discodeit.repo;

import com.sprint.mission.application.MessageDto;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    MessageDto create(String context);

    MessageDto findById(UUID id);

    List<MessageDto> findAll();

    void updateContext(UUID id, String context);

    void delete(UUID id);
}
