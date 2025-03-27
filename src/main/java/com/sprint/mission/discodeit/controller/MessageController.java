package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;

    // formdata 형태로, message, file키로 전달받기
    // required false, 파일 없어도 메세지 전송 가능
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestPart("message") MessageCreateRequest request,
            @RequestPart(value = "file", required = false) List<MultipartFile> files) {
//        System.out.println("메세지 send 요청 실행");
//        System.out.println("메세지 작성자 : " + request.authorId());
//        System.out.println("메세지 채널 : " + request.channelId());
//        System.out.println("메세지 내용 : " + request.content());
//
//        // 파일 정보 확인
//        if (files != null) {
//            for (MultipartFile file : files) {
//                System.out.println("파일명: " + file.getOriginalFilename());
//                System.out.println("파일타입: " + file.getContentType());
//                System.out.println("사이즈: " + file.getSize());
//            }
//        }

        // MultipartFile → BinaryContentCreateRequest 로 변환, List.of() 불변 (빈)리스트
        List<BinaryContentCreateRequest> binaryRequests = files == null ? List.of() :
                files.stream().map(file -> {
                    try {
                        return new BinaryContentCreateRequest(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("파일 변환 실패", e);
                    }
                }).toList();


        Message message = messageService.create(request, binaryRequests);
        System.out.println("message + form data :" + message);
        return ResponseEntity.ok("성공적으로 메세지 전송");
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Message> editMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequest request) {
        System.out.println("메세지 수정 API가 들어왔습니다.");
        Message message = messageService.update(messageId, request);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID messageId) {
        System.out.println("메세지 삭제 API가 들어왔습니다.");
        messageService.delete(messageId);
        return ResponseEntity.ok("메세지 삭제 성공");
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<List<Message>> findAllByChannelId(@PathVariable UUID channelId) {
        System.out.println("톡방 메세지 전제 조회 API 실행");
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messageList);
    }
}
