package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelApi {

    @Operation(summary = "Public Channel 생성", operationId = "create_3")
    @ApiResponse(
            responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = Channel.class))
    )
    ResponseEntity<Channel> createPublic(
            @Parameter(description = "Public Channel 생성 정보") PublicChannelCreateRequest request
    );

    @Operation(summary = "Private Channel 생성", operationId = "create_4")
    @ApiResponse(
            responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = Channel.class))
    )
    ResponseEntity<Channel> createPrivate(
            @Parameter(description = "Private Channel 생성 정보") PrivateChannelCreateRequest request
    );

    @Operation(summary = "Channel 정보 수정", description = "update_3")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "404", description = "Channel을 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Channel with id {channelId} not found"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Private Channel은 수정할 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Private channel cannot be updated"))
            ),
            @ApiResponse(
                    responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = Channel.class))
            )
    })
    ResponseEntity<Channel> updateChannel(
            @Parameter(
                    name = "channelId",
                    description = "수정할 Channel ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            ) UUID channelId,
            @Parameter(description = "수정할 Channel 정보") PublicChannelUpdateRequest request
    );

    @Operation(summary = "Channel 삭제", operationId = "delete_2")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음"),
            @ApiResponse(
                    responseCode = "204", description = "Channel이 성공적으로 삭제됨",
                    content = @Content(examples = @ExampleObject(value = "Channel  with id {channelId} not found"))
            )
    })
    ResponseEntity<Void> deleteChannelById(
            @Parameter(
                    name = "channelId",
                    description = "삭제할 Channel ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            ) UUID channelId
    );

    @Operation(summary = "User가 참여 중인 Channel목록 조회", operationId = "findAll_1")
    @ApiResponse(
            responseCode = "200", description = "Channel 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))
    )
    ResponseEntity<List<ChannelDto>> findAllChannelsByUserId(
            @Parameter(
                    name = "userId",
                    description = "조회할 User ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            ) UUID userId
    );
}
