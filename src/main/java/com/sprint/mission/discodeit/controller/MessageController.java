package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.IdResponse;
import com.sprint.mission.discodeit.controller.dto.MessageInfoResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    // 메시지 생성
    @RequestMapping(value = "/{channelId}/{authorId}", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<IdResponse> create(
            @PathVariable UUID channelId, @PathVariable UUID authorId,
            @RequestPart("content") MessageCreateRequest request,
            @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        List<BinaryContentCreateRequest> binaryContentList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                binaryContentList.add(BinaryContentCreateRequest.of(file));
            }
        }
        Message message = messageService.create(channelId, authorId, request, binaryContentList);
        return ResponseEntity.ok(IdResponse.of(true, message.getId()));
    }

    // 메시지 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<IdResponse> update(@PathVariable UUID id, @RequestBody MessageUpdateRequest request) {
        Message message = messageService.update(id, request);
        return ResponseEntity.ok(IdResponse.of(true, message.getId()));
    }

    // 메시지 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<IdResponse> delete(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 특정 채널의 메시지 목록 조회
    @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageInfoResponse>> findAllByChannelId(@PathVariable UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        List<MessageInfoResponse> response = messages.stream().map(MessageInfoResponse::of).toList();
        return ResponseEntity.ok(response);
    }
}
