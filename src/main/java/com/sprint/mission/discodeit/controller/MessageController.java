package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/create")
    public ResponseEntity<Message> createMessage(
            @RequestPart("messageInfo") MessageCreateDto messageCreateRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files

    ) {

        List<BinaryContentCreateDto> contentCreate = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    BinaryContentCreateDto content = new BinaryContentCreateDto(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                    );
                    contentCreate.add(content);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Message createMessage = messageService.create(messageCreateRequest, contentCreate);
        return ResponseEntity.ok(createMessage);
    }


    @PutMapping("/update")
    public ResponseEntity<Message> updateMessage(@RequestBody MessageUpdateDto messageUpdateRequest) {

        Message updateMessage = messageService.update(messageUpdateRequest);
        return ResponseEntity.ok(updateMessage);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Message> deleteMessage(@RequestBody MessageDeleteDto messageDeleteRequest) {

        messageService.delete(messageDeleteRequest);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/find")
    public ResponseEntity<MessageFindResponseDto> findMessages(
            @RequestBody MessageFindRequestDto messageFindRequest
    ) {
        MessageFindResponseDto MessageFindResponse = messageService.find(messageFindRequest);
        return ResponseEntity.ok(MessageFindResponse);
    }


    @GetMapping("/findAllByChannel")
    public ResponseEntity<List<MessageFindAllByChannelIdResponseDto>> findMessagesByChannelId(
            @RequestBody MessageFindAllByChannelIdRequestDto messageFindByChannelIdRequest
    ) {
        List<MessageFindAllByChannelIdResponseDto> messageFindByChannelResponse =
                messageService.findAllByChannelId(messageFindByChannelIdRequest);

        return ResponseEntity.ok(messageFindByChannelResponse);
    }


}
