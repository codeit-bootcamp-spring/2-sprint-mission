package com.sprint.mission.discodeit.infra;

import com.sprint.mission.application.MessageDto;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    MessageDto save(String context);

    MessageDto findById(UUID id);

    List<MessageDto> findAll();

    void updateContext(UUID id, String context);

    void delete(UUID id);
}
