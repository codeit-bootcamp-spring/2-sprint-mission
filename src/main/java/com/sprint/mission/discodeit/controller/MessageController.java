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
            @RequestPart("messageInfo") MessageCreateDto messageCreateReq,
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
                } catch (IOException e){
                    return ResponseEntity.badRequest().build();
                }
            }
        }

        Message createMessage = messageService.create(messageCreateReq, contentCreate);
        return ResponseEntity.ok(createMessage);
    }


    @PutMapping("/update")
    public ResponseEntity<Message> updateMessage(@RequestPart("newMessageInfo")MessageUpdateDto messageUpdateReq){

        Message updateMessage = messageService.update(messageUpdateReq);
        return ResponseEntity.ok(updateMessage);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Message> deleteMessage(@RequestPart("messageInfo") MessageDeleteDto messageDeleteReq){

        messageService.delete(messageDeleteReq);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/find")
    public ResponseEntity<MessageFindResponseDto> findMessages(
            @RequestPart("messageInfo")MessageFindRequestDto messageFindReq
    ) {
        MessageFindResponseDto MessageFindResponse = messageService.find(messageFindReq);
        return ResponseEntity.ok(MessageFindResponse);
    }


    @GetMapping("/findAllByChannel")
    public ResponseEntity<List<MessageFindAllByChannelIdResponseDto>> findMessagesByChannelId(
            @RequestPart("channelId") MessageFindAllByChannelIdRequestDto messageFindByChannelIdReq
    ) {
        List<MessageFindAllByChannelIdResponseDto> messageFindByChannelResponse =
                messageService.findAllByChannelId(messageFindByChannelIdReq);

        return ResponseEntity.ok(messageFindByChannelResponse);
    }


}
