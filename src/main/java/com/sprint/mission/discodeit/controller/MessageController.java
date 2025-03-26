package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageFindDTO;
import com.sprint.mission.discodeit.dto.create.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.dto.create.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.display.MessageDisplayList;
import com.sprint.mission.discodeit.dto.result.MessageCreateResult;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{userId}/{serverId}/{channelId}")
public class MessageController {
    private final MessageService messageService;

    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageCreateResult> create(
            @PathVariable UUID userId,@PathVariable UUID channelId,
            @RequestPart("message") MessageCreateRequestDTO messageDTO,
            @RequestPart(value = "profileImage", required = false) List<MultipartFile> files) throws IOException {
        List<Optional<BinaryContentCreateRequestDTO>> list = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                Optional<BinaryContentCreateRequestDTO> binaryContentRequest = Optional.empty();
                if (file != null && !file.isEmpty()) {
                    binaryContentRequest = Optional.of(new BinaryContentCreateRequestDTO(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                    ));
                    list.add(binaryContentRequest);
                }
            }
        }

        Message message = messageService.create(userId, channelId, messageDTO, list);
        return ResponseEntity.ok(new MessageCreateResult(message.getMessageId()));
    }

    @GetMapping
    public ResponseEntity<MessageDisplayList> findAll(@PathVariable UUID channelId) {
        List<MessageFindDTO> list = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(new MessageDisplayList(list));
    }

    @PutMapping("/update/{messageId}")
    public ResponseEntity<UUID> update(
            @PathVariable UUID messageId,
            @RequestBody UpdateMessageDTO updateMessageDTO) {

        UUID update = messageService.update(messageId, updateMessageDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<String> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok("Delete successful");
    }
}
