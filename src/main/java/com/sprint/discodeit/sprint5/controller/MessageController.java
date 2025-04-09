package com.sprint.discodeit.sprint5.controller;

import com.sprint.discodeit.sprint5.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.Message;
import com.sprint.discodeit.sprint5.service.basic.message.BasicMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Message API", description = "메시지 관련 API입니다.")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final BasicMessageService messageService;

    @Operation(summary = "메시지 전송", description = "지정된 채널에 메시지를 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "메시지 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @PostMapping("/channels/{channelId}")
    public ResponseEntity<Message> sendMessage(
            @Parameter(description = "메시지를 보낼 채널의 UUID") @PathVariable String channelId,
            @RequestBody MessageRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.create(UUID.fromString(channelId), requestDto));
    }

    @Operation(summary = "메시지 수정", description = "메시지 내용을 수정합니다.")
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(
            @Parameter(description = "수정할 메시지의 UUID") @PathVariable String messageId,
            @RequestBody MessageUpdateRequestDto updateDto) {
        return ResponseEntity.ok(messageService.update(UUID.fromString(messageId), updateDto));
    }

    @Operation(summary = "메시지 삭제", description = "지정된 메시지를 삭제합니다.")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "삭제할 메시지의 UUID") @PathVariable String messageId) {
        messageService.delete(UUID.fromString(messageId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "채널 메시지 조회", description = "지정된 채널의 메시지를 조회합니다.")
    @GetMapping("/channels/{channelId}")
    public ResponseEntity<List<Message>> getMessagesByChannel(
            @Parameter(description = "메시지를 조회할 채널의 UUID") @PathVariable String channelId) {
        return ResponseEntity.ok(messageService.findChannel(UUID.fromString(channelId)));
    }
}
