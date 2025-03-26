package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.Message.CreateMessageDto;
import com.sprint.mission.discodeit.DTO.Message.MessageDto;
import com.sprint.mission.discodeit.DTO.Message.UpdateMessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    //메세지 보내기
    @RequestMapping(method = RequestMethod.POST)
    public MessageDto sendMessage(@RequestBody CreateMessageDto request) {
        return messageService.create(request);
    }

    //메세지 수정
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public MessageDto updateMessage(@PathVariable UUID id, @RequestBody UpdateMessageDto request) {
        request = new UpdateMessageDto(id, request.newContent());
        return messageService.update(request);
    }

    //메세지 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteMessage(@PathVariable UUID id) {
        messageService.delete(id);
    }

    //특정 채널의 메세지 목록 조회
    @RequestMapping(method = RequestMethod.GET, value = "/channel/{channelId}")
    public List<MessageDto> findMessagesByChannel(@PathVariable UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }
}
