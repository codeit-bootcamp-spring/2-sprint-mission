package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.message.*;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping
    public ResponseEntity<CreateMessageResponseDTO> createMessage(@RequestPart("message") @Valid CreateMessageRequestDTO createMessageRequestDTO,
                                                                  @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) {

        MessageDTO messageDTO = messageService.create(messageMapper.toMessageParam(createMessageRequestDTO), multipartFileList);
        return ResponseEntity.ok(messageMapper.toMessageResponseDTO(messageDTO));
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<UpdateMessageResponseDTO> updateMessage(@PathVariable("messageId") UUID id,
                                                                  @RequestPart("message") @Valid UpdateMessageRequestDTO updateMessageRequestDTO,
                                                                  @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) {

        UpdateMessageDTO updateMessageDTO = messageService.update(id, messageMapper.toUpdateMessageParam(updateMessageRequestDTO), multipartFileList);
        return ResponseEntity.ok(messageMapper.toUpdateMessageResponseDTO(updateMessageDTO));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<DeleteMessageResponseDTO> deleteMessage(@PathVariable("messageId") UUID id) {
        messageService.delete(id);
        return ResponseEntity.ok(new DeleteMessageResponseDTO(id, id + "번 메시지가 삭제되었습니다."));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<MessageListDTO> getChannelMessages(@PathVariable("channelId") UUID id) {
        List<MessageDTO> messageDTOList = messageService.findAllByChannelId(id);
        return ResponseEntity.ok(new MessageListDTO(messageDTOList));
    }
}
