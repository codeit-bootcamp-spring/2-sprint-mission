package com.sprint.mission.discodeit.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.sprint.mission.discodeit.controller.swagger.MessageApi;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @PostMapping(path = "", consumes = MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<MessageDto> send(
        @Valid @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        MessageDto messageDto = messageService.sendMessage(messageCreateRequest, attachments);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    }

    @PatchMapping("/{messageId}")
    @Override
    @PreAuthorize("principal.userId == @basicMessageService.findById(#messageId).author.id")
    public ResponseEntity<MessageDto> update(
        @PathVariable("messageId") UUID messageId,
        @Valid @RequestBody MessageUpdateRequest messageUpdateRequest
    ) {
        MessageDto messageDto = messageService.updateMessage(messageId, messageUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @DeleteMapping("/{messageId}")
    @Override
    @PreAuthorize("hasRole('ADMIN') or principal.userId == @basicMessageService.findById(#messageId).author.id")
    public ResponseEntity<Void> delete(
        @PathVariable("messageId") UUID messageId
    ) {
        messageService.deleteMessageById(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("")
    @Override
    public ResponseEntity<PageResponse<MessageDto>> findChannelMessage(
        @RequestParam("channelId") UUID channelId,
        @RequestParam(value = "cursor", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant cursor,
        @PageableDefault(size = 50, sort = "createdAt", direction = Direction.DESC)
        @Parameter(hidden = true) Pageable pageable
    ) {
        PageResponse<MessageDto> page = messageService.findMessageByChannelId(channelId, pageable,
            cursor);
        return ResponseEntity.status(HttpStatus.OK)
            .body(page);
    }
}
