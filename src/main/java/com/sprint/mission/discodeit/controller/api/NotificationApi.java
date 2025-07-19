package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name ="Notification", description = "알림 Api")
public interface NotificationApi {

    @Operation(summary = "알림 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "알림 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationDto.class)))
            )
    })
    ResponseEntity<List<NotificationDto>> findNotifications(Authentication authentication);

    @Operation(summary = "알림 읽기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "알림 읽기 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationDto.class)))
            )
    })
    ResponseEntity<Void> deleteNotifications(@PathVariable UUID notificationId, Authentication authentication);

}
