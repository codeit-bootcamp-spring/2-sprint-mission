package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.service.ChannelResult;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelResult> createPublic(@Valid @RequestBody PublicChannelCreateRequest channelRegisterRequest) {
        log.info("공개 채널 생성 요청: name={}, description={}", channelRegisterRequest.name(), channelRegisterRequest.description());
        ChannelResult aPublic = channelService.createPublic(channelRegisterRequest);
        log.info("공개 채널 생성 성공: channelId={}", aPublic.id());

        return ResponseEntity.ok(aPublic);
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResult> createPrivate(@Valid @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
        log.info("비공개 채널 생성 요청: participantIds={}", privateChannelCreateRequest.participantIds());
        ChannelResult aPrivate = channelService.createPrivate(privateChannelCreateRequest);
        log.info("비공개 채널 생성 성공: channelId={}", aPrivate.id());

        return ResponseEntity.ok(aPrivate);
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResult> getById(@PathVariable UUID channelId) {
        log.debug("채널 조회 요청: channelId={}", channelId);
        ChannelResult channel = channelService.getById(channelId);
        log.info("채널 조회 성공: channelId={}", channelId);

        return ResponseEntity.ok(channel);
    }

    @GetMapping
    public ResponseEntity<List<ChannelResult>> getAllByUserId(@RequestParam(value = "userId") UUID userId) {
        log.debug("사용자별 채널 목록 조회 요청: userId={}", userId);
        List<ChannelResult> channels = channelService.getAllByUserId(userId);
        log.info("사용자별 채널 목록 조회 성공: userId={}, 채널 수={}", userId, channels.size());

        return ResponseEntity.ok(channels);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelResult> updatePublic(@PathVariable UUID channelId, @Valid @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
        log.info("공개 채널 수정 요청: channelId={}, newName={}, newDescription={}", channelId, publicChannelUpdateRequest.newName(), publicChannelUpdateRequest.newDescription());
        ChannelResult channelResult = channelService.updatePublic(channelId, publicChannelUpdateRequest);
        log.info("공개 채널 수정 성공: channelId={}", channelId);

        return ResponseEntity.ok(channelResult);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        log.warn("채널 삭제 요청: channelId={}", channelId);
        channelService.delete(channelId);
        log.info("채널 삭제 성공: channelId={}", channelId);

        return ResponseEntity.noContent().build();
    }

}

