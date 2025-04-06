package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Message")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Message 생성", operationId = "create_2")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음"),
            @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(
            @RequestPart("messageCreateRequest") MessageCreateRequest request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
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

        Message message = messageService.create(request, attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Operation(summary = "Message 내용 수정", operationId = "update_2")
    @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨")
    @PatchMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(
            @Parameter(
                    name = "messageId",
                    description = "수정할 Message ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateRequest request) {
        Message updatedMessage = messageService.updateMessage(messageId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }

    @Operation(summary = "Message 삭제", operationId = "delete_1")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
            @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
    })
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessageById(
            @Parameter(
                    name = "messageId",
                    description = "삭제할 Message ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Channel의 Message 목록 조회", operationId = "findAllByChannelId")
    @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<Message>> findAllByChannelId(
            @Parameter(
                    name = "channelId",
                    description = "조회할 Channel ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @RequestParam UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }


}
