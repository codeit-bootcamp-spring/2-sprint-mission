package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
@Tag(name = "Message", description = "메시지 괸리 API")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "메시지 생성", description = "첨부 파일을 포함한 메시지 생성")
    @ApiResponse(
            responseCode = "201",
            description = "메시지 생성 성공",
            content = @Content(schema = @Schema(implementation = Message.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(examples = @ExampleObject(value = "Invalid request data"))
    )
    @ApiResponse(
            responseCode = "404",
            description = "채널 또는 사용자 없음",
            content = @Content(examples = @ExampleObject(value = "Channel/User not found"))
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(
            @Parameter(
                    description = "메시지 생성 요청 데이터 (JSON)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MessageCreateRequest.class))
            )
            @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,

            @Parameter(
                    description = "첨부 파일 목록",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
                .map(files -> files.stream()
                        .map(file -> {
                            try {
                                return new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList())
                .orElse(new ArrayList<>());
        Message createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMessage);
    }

    @Operation(summary = "메시지 수정", description = "메시지 내용 업데이트")
    @ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(schema = @Schema(implementation = Message.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "메시지 없음",
            content = @Content(examples = @ExampleObject(value = "Message with ID {messageId} not found"))
    )
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> update(
            @Parameter(description = "수정할 메시지 ID", required = true)
            @PathVariable("messageId") UUID messageId,

            @Parameter(description = "메시지 수정 요청 데이터", required = true)
            @RequestBody MessageUpdateRequest request) {
        Message updatedMessage = messageService.update(messageId, request);
        return ResponseEntity.ok(updatedMessage);
    }

    @Operation(summary = "메시지 삭제")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @ApiResponse(
            responseCode = "404",
            description = "메시지 없음",
            content = @Content(examples = @ExampleObject(value = "Message with ID {messageId} not found"))
    )
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 메시지 Id", required = true)
            @PathVariable("messageId") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "채널별 메시지 조회", description = "특정 채널의 모든 메시지 조회")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(type = "array", implementation = Message.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "채널 없음",
            content = @Content(examples = @ExampleObject(value = "Channel with ID {channelId} not found"))
    )
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> findAllByChannelId(
            @Parameter(description = "조회할 채널 ID", required = true)
            @PathVariable("channelId") UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }
}
