package com.sprint.mission.discodeit.core.message.usecase.crud;

import org.springframework.stereotype.Service;


@Service
public interface MessageService extends CreateMessageUseCase, FindMessageUseCase,
    UpdateMessageUseCase, DeleteMessageUseCase {

  void reset(boolean adminAuth);

//    void print(UUID channelId);

}
