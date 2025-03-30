package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BaseResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResponseDto> createMessage(@RequestBody MessageCreateDto messageCreateDto) {
        Message message = messageService.create(messageCreateDto);
        return ResponseEntity.ok(BaseResponseDto.success(message.getId() + " 메시지 등록이 완료되었습니다."));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponseDto> updateMessage(@RequestBody MessageUpdateDto messageUpdateDto) {
        Message message = messageService.update(messageUpdateDto);
        return ResponseEntity.ok(BaseResponseDto.success(message.getId() + " 메시지 변경이 완료되었습니다."));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponseDto> deleteMessage(@PathVariable("id") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok(BaseResponseDto.success(messageId + " 메시지 삭제가 완료되었습니다."));
    }

    @RequestMapping(value = "/channel/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessages(@PathVariable("id") UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}
