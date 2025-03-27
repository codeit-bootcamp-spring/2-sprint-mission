package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.message.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public Message createMessage(@RequestBody CreateMessageDTO dto, List<BinaryContentDTO> attachments) {
        return messageService.createMessage(dto, attachments != null ? attachments : List.of());
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public Message updateMessage(@PathVariable UUID messageId, @RequestBody UpdateMessageDTO dto) {
        return messageService.updateMessage(messageId, dto);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public List<Message> getMessagesByChannel(@PathVariable UUID channelId) {
        return messageService.searchAllByChannelId(channelId);
    }


}
