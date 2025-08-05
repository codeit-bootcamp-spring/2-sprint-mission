package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.KafkaMessageProducer;
import jakarta.validation.Valid;

import java.util.Collections;
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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
import com.sprint.mission.discodeit.security.jwt.JwtSession;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final KafkaMessageProducer kafkaMessageProducer;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> create(
            @RequestPart("request") @Valid MessageCreateRequest request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments,
            @AuthenticationPrincipal JwtSession jwtSession
    ) {
        User user = jwtSession.getUser();
        MessageDto messageDto = messageService.create(request, attachments, user);


        messagingTemplate.convertAndSend("/sub/channels." + messageDto.channelId() + ".messages", messageDto);

        return new ResponseEntity<>(messageDto, HttpStatus.CREATED);
    }


    @MessageMapping("/messages")
    public void sendMessage(MessageCreateRequest request, @AuthenticationPrincipal User user) {
        MessageDto message = messageService.create(request, Collections.emptyList(), user);

        // 🚀 실시간 WebSocket 전송 (즉시 응답성)
        messagingTemplate.convertAndSend("/sub/channels." + message.channelId() + ".messages", message);
        
        // 🔄 Kafka 전송 (안정성, 로깅, 다른 서비스 연동)
        kafkaMessageProducer.sendMessage(message);
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

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable UUID messageId) {
        MessageDto messageDto = messageService.find(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }


}
