package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  MessageResult create(MessageCreationRequest messageCreationRequest,
      List<MultipartFile> files);

  MessageResult getById(UUID id);

  List<MessageResult> getAllByChannelId(UUID channelId);

  MessageResult updateContext(UUID id, String context);

  void delete(UUID id);
}
