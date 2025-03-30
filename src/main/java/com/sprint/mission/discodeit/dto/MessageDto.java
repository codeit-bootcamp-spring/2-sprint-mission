package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.util.UpdateOperation;
import com.sprint.mission.discodeit.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class MessageDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotNull
        private UUID channelId;
        @NotBlank
        private String message;
        private List<UUID> binaryContents;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotNull
        private UUID id;
        private String message;
        private List<UUID> binaryContents;
        private UpdateOperation operation;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor

    public static class Response {
        @NotNull
        private UUID id;
        @NotNull
        private UUID channelId;
        @NotNull
        private UUID authorId;
        private String message;
        private ZonedDateTime createdAt;
        private ZonedDateTime updateAt;
        private List<UUID> binaryContentIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor

    public static class Summary {
        @NotNull
        private UUID id;
        private UUID senderId;           // 발신자
        @NotNull
        private String message;          // 메시지 내용
        private ZonedDateTime createdAt; // 생성 시간
        @NotNull
        private boolean hasAttachments;  // 첨부파일 유무
        public static Summary from(Message message, boolean hasAttachments) {
            return Summary.builder()
                    .id(message.getId())
                    .senderId(message.getAuthorId())
                    .message(message.getMessage())
                    .createdAt(message.getCreatedAt())
                    .hasAttachments(hasAttachments)
                    .build();
        }
    }
}