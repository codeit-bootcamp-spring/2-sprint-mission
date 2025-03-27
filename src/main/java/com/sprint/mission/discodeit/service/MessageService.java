package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDTO create(CreateMessageParam createMessageParam, List<MultipartFile> multipartFiles);
    MessageDTO find(UUID id);
    List<MessageDTO> findAllByChannelId(UUID channelId);
    UpdateMessageDTO update(UUID id, UpdateMessageParam updateMessageParam, List<MultipartFile> multipartFiles);
    void delete(UUID id);
}
