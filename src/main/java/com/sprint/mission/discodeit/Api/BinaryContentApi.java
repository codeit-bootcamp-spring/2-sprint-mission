/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.0.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.sprint.mission.discodeit.Api;

import com.sprint.mission.discodeit.entity.BinaryContent;

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
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /api/binaryContents/{binaryContentId} : 첨부 파일 조회
     *
     * @param binaryContentId 조회할 첨부 파일 ID (required)
     * @return 첨부 파일 조회 성공 (status code 200)
     *         or 첨부 파일을 찾을 수 없음 (status code 404)
     */
    @Operation(
        operationId = "find",
        summary = "첨부 파일 조회",
        tags = { "BinaryContent" },
        responses = {
            @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공", content = {
                @Content(mediaType = "*/*", schema = @Schema(implementation = BinaryContent.class))
            }),
            @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/api/binaryContents/{binaryContentId}",
        produces = { "*/*" }
    )
    default ResponseEntity<BinaryContent> find(
        @Parameter(name = "binaryContentId", description = "조회할 첨부 파일 ID", required = true, in = ParameterIn.PATH) @PathVariable("binaryContentId") Object binaryContentId
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("*/*"))) {
                    String exampleString = "{ \"createdAt\" : \"\", \"fileName\" : \"\", \"size\" : \"\", \"bytes\" : \"\", \"id\" : \"\", \"contentType\" : \"\" }";
                    ApiUtil.setExampleResponse(request, "*/*", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /api/binaryContents : 여러 첨부 파일 조회
     *
     * @param binaryContentIds 조회할 첨부 파일 ID 목록 (required)
     * @return 첨부 파일 목록 조회 성공 (status code 200)
     */
    @Operation(
        operationId = "findAllByIdIn",
        summary = "여러 첨부 파일 조회",
        tags = { "BinaryContent" },
        responses = {
            @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공", content = {
                @Content(mediaType = "*/*", schema = @Schema(implementation = Object.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/api/binaryContents",
        produces = { "*/*" }
    )
    default ResponseEntity<Object> findAllByIdIn(
        @NotNull @Parameter(name = "binaryContentIds", description = "조회할 첨부 파일 ID 목록", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "binaryContentIds", required = true) Object binaryContentIds
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
