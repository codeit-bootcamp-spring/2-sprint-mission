package com.sprint.mission.discodeit.core.message.usecase;

import org.springframework.stereotype.Service;


@Service
public interface MessageService extends CreateMessageUseCase, FindMessageUseCase,
    UpdateMessageUseCase,
    DeleteMessageUseCase {

}
