package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  MessageDto sendMessage(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments);

  List<MessageDto> findMessageByChannelId(UUID id, Pageable pageable);

  MessageDto updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  void deleteMessageById(UUID id);
}
