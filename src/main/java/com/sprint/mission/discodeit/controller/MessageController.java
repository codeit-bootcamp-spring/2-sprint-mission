package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.SaveMessageParamDto;
import com.sprint.mission.discodeit.dto.UpdateMessageParamDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SaveMessageParamDto saveMessageParamDto) {
        messageService.sendMessage(saveMessageParamDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/update")
    public ResponseEntity<?> update(@ModelAttribute UpdateMessageParamDto updateMessageParamDto) {
        messageService.updateMessage(updateMessageParamDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestParam UUID messageUUID
    ) {
        messageService.deleteMessageById(messageUUID);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-channel-message")
    public ResponseEntity<List<FindMessageByChannelIdResponseDto>> FindChannelMessage(
            @RequestParam UUID channelUUID
    ) {
        List<FindMessageByChannelIdResponseDto> findMessageByChannelIdDtoList = messageService.findMessageByChannelId(channelUUID);
        return ResponseEntity.ok().body(findMessageByChannelIdDtoList);
    }
}
