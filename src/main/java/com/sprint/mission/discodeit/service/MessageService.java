package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  Message sendMessage(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments);

  List<Message> findMessageByChannelId(UUID id);

  Message updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  void deleteMessageById(UUID id);
}
