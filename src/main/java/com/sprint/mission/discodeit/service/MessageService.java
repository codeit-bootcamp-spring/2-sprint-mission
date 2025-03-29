package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Qualifier("BasicMessageService")
public interface MessageService {

    MessageDto.Response create(MessageDto.Create messageCreateDTO) throws IOException;
    MessageDto.Response findByMessage(UUID messageId);
    List<MessageDto.Response> findAllMessage();
    List<MessageDto.Response> findAllByChannelId(UUID channelId);
    MessageDto.Response updateMessage(MessageDto.Update messageUpdateDTO) throws IOException;
    void deleteMessage(UUID messageId);
}
