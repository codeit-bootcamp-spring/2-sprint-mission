package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto createMessage(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachmentRequests);

  MessageDto findById(UUID messageId);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, int page);

  //PageResponse<MessageDto> findALLByChannelIdWithCursor(UUID channelId, Instant cursor);

  MessageDto updateMessage(UUID messageId, MessageUpdateRequest request);

  void deleteMessage(UUID messageId);

}
