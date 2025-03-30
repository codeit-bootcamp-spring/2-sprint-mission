package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<Void>> send(@RequestBody SaveMessageParamDto saveMessageParamDto) {
        messageService.sendMessage(saveMessageParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/update")
    public ResponseEntity<ApiResponse<Void>> update(@ModelAttribute UpdateMessageParamDto updateMessageParamDto) {
        messageService.updateMessage(updateMessageParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestParam UUID messageUUID
    ) {
        messageService.deleteMessageById(messageUUID);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/find-channel-message")
    public ResponseEntity<ApiResponse<List<FindMessageByChannelIdResponseDto>>> FindChannelMessage(
            @RequestParam UUID channelUUID
    ) {
        List<FindMessageByChannelIdResponseDto> findMessageByChannelIdDtoList = messageService.findMessageByChannelId(channelUUID);
        return ResponseEntity.ok(ApiResponse.success(findMessageByChannelIdDtoList));
    }
}
