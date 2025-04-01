package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageFindDTO;
import java.util.List;
import java.util.UUID;

public interface FindMessageUseCase {

  MessageFindDTO find(UUID messageId);

  List<MessageFindDTO> findAllByChannelId(UUID channelId);

}
