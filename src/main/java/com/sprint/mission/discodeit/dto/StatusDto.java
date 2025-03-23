package com.sprint.mission.discodeit.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

public class StatusDto {

    @Getter
    @Builder(toBuilder = true)
    public static class Summary {
        private final UUID id; //status 아이디
        private UUID userid; // 유저아이디
        private ZonedDateTime lastSeenAt; // 마지막 접속 시간
        private ZonedDateTime updatedAt; // 업데이트 시간
    }

    @Getter
    @Builder(toBuilder = true)
    public static class Create {
        private final UUID userid; //유저 아이디

    }

    @Getter
    @Builder(toBuilder = true)
    public static class Delete {
        private final UUID userid; //유저 아이디

    }
    @Getter
    @Builder(toBuilder = true)
    public static class ResponseDelete {
        private final UUID id;
        private String message;
    }
}
