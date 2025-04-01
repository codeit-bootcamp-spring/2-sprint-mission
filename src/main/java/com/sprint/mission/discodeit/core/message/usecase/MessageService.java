package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageFindDTO;
import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.UpdateMessageDTO;
import com.sprint.mission.discodeit.core.message.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public interface MessageService {

  void reset(boolean adminAuth);

  Message create(UUID userId, UUID channelId, MessageCreateRequestDTO messageWriteDTO,
      List<Optional<BinaryContentCreateRequestDTO>> binaryContentDTOs);

  MessageFindDTO find(UUID messageId);

  List<MessageFindDTO> findAllByChannelId(UUID channelId);

  UUID update(UUID messageId, UpdateMessageDTO updateMessageDTO);

  void delete(UUID messageId);

//    void print(UUID channelId);

}
