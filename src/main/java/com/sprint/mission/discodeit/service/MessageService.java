package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.message.CreateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageResult;
import com.sprint.mission.discodeit.dto.service.message.FindMessageResult;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageResult;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

public interface MessageService {

  CreateMessageResult create(CreateMessageCommand createMessageCommand,
      List<MultipartFile> multipartFiles);

  FindMessageResult find(UUID id);

  List<FindMessageResult> findAllByChannelId(UUID channelId);

  UpdateMessageResult update(UUID id, UpdateMessageCommand updateMessageCommand,
      List<MultipartFile> multipartFiles);

  void delete(UUID id);
}
