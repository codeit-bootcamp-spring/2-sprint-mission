package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

    Message createMessage(CreateMessageRequest request, List<MultipartFile> attachments);

    MessageResponse getMessageById(UUID messageId);

    List<MessageResponse> findAllByChannelId(UUID channelId);

    void updateMessage(UpdateMessageRequest request);

    void deleteMessage(UUID messageId);
}
