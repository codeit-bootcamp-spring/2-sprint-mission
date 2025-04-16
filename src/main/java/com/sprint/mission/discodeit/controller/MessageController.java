package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Message 생성")
    @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "User or Channel not found")))
    @ApiResponse(responseCode = "200", description = "Message가 성공적으로 성생됨")
    public ResponseEntity<MessageResponseDto> createMessage(
            @RequestPart("messageCreateRequest") MessageCreateDto messageCreateRequest,
            @RequestPart(value = "attachments", required = false) @Parameter(description = "Message 첨부 파일들") List<MultipartFile> attachments

    ) {

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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        MessageResponseDto createMessage = messageService.create(messageCreateRequest, contentCreate);
        return ResponseEntity.ok(createMessage);
    }


    @PatchMapping("/{messageId}")
    @Operation(summary = "Message 내용 수정")
    @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨")
    @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "Message does not found")))
    public ResponseEntity<MessageResponseDto> updateMessage(
            @PathVariable @Parameter(description = "수정 할 Message ID") UUID messageId,
            @RequestBody MessageUpdateDto messageUpdateRequest
    ) {
        MessageResponseDto updateMessage = messageService.update(messageId, messageUpdateRequest);
        return ResponseEntity.ok(updateMessage);
    }


    @DeleteMapping("/{messageId}")
    @Operation(summary = "Message 삭제")
    @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨")
    @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "Message does not found")))
    public ResponseEntity<Message> deleteMessage(
            @PathVariable @Parameter(description = "삭제할 Message ID") UUID messageId
    ) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/find/{messageId}")
    public ResponseEntity<MessageResponseDto> findMessages(
            @PathVariable @Parameter UUID messageId
    ) {
        MessageResponseDto MessageFindResponse = messageService.find(messageId);
        return ResponseEntity.ok(MessageFindResponse);
    }


    @GetMapping
    @Operation(summary = "Channel의 Message 목록 조회")
    @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
    public ResponseEntity<List<MessageResponseDto>> findMessagesByChannelId(
            @RequestParam @Parameter(description = "조회할 Channel ID") UUID channelId
    ) {
        List<MessageResponseDto> messageFindByChannelResponse =
                messageService.findAllByChannelId(channelId);

        return ResponseEntity.ok(messageFindByChannelResponse);
    }


}
