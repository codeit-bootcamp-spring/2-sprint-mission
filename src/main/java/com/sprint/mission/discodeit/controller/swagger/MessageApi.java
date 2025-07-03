package com.sprint.mission.discodeit.controller.swagger;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageApi {

    @Operation(summary = "Message 생성", operationId = "create_2")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            content = @Content(
                mediaType = "*/*",
                schema = @Schema(implementation = MessageDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Channel 또는 User를 찾을 수 없음",
            content = @Content(
                mediaType = "*/*",
                examples = @ExampleObject("Channel | Author with id {channelId | authorId} not found")
            )
        )
    })
    ResponseEntity<MessageDto> send(
        @Valid @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    );

    @Operation(summary = "Message 내용 수정", operationId = "update_2")
    @Parameters(value = {
        @Parameter(
            name = "messageId",
            in = ParameterIn.PATH,
            description = "수정할 Message ID",
            required = true,
            schema = @Schema(type = "string", format = "uuid")
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = MessageUpdateRequest.class)
        ),
        required = true
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "메세지가 성공적으로 수정됨",
            content = @Content(
                mediaType = "*/*",
                schema = @Schema(implementation = MessageDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "메세지를 찾을 수 없음",
            content = @Content(
                mediaType = "*/*",
                examples = @ExampleObject("Message with id {messageId} not found")
            )
        )
    })
    ResponseEntity<MessageDto> update(
        @PathVariable("messageId") UUID messageId,
        @Valid @RequestBody MessageUpdateRequest messageUpdateRequest
    );

    @Operation(summary = "Message 삭제", operationId = "delete_1")
    @Parameters({
        @Parameter(
            name = "messageId",
            in = ParameterIn.PATH,
            description = "삭제할 Mesaage ID",
            required = true,
            schema = @Schema(type = "string", format = "uuid")
        )
    })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "메세지가 성공적으로 삭제됨"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Message를 찾을 수 없음",
            content = @Content(
                mediaType = "*/*",
                examples = @ExampleObject("Message with id {messageId} not found")
            )
        )
    })
    ResponseEntity<Void> delete(
        @PathVariable("messageId") UUID messageId
    );

    @Operation(summary = "Channel의 Message 목록 조회")
    @Parameters(value = {
        @Parameter(
            name = "channelId",
            in = ParameterIn.QUERY,
            description = "조회할 Channel ID",
            required = true,
            schema = @Schema(type = "string", format = "uuid")
        )
    })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Message목록 조회 성공",
            content = @Content(
                mediaType = "*/*",
                schema = @Schema(implementation = PageResponse.class)
            )
        )
    })
    ResponseEntity<PageResponse<MessageDto>> findChannelMessage(
        @RequestParam("channelId") UUID channelId,
        @RequestParam(value = "cursor", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant cursor,
        @PageableDefault(size = 50, sort = "createdAt", direction = Direction.DESC)
        @Parameter(hidden = true) Pageable pageable
    );
}
