package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.MultipartToBinaryConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Message 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createMessage(
            @RequestPart("messageCreateRequest") MessageCreateDto messageCreateDto,
            @RequestPart(name = "attachments", required = false) List<MultipartFile> files) {
        List<BinaryContentCreateDto> binaryDtos = MultipartToBinaryConverter.toBinaryContentCreateDtos(files);
        Message message = messageService.create(messageCreateDto, binaryDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Operation(summary = "Message 내용 수정")
    @PatchMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId,
                                                 @RequestBody MessageUpdateDto messageUpdateDto) {
        Message message = messageService.update(messageId, messageUpdateDto);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "Message 삭제")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Channel의 Message 목록 조회")
    @GetMapping
    public ResponseEntity<List<Message>> getMessagesByChannelId(@RequestParam(name = "channelId") UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}
