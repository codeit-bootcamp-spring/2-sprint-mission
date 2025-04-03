package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
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
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createMessage(
            @RequestPart("messageCreateRequest") MessageCreateDto messageCreateDto,
            @RequestPart(name = "attachments", required = false) List<MultipartFile> files) {
        List<BinaryContentCreateDto> binaryDtos = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    BinaryContentCreateDto dto = new BinaryContentCreateDto(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                    );
                    binaryDtos.add(dto);
                } catch (IOException e) {
                    return ResponseEntity.internalServerError().build();
                }
            }
        }
        Message message = messageService.create(messageCreateDto, binaryDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId,
                                                 @RequestBody MessageUpdateDto messageUpdateDto) {
        Message message = messageService.update(messageId, messageUpdateDto);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessagesByChannelId(@RequestParam(name = "channelId") UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}
