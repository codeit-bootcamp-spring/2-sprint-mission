package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

  private final ChannelService channelService;


  //사용자 채널 조회
  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @GetMapping
  public ResponseEntity<List<ChannelDto>> findByUserId(@RequestParam UUID userId) {
    System.out.println("사용자가 속한 채널 조회 API 실행");
    List<ChannelDto> channelDtos = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelDtos);
  }

  @Operation(summary = "Public channel 조회")
  @PostMapping("/public")
  public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request) {
    System.out.println("공개 채널 생성 API가 들어왔습니다.");
    Channel channel = channelService.create(request);
    return ResponseEntity.ok(channel);
  }

  // @Valid 유효성 검사하고, @NotBlack하고 세트임.
  @Operation(summary = "Private channel 조회")
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivate(
      @Valid @RequestBody PrivateChannelCreateRequest request) {
    System.out.println("비공개 채널 생성 API 요청이 들어왔습니다.");
    Channel channel = channelService.create(request);
    return ResponseEntity.ok(channel);
  }

  // 공개 톡방 정보 수정
  @Operation(summary = "Channel 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> updatePublic(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    System.out.println("공개 톡방 정보 수정 API 요청 들어옴.");
    Channel channel = channelService.update(channelId, request);
    return ResponseEntity.ok(channel);
  }

  //채널 삭제
  @Operation(summary = "Channel 삭제")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<String> deleteChannel(@PathVariable UUID channelId) {
    System.out.println("채널 삭제 API 실행");
    channelService.delete(channelId);
    return ResponseEntity.ok("delete success");
  }
}
