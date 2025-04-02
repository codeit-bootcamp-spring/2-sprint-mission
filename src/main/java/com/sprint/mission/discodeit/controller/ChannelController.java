package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channel")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/create/public")
    public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request) {
        System.out.println("공개 채널 생성 API가 들어왔습니다.");
        Channel channel = channelService.create(request);
        return ResponseEntity.ok(channel);
    }

    // @Valid 유효성 검사하고, @NotBlack하고 세트임.
    @PostMapping("/create/private")
    public ResponseEntity<Channel> createPrivate(@Valid @RequestBody PrivateChannelCreateRequest request) {
        System.out.println("비공개 채널 생성 API 요청이 들어왔습니다.");
        Channel channel = channelService.create(request);
        return ResponseEntity.ok(channel);
    }

    // 공개 톡방 정보 수정
    @PutMapping("/{channelId}")
    public ResponseEntity<Channel> updatePublic(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        System.out.println("공개 톡방 정보 수정 API 요청 들어옴.");
        Channel channel = channelService.update(channelId, request);
        return ResponseEntity.ok(channel);
    }

    //채널 삭제
    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> deleteChannel(@PathVariable UUID channelId) {
        System.out.println("채널 삭제 API 실행");
        channelService.delete(channelId);
        return ResponseEntity.ok("delete success");
    }

    //사용자 채널 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<ChannelDto>> findByUserId(@PathVariable UUID userId) {
        System.out.println("사용자가 속한 채널 조회 API 실행");
        List<ChannelDto> channelDtos = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channelDtos);
    }
}
