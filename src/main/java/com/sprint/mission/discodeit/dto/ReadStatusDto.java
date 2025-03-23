package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

//읽음 상태 dTo
public class ReadStatusDto {
    @Getter
    @Setter
    public static class Create {
        @NotNull(message = "채널 ID는 필수입니다")
        private UUID channelId;

        @NotNull(message = "사용자 ID는 필수입니다")
        private UUID userId;

        @NotNull(message = "마지막으로 읽은 메시지 ID는 필수입니다")
        private UUID lastReadMessageId;
    }
    @Getter
    @Setter
    public static class Update {
        @NotNull(message = "읽음 상태 ID는 필수입니다")
        private UUID id;
        
        @NotNull(message = "마지막으로 읽은 메시지 ID는 필수입니다")
        private UUID lastReadMessageId;
    }
    

    @Getter
    @Setter
    public static class ResponseReadStatus {
        private UUID id;
        private UUID channelId;
        private UUID userId;
        private UUID lastReadMessageId;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;
    }
} 