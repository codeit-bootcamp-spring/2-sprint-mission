package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.sprint.mission.discodeit.entity.User;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> create(
        @RequestPart("messageCreateRequest") @Valid MessageCreateRequest messageCreateRequest,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments,
        @AuthenticationPrincipal User user) {

        MessageDto createdMessage = messageService.create(messageCreateRequest, attachments, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @PatchMapping(path = "{messageId}")
    public ResponseEntity<MessageDto> update(@PathVariable("messageId") UUID messageId,
        @RequestBody @Valid MessageUpdateRequest request) {
        MessageDto updatedMessage = messageService.update(messageId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }

    @DeleteMapping(path = "{messageId}")
    public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
        @RequestParam("channelId") UUID channelId,
        @RequestParam(value = "cursor", required = false) String cursor,
        @PageableDefault(size = 50, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
            pageable.getPageSize());
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }


}
