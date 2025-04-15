package com.sprint.discodeit.sprint.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;
import com.sprint.discodeit.sprint.domain.dto.PaginatedResponse;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelMessageResponseDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.CursorPaginatedResponse;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageRequestDto;
import com.sprint.discodeit.sprint.domain.dto.messageDto.MessageUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Message;
import com.sprint.discodeit.sprint.service.basic.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Message API", description = "메시지 관련 API입니다.")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "메시지 전송", description = "지정된 채널에 메시지를 전송합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "메시지 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")})
    @PostMapping("/channels/{channelId}")
    public ResponseEntity<Message> sendMessage(
            @Parameter(description = "메시지를 보낼 채널의 UUID") @PathVariable Long channelId,
            @RequestBody MessageRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(channelId, requestDto));
    }

    @Operation(summary = "메시지 수정", description = "메시지 내용을 수정합니다.")
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@Parameter(description = "수정할 메시지의 UUID") @PathVariable Long messageId,
                                                 @RequestBody MessageUpdateRequestDto updateDto) {
        return ResponseEntity.ok(messageService.update(messageId, updateDto));
    }

    @Operation(summary = "메시지 삭제", description = "지정된 메시지를 삭제합니다.")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@Parameter(description = "삭제할 메시지의 UUID") @PathVariable Long messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/channels/{channelId}/messages")
    public ResponseEntity<PaginatedResponse<ChannelMessageResponseDto>> getMessages(@PathVariable Long channelId,
                                                                                    @PageableDefault(size = 50, sort = "createdAt", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(messageService.findChannel(channelId, pageable));
    }

    @GetMapping("/channels/{channelId}/messages/cursor")
    public ResponseEntity<CursorPaginatedResponse<ChannelMessageResponseDto>> getMessagesWithCursor(
            @PathVariable Long channelId,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "50") int size)

    {
        return ResponseEntity.ok(messageService.findByChannelCursor(channelId, lastMessageId, size)
        );
    }

}
