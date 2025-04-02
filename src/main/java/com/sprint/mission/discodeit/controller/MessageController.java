package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BaseResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/messages")
    public ResponseEntity<BaseResponseDto> createMessage(@RequestPart("message") MessageCreateDto messageCreateDto,
                                                         @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        List<BinaryContentCreateDto> binaryDtos = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    BinaryContentCreateDto dto = new BinaryContentCreateDto(
                            file.getOriginalFilename(),
                            file.getSize(),
                            file.getContentType(),
                            file.getBytes()
                    );
                    binaryDtos.add(dto);
                } catch (IOException e) {
                    return ResponseEntity.internalServerError()
                            .body(BaseResponseDto.failure("첨부파일 처리 중 오류 발생: " + e.getMessage()));
                }
            }
        }

        Message message = messageService.create(messageCreateDto, binaryDtos);
        return ResponseEntity.ok(BaseResponseDto.success(message.getId() + " 메시지 등록이 완료되었습니다."));
    }

    @PutMapping("/messages/{id}")
    public ResponseEntity<BaseResponseDto> updateMessage(@PathVariable("id") UUID messageId,
                                                         @RequestBody MessageUpdateDto messageUpdateDto) {
        Message message = messageService.update(messageId, messageUpdateDto);
        return ResponseEntity.ok(BaseResponseDto.success(message.getId() + " 메시지 변경이 완료되었습니다."));
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<BaseResponseDto> deleteMessage(@PathVariable("id") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok(BaseResponseDto.success(messageId + " 메시지 삭제가 완료되었습니다."));
    }

    @GetMapping("/channels/{channelId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}
