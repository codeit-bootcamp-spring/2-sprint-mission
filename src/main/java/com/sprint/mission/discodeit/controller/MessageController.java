package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.MultipartToBinaryConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Message 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> createMessage(
            @RequestPart("messageCreateRequest") MessageCreateDto messageCreateDto,
            @RequestPart(name = "attachments", required = false) List<MultipartFile> files) {
        log.info("Received message create request: {}", messageCreateDto);
        List<BinaryContentCreateDto> binaryDtos = MultipartToBinaryConverter.toBinaryContentCreateDtos(files);
        MessageDto messageDto = messageService.create(messageCreateDto, binaryDtos);
        log.info("Message created successfully: messageId={}", messageDto.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    }

    @Operation(summary = "Message 내용 수정")
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
                                                    @RequestBody MessageUpdateDto messageUpdateDto) {
        log.info("Received message update request: messageId={}, updateDto={}", messageId, messageUpdateDto);
        MessageDto messageDto = messageService.update(messageId, messageUpdateDto);
        log.info("Message updated successfully: messageId={}", messageDto.id());

        return ResponseEntity.ok(messageDto);
    }

    @Operation(summary = "Message 삭제")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        log.info("Received message delete request: messageId={}", messageId);
        messageService.delete(messageId);
        log.info("Message deleted successfully: messageId={}", messageId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Channel의 Message 목록 조회")
    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> getMessagesByChannelId(
            @RequestParam UUID channelId,
            @RequestParam(required = false) Instant cursor,
            @PageableDefault(
                    size = 50,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        PageResponse<MessageDto> messageDtos = messageService.findAllByChannelId(channelId, cursor, pageable);
        return ResponseEntity.ok(messageDtos);
    }
}
