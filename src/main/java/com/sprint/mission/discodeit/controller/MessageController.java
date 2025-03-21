package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDTO;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController  {
    private final MessageService messageService;

    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(
            @RequestParam("message") CreateMessageRequestDTO messageRequestDTO,
            @RequestParam(value = "profileImage", required = false) List<MultipartFile> files
    ) throws IOException {
        List<Optional<CreateBinaryContentRequestDTO>> list = new ArrayList<>();
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


        Message message = messageService.create(messageRequestDTO, list);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/{channelId}")
    public ResponseEntity<List<Message>> findAll(@PathVariable String channelId) {
        List<Message> list = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(list);
    }

//    @PutMapping("/update/{userId}")
//    public ResponseEntity<> update(@PathVariable String , @RequestBody UserUpdateRequestDTO ) {
//
//
//    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<String> delete(@PathVariable String messageId) {
        boolean isDelete = messageService.delete(messageId);
        if (isDelete == true) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.status(401).body("Delete failed");
        }
    }
}
