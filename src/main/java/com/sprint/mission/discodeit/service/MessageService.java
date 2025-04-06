package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  Message create(MessageCreateRequest messageCreateRequest, List<MultipartFile> attachments);

  Message find(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  Message update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
