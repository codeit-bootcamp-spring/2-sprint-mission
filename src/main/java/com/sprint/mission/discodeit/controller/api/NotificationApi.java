package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "NotificationApi", description = "NotificationApi API")
public interface NotificationApi {

    @Operation(summary = "알림 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 알림 리스트 반환",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationDto.class))))
    })
    @GetMapping
    ResponseEntity<List<NotificationDto>> getNotifications(@Parameter(description = "사용자 정보") String authHeader);

    @Operation(summary = "알림 확인(삭제)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "알림이 성공적으로 확인(삭제)됨")
    })
    @DeleteMapping("/{notificationId}")
    ResponseEntity<Void> deleteNotification(
            @Parameter(description = "사용자 정보") String authHeader,
            @Parameter(description = "확인할 알림 ID") @PathVariable UUID notificationId
    );
}
