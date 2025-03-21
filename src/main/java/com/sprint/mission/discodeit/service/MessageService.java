package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.RequestToService.MessageWriteDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.legacy.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface MessageService {

    void reset(boolean adminAuth);

    Message create(MessageWriteDTO messageWriteDTO,List<Optional<BinaryContentCreateDTO>> binaryContentDTOs);

    Message find(String messageId);

    List<Message> findAllByChannelId(String channelId);

    void print(String channelId);


    boolean delete(String messageId);

    boolean update(String messageId, MessageCRUDDTO messageCRUDDTO);

}
