/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.0.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.sprint.mission.discodeit.Api;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.request.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-04-03T13:02:47.093482200+09:00[Asia/Seoul]")
@Validated
@RestController
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /api/readStatuses : Message 읽음 상태 생성
     *
     * @param readStatusCreateRequest  (required)
     * @return Channel 또는 User를 찾을 수 없음 (status code 404)
     *         or 이미 읽음 상태가 존재함 (status code 400)
     *         or Message 읽음 상태가 성공적으로 생성됨 (status code 201)
     */
    @Operation(
        operationId = "create1",
        summary = "Message 읽음 상태 생성",
        tags = { "ReadStatus" },
        responses = {
            @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재함"),
            @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨", content = {
                @Content(mediaType = "*/*", schema = @Schema(implementation = ReadStatus.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/api/readStatuses",
        produces = { "*/*" },
        consumes = { "application/json" }
    )
    default ResponseEntity<ReadStatus> create1(
        @Parameter(name = "ReadStatusCreateRequest", description = "", required = true) @Valid
        @RequestBody
        ReadStatusCreateRequest readStatusCreateRequest
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("*/*"))) {
                    String exampleString = "{ \"createdAt\" : \"\", \"lastReadAt\" : \"\", \"id\" : \"\", \"userId\" : \"\", \"channelId\" : \"\", \"updatedAt\" : \"\" }";
                    ApiUtil.setExampleResponse(request, "*/*", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /api/readStatuses : User의 Message 읽음 상태 목록 조회
     *
     * @param userId 조회할 User ID (required)
     * @return Message 읽음 상태 목록 조회 성공 (status code 200)
     */
    @Operation(
        operationId = "findAllByUserId",
        summary = "User의 Message 읽음 상태 목록 조회",
        tags = { "ReadStatus" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공", content = {
                @Content(mediaType = "*/*", schema = @Schema(implementation = Object.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/api/readStatuses",
        produces = { "*/*" }
    )
    default ResponseEntity<Object> findAllByUserId(
        @NotNull @Parameter(name = "userId", description = "조회할 User ID", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "userId", required = true) Object userId
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PATCH /api/readStatuses/{readStatusId} : Message 읽음 상태 수정
     *
     * @param readStatusId 수정할 읽음 상태 ID (required)
     * @param readStatusUpdateRequest  (required)
     * @return Message 읽음 상태가 성공적으로 수정됨 (status code 200)
     *         or Message 읽음 상태를 찾을 수 없음 (status code 404)
     */
    @Operation(
        operationId = "update1",
        summary = "Message 읽음 상태 수정",
        tags = { "ReadStatus" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨", content = {
                @Content(mediaType = "*/*", schema = @Schema(implementation = ReadStatus.class))
            }),
            @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음")
        }
    )
    @RequestMapping(
        method = RequestMethod.PATCH,
        value = "/api/readStatuses/{readStatusId}",
        produces = { "*/*" },
        consumes = { "application/json" }
    )
    default ResponseEntity<ReadStatus> update1(
        @Parameter(name = "readStatusId", description = "수정할 읽음 상태 ID", required = true, in = ParameterIn.PATH) @PathVariable("readStatusId") Object readStatusId,
        @Parameter(name = "ReadStatusUpdateRequest", description = "", required = true) @Valid @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("*/*"))) {
                    String exampleString = "{ \"createdAt\" : \"\", \"lastReadAt\" : \"\", \"id\" : \"\", \"userId\" : \"\", \"channelId\" : \"\", \"updatedAt\" : \"\" }";
                    ApiUtil.setExampleResponse(request, "*/*", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
