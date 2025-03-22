package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.MessageFindDTO;
import com.sprint.mission.discodeit.dto.display.MessageDisplayList;
import com.sprint.mission.discodeit.dto.create.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.create.CreateMessageRequestDTO;
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
@RequestMapping("/api/servers/{serverId}/channels/{channelId}/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(
            @PathVariable UUID serverId,
            @PathVariable UUID channelId,
            @RequestParam("message") MultipartFile messageJson,
            @RequestParam(value = "profileImage", required = false) List<MultipartFile> files
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CreateMessageRequestDTO messageDTO = objectMapper.readValue(messageJson.getBytes(), CreateMessageRequestDTO.class);

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

        Message message = messageService.create(messageDTO, list);
        return ResponseEntity.ok(message);
    }


    @GetMapping
    public ResponseEntity<MessageDisplayList> findAll(@PathVariable UUID channelId) {
        List<MessageFindDTO> list = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(new MessageDisplayList(list));
    }

    @PutMapping(value = "/update/{messageId}")
    public ResponseEntity<UUID> update(
            @PathVariable String serverId,
            @PathVariable String channelId,
            @PathVariable UUID messageId,
            @RequestParam("message") UpdateMessageDTO updateMessageDTO) {

        UUID update = messageService.update(messageId, updateMessageDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<String> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok("Delete successful");
    }
}
