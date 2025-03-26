package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageFindDTO;
import com.sprint.mission.discodeit.dto.create.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.dto.create.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public interface MessageService {

    void reset(boolean adminAuth);

    Message create(UUID userId, MessageCreateRequestDTO messageWriteDTO, List<Optional<BinaryContentCreateRequestDTO>> binaryContentDTOs);

    MessageFindDTO find(UUID messageId);

    List<MessageFindDTO> findAllByChannelId(UUID channelId);

    void print(UUID channelId);

    void delete(UUID messageId);

    UUID update(UUID messageId, UpdateMessageDTO updateMessageDTO );

}
