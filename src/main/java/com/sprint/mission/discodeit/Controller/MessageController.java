package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.DTO.Request.MessageRequestBodyDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.MessageWriteDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController  {
    private final MessageService messageService;

    @PostMapping("/write")
    public ResponseEntity<Message> create(@RequestBody MessageRequestBodyDTO messageRequestBodyDTO) {
        MessageWriteDTO messageWriteDTO = messageRequestBodyDTO.messageWriteDTO();
        List<BinaryContentCreateDTO> binaryContentCreateDTOS = messageRequestBodyDTO.binaryContentCreateDTO();

        List<Optional<BinaryContentCreateDTO>> list = binaryContentCreateDTOS.stream()
                .map(Optional::ofNullable)
                .toList();

        Message message = messageService.create(messageWriteDTO, list);
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

    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> delete(@PathVariable String messageId) {
        boolean isDelete = messageService.delete(messageId);
        if (isDelete == true) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.status(401).body("Delete failed");
        }
    }
}
