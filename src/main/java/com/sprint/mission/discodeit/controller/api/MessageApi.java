package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
public interface MessageApi {

    @Operation(summary = "Message 생성", operationId = "create_2")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found"))
            ),
            @ApiResponse(
                    responseCode = "201", description = "Message가 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
    ResponseEntity<MessageDto> create(
            @Parameter(
                    description = "Message 생성 정보",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ) MessageCreateRequest request,
            @Parameter(
                    description = "Message 첨부 파일들",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) List<MultipartFile> attachments
    );

    @Operation(summary = "Message 내용 수정", operationId = "update_2")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Message가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Message를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found"))
            ),
    })
    ResponseEntity<MessageDto> updateMessage(
            @Parameter(
                    name = "messageId",
                    description = "수정할 Message ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            ) UUID messageId,
            @Parameter(description = "수정할 Message 내용") MessageUpdateRequest request
    );

    @Operation(summary = "Message 삭제", operationId = "delete_1")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
            @ApiResponse(
                    responseCode = "404", description = "Message를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found"))
            )
    })
    ResponseEntity<Void> deleteMessageById(
            @Parameter(
                    name = "messageId",
                    description = "삭제할 Message ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            ) UUID messageId
    );

    @Operation(summary = "Channel의 Message 목록 조회", operationId = "findAllByChannelId")
    @ApiResponse(
            responseCode = "200", description = "Message 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = PageResponse.class))
    )
    ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
            @Parameter(
                    name = "channelId",
                    description = "조회할 Channel ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            ) UUID channelId,
            @Parameter(description = "페이징 커서 정보") Instant cursor,
            @Parameter(description = "페이징 정보", example = "{\"size\": 50, \"sort\": \"createdAt,desc\"}") Pageable pageable
    );
}
