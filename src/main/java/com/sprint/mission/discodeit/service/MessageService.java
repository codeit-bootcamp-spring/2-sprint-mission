package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageFindDTO;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface MessageService {

    void reset(boolean adminAuth);

    Message create(CreateMessageRequestDTO messageWriteDTO, List<Optional<CreateBinaryContentRequestDTO>> binaryContentDTOs);

    MessageFindDTO find(String messageId);

    List<MessageFindDTO> findAllByChannelId(String channelId);

    void print(String channelId);

    boolean delete(String messageId);

//    boolean update(String messageId, MessageCRUDDTO messageCRUDDTO);

}
