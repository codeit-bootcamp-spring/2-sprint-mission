package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageFindDTO;
import com.sprint.mission.discodeit.dto.create.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.create.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.display.MessageDisplayList;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/servers/{serverId}/channels/{channelId}/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(
            @RequestPart("message") CreateMessageRequestDTO messageDTO,
            @RequestPart(value = "profileImage", required = false) List<MultipartFile> files,
            HttpServletRequest httpRequest
    ) throws IOException {
        UUID userId = (UUID) httpRequest.getAttribute("userId");

        List<Optional<CreateBinaryContentRequestDTO>> list = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                Optional<CreateBinaryContentRequestDTO> binaryContentRequest = Optional.empty();
                if (file != null && !file.isEmpty()) {
                    binaryContentRequest = Optional.of(new CreateBinaryContentRequestDTO(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                    ));
                    list.add(binaryContentRequest);
                }
            }
        }

        Message message = messageService.create(userId, messageDTO, list);
        return ResponseEntity.ok(message);
    }


    @GetMapping
    public ResponseEntity<MessageDisplayList> findAll(@PathVariable UUID channelId) {
        List<MessageFindDTO> list = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(new MessageDisplayList(list));
    }

    @PutMapping(value = "/{messageId}/update")
    public ResponseEntity<UUID> update(
            @PathVariable UUID messageId,
            @RequestParam("message") UpdateMessageDTO updateMessageDTO) {

        UUID update = messageService.update(messageId, updateMessageDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{messageId}/delete")
    public ResponseEntity<String> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok("Delete successful");
    }
}
