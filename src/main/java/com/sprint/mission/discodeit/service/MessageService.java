package com.sprint.mission.discodeit.service;

import com.sprint.mission.application.MessageDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(String context);

    MessageDto findById(UUID id);

    List<MessageDto> findAll();

    void updateContextById(UUID id, String context);

    void remove(UUID id);
}
