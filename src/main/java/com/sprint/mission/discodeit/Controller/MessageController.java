package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController  {
    private final MessageService messageService;

//    @PostMapping("/write")
//    public ResponseEntity<Message> create(@RequestBody MessageWriteDTO messageWriteDTO) {
//        messageService.create(messageWriteDTO)
//        return ResponseEntity.ok(channel);
//    }
//
//
//    @GetMapping("/{serverId}")
//    public ResponseEntity<List<ChannelDTO>> findAll(@PathVariable String serverId) {
//        List<ChannelDTO> list = channelService.findAllByServerAndUser(serverId);
//        return ResponseEntity.ok(list);
//    }

//    @PutMapping("/update/{userId}")
//    public ResponseEntity<User> update(@PathVariable String , @RequestBody UserUpdateRequestDTO ) {
//
//
//    }
//
//    @DeleteMapping("/{channelId}")
//    public ResponseEntity<String> delete(@PathVariable String channelId) {
//        boolean isDelete = channelService.delete(channelId);
//        if (isDelete == true) {
//            return ResponseEntity.ok("Delete successful");
//        } else {
//            return ResponseEntity.status(401).body("Delete failed");
//        }
//    }
}
