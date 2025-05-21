package com.sprint.mission.discodeit.domain.message.controller;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.dto.request.ChannelMessagePageRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResult> create(
            @Valid @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        log.info("메시지 생성 요청: channelId={}, authorId={}, 첨부파일 수={}", messageCreateRequest.channelId(), messageCreateRequest.authorId(), attachments != null ? attachments.size() : 0);
        List<BinaryContentRequest> binaryContentRequests = getBinaryContentRequests(attachments);
        MessageResult message = messageService.create(messageCreateRequest, binaryContentRequests);
        log.info("메시지 생성 성공: messageId={}", message.id());

        return ResponseEntity.ok(message);
    }

    private List<BinaryContentRequest> getBinaryContentRequests(List<MultipartFile> attachments) {
        if (attachments == null) {
            return List.of();
        }

        return attachments.stream()
                .map(BinaryContentRequest::fromMultipartFile)
                .toList();
    }

    @GetMapping
    public ResponseEntity<PageResponse<MessageResult>> getAllByChannelId(
            @RequestParam("channelId") UUID channelId,
            @RequestParam(value = "cursor", required = false) Instant cursor,
            @PageableDefault(size = 50, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.debug("채널별 메시지 목록 조회 요청: channelId={}, pageSize={}", channelId, pageable.getPageSize());
        ChannelMessagePageRequest messageByChannelRequest = new ChannelMessagePageRequest(cursor, pageable.getPageSize(), pageable.getPageNumber(), pageable.getSort());
        PageResponse<MessageResult> messages = messageService.getAllByChannelId(channelId, messageByChannelRequest);
        log.info("채널별 메시지 목록 조회 성공: channelId={}, 메시지 수={}", channelId, messages.content().size());

        return ResponseEntity.ok(messages);
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResult> update(@PathVariable UUID messageId, @Valid @RequestBody MessageUpdateRequest messageUpdateRequest) {
        log.info("메시지 수정 요청: messageId={}, newContent={}", messageId, messageUpdateRequest.newContent());
        MessageResult message = messageService.updateContext(messageId, messageUpdateRequest.newContent());
        log.info("메시지 수정 성공: messageId={}", messageId);

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        log.warn("메시지 삭제 요청: messageId={}", messageId);
        messageService.delete(messageId);
        log.info("메시지 삭제 성공: messageId={}", messageId);

        return ResponseEntity.noContent().build();
    }

}