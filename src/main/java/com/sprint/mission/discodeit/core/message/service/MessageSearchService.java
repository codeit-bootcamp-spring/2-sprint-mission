package com.sprint.mission.discodeit.core.message.service;

import com.sprint.mission.discodeit.core.message.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.dto.PageResponse;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSearchService {

  private final JpaMessageRepository messageRepository;

  @Cacheable("messages")
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    PageResponse<Message> result = messageRepository.findAllByChannelIdWithAuthor(
        channelId,
        Optional.ofNullable(cursor).orElse(Instant.now()), pageable);

    List<MessageDto> messageDtoList = result.content().stream().map(MessageDto::from).toList();

    return new PageResponse<>(messageDtoList
        , result.nextCursor(), result.size(), result.hasNext(), result.totalElements());
  }
}
