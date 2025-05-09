package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageByChannelRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.service.message.MessageResult;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "메세지 관련 API")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(
            summary = "메세지 생성",
            description = "첨부 파일을 포함한 메세지 생성"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메세지 생성 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResult> create(
            @Parameter(description = "메세지 생성 정보", required = true)
            @Valid @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,

            @Parameter(description = "메세지 첨부파일", required = true)
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

        List<BinaryContentRequest> binaryContentRequests = List.of();
        if (attachments != null) {
            binaryContentRequests = attachments.stream()
                    .map(BinaryContentRequest::fromMultipartFile)
                    .toList();
        }

        return ResponseEntity.ok(messageService.create(messageCreateRequest, binaryContentRequests));
    }


    @GetMapping
    public ResponseEntity<PageResponse<MessageResult>> getAllByChannelId(@Valid MessageByChannelRequest messageByChannelRequest) {
        return ResponseEntity.ok(messageService.getAllByChannelId(messageByChannelRequest));
    }

    @Operation(
            summary = "메세지 수정",
            description = "메세지 내용 수정"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메세지 수정 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResult> update(
            @Parameter(description = "메세지 ID", required = true)
            @PathVariable UUID messageId,

            @Parameter(description = "메세지 수정 내용", required = true)
            @RequestBody MessageUpdateRequest messageUpdateRequest) {

        return ResponseEntity.ok(
                messageService.updateContext(messageId, messageUpdateRequest.newContent()));
    }

    @Operation(
            summary = "메세지 식제"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메세지 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "메세지 ID", required = true)
            @PathVariable UUID messageId) {

        messageService.delete(messageId);

        return ResponseEntity.noContent().build();
    }
}