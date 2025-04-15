package com.sprint.mission.discodeit.core.message.usecase;

import org.springframework.stereotype.Service;

//TODO. service 반환값 수정해야함

@Service
public interface MessageService extends CreateMessageUseCase, FindMessageUseCase,
    UpdateMessageUseCase,
    DeleteMessageUseCase {

}
