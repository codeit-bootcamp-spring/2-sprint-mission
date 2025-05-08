package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Message 생성")
    @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "User or Channel not found")))
    @ApiResponse(responseCode = "200", description = "Message가 성공적으로 성생됨")
    public ResponseEntity<MessageResponseDto> createMessage(
            @Valid @RequestPart("messageCreateRequest") MessageCreateDto messageCreateRequest,
            @RequestPart(value = "attachments", required = false) @Parameter(description = "Message 첨부 파일들") List<MultipartFile> attachments

    ) {
        logger.debug("[Message Controller][createMessage] Received messageCreateRequest");
        logger.debug("[Message Controller][createMessage] Starting profile upload process: filename={}", attachments);
        List<BinaryContentCreateDto> contentCreate = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                try {
                    BinaryContentCreateDto content = new BinaryContentCreateDto(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                    );
                    contentCreate.add(content);
                    logger.debug("[Message Controller][createMessage] BinaryContentCreateDto constructed");
                } catch (IOException e) {
                    logger.error("[Message Controller][createMessage] Exception occurred while uploading profile image", e);
                    throw new RuntimeException(e);
                }
            }
        }
        logger.debug("[Message Controller][createUser] Calling messageService.create()");
        MessageResponseDto createMessage = messageService.create(messageCreateRequest, contentCreate);
        logger.info("[Message Controller][createUser] Created successfully: userId={}", createMessage.id());
        return ResponseEntity.ok(createMessage);
    }


    @PatchMapping("/{messageId}")
    @Operation(summary = "Message 내용 수정")
    @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨")
    @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "Message does not found")))
    public ResponseEntity<MessageResponseDto> updateMessage(
            @PathVariable @Parameter(description = "수정 할 Message ID") UUID messageId,
            @Valid @RequestBody MessageUpdateDto messageUpdateRequest
    ) {
        logger.debug("[Message Controller][updateMessage] Received messageUpdateRequest: messageId={}", messageId);
        logger.debug("[Message Controller][updateMessage] Calling messageService.update()");
        MessageResponseDto updateMessage = messageService.update(messageId, messageUpdateRequest);
        logger.info("[Message Controller][updateMessage] Updated successfully: messageId={}", messageId);
        return ResponseEntity.ok(updateMessage);
    }


    @DeleteMapping("/{messageId}")
    @Operation(summary = "Message 삭제")
    @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨")
    @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "Message does not found")))
    public ResponseEntity<Message> deleteMessage(
            @PathVariable @Parameter(description = "삭제할 Message ID") UUID messageId
    ) {
        logger.debug("[Message Controller][deleteMessage] Received delete request: messageId={}", messageId);
        logger.debug("[Message Controller][deleteMessage] Calling messageService.delete()");
        messageService.delete(messageId);
        logger.info("[Message Controller][deleteMessage] Deleted successfully: messageId={}", messageId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/find/{messageId}")
    public ResponseEntity<PageResponseDto<MessageResponseDto>> findMessages(
            @PathVariable @Parameter UUID messageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        PageResponseDto<MessageResponseDto> MessageFindResponse = messageService.find(messageId, page, size);
        return ResponseEntity.ok(MessageFindResponse);
    }


    @GetMapping
    @Operation(summary = "Channel의 Message 목록 조회")
    @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
    public ResponseEntity<PageResponseDto<MessageResponseDto>> findMessagesByChannelId(
            @RequestParam @Parameter(description = "조회할 Channel ID") UUID channelId,
            @RequestParam(required = false) Instant cursor,
            @ParameterObject Pageable pageable

    ) {
        PageResponseDto<MessageResponseDto> messageFindByChannelResponse =
                messageService.findAllByChannelId(channelId, cursor, pageable);
        return ResponseEntity.ok(messageFindByChannelResponse);
    }


}
