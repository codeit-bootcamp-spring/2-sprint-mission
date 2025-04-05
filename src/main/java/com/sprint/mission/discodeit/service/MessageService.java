package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Qualifier("BasicMessageService")
public interface MessageService {

  MessageDto.Response create(MessageDto.Create messageCreateDTO, UUID uuid) throws IOException;

  Optional<ZonedDateTime> findMessageByChannelId(UUID channelId);

  MessageDto.Response findByMessage(UUID messageId);

  List<MessageDto.Response> findAllMessage();

  List<MessageDto.Response> findAllByChannelId(UUID channelId);

  MessageDto.Response updateMessage(UUID messageId, MessageDto.Update messageUpdateDTO, UUID uuid)
      throws IOException;

  void deleteMessage(UUID messageId);
}
