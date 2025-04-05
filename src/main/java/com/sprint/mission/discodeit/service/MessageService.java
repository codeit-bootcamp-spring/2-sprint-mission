package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message createMessage(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachmentRequests);

  Message findById(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  Message updateMessage(UUID messageId, MessageUpdateRequest request);

  void deleteMessage(UUID messageId);

}
