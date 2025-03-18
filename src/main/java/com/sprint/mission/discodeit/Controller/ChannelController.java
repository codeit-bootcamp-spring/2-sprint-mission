package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.DTO.Request.ChannelCreateDTO;
import com.sprint.mission.discodeit.DTO.Request.EnterChannelDTO;
import com.sprint.mission.discodeit.DTO.Request.QuitChannelDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.ChannelJoinQuitDTO;
import com.sprint.mission.discodeit.DTO.legacy.Channel.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<Channel> create(@RequestBody ChannelCreateDTO channelCreateDTO) {
        Channel channel = channelService.create(channelCreateDTO);
        return ResponseEntity.ok(channel);
    }

    @PutMapping("/join/{channelId}")
    public ResponseEntity<String> join(@PathVariable String channelId, @RequestBody EnterChannelDTO enterChannelDTO) {
        UUID channelUUID = UUID.fromString(channelId);
        UUID userUUID = UUID.fromString(enterChannelDTO.userId());
        ChannelJoinQuitDTO joinDTO = ChannelJoinQuitDTO.join(channelUUID, userUUID, enterChannelDTO.type());
        User join = channelService.join(joinDTO);
        return ResponseEntity.ok(join.getName() + " has entered the server");
    }

    @PutMapping("/quit/{channelId}")
    public ResponseEntity<String> quit(@PathVariable String channelId, @RequestBody QuitChannelDTO quitChannelDTO) {
        UUID channelUUID = UUID.fromString(channelId);
        UUID userUUID = UUID.fromString(quitChannelDTO.userId());
        ChannelJoinQuitDTO quitDTO = ChannelJoinQuitDTO.quit(channelUUID, userUUID);
        User quit = channelService.quit(quitDTO);

        return ResponseEntity.ok(quit.getName() + " has quit the server");
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<List<ChannelDTO>> findAll(@PathVariable String serverId) {
        List<ChannelDTO> list = channelService.findAllByServerAndUser(serverId);
        return ResponseEntity.ok(list);
    }

//    @PutMapping("/update/{userId}")
//    public ResponseEntity<User> update(@PathVariable String , @RequestBody UserUpdateRequestDTO ) {
//
//
//    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> delete(@PathVariable String channelId) {
        boolean isDelete = channelService.delete(channelId);
        if (isDelete == true) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.status(401).body("Delete failed");
        }
    }
}
