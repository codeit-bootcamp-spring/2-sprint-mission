package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FindMessageUseCase {

//  MessageResult find(UUID messageId, Pageable pageable);

//  Slice<MessageResult> findMessagesByChannelId(UUID channelId, Pageable pageable);

  Slice<MessageResult> findMessagesByChannelId(UUID channelId, Instant cursor, Pageable pageable);

}
