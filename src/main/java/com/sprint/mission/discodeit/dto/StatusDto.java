package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.util.StatusOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

public class StatusDto {

    @Getter
    @Builder(toBuilder = true)
    public static class Summary {
        @NotNull
        private final UUID id; //status 아이디
        @NotNull
        private UUID userid; // 유저아이디
        private ZonedDateTime lastSeenAt; // 마지막 접속 시간
        private ZonedDateTime updatedAt; // 업데이트 시간
    }

    @Getter
    @Builder(toBuilder = true)
    public static class Create {
        @NotNull
        private final UUID userid; //유저 아이디
    }


    @Getter
    @Builder(toBuilder = true)
    public static class ResponseDelete {
        @NotNull
        private final UUID id;
        private String message;
    }
    @AllArgsConstructor
    @Getter
    @Builder(toBuilder = true)
    public static class StatusRequest {
        @NotBlank
        private String status;
    }
    @AllArgsConstructor
    @Getter
    @Builder(toBuilder = true)
    public static class StatusResponse {
        private UUID id;
        @NotBlank
        private StatusOperation status;
    }
}