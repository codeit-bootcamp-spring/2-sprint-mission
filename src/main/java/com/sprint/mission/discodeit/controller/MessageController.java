package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> create(
        @RequestPart("messageCreateRequest") @Valid MessageCreateRequest messageCreateRequest,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        // log
        log.info("BinaryContent 생성 요청");
        List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
            .map(files -> files.stream()
                .map(file -> {
                    try {
                        log.info("파일 저장 시도");
                        return new BinaryContentCreateRequest(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                        );
                    } catch (IOException e) {
                        log.error("파일 저장 중 오류 발생", e);
                        // 커스텀 에러처리
                        throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR, null);
                    }
                })
                .toList())
            .orElse(new ArrayList<>());
        log.info("Message 생성 요청, message: {}", messageCreateRequest.content());
        MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
        log.info("Message 생성 완료, message: {}", createdMessage.content());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdMessage);
    }

    @PatchMapping(path = "{messageId}")
    public ResponseEntity<MessageDto> update(
        @PathVariable("messageId") UUID messageId,
        @RequestBody @Valid MessageUpdateRequest request
    ) {
        log.debug("Message 업데이트 요청, message: {}", request.newContent());
        MessageDto updatedMessage = messageService.update(messageId, request);
        log.info("Message 업데이트 완료, message: {}", updatedMessage.content());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedMessage);
    }

    @DeleteMapping(path = "{messageId}")
    public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
        log.warn("Message 삭제 요청");
        messageService.delete(messageId);
        log.info("Message 삭제 완료");
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
        @RequestParam("channelId") UUID channelId,
        @RequestParam(value = "cursor", required = false) Instant cursor,
        // Spring data 페이지네이션을 위한 기본값 정의
        @PageableDefault(
            size = 50,
            page = 0,
            sort = "createdAt",
            direction = Direction.DESC
        ) Pageable pageable
    ) {
        log.info("채널에 전체 message 조회 요청");
        PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
            pageable);
        log.info("채널Id로 전제 message 조회 성공");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messages);
    }
}
