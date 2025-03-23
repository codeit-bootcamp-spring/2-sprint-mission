package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @NotBlank
    public static class Create {
        private UUID channelId;
        private UUID authorId;
        private String content;
        private List<BinaryContentDto.Create> binaryContents;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotNull
        private UUID messageId;
        private String content;
        private List<BinaryContentDto.Create> binaryContents;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @NotBlank
    public static class Response {
        private UUID messageId;
        private UUID channelId;
        private UUID authorId;
        private String content;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;
        private List<UUID> binaryContentIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAttachments {
        private UUID messageId;
        private List<BinaryContentDto.Create> binaryContentsToAdd;
        private List<UUID> binaryContentIdsToRemove;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @NotBlank
    public static class Summary {
        private UUID id;
        private UUID senderId;           // 발신자
        private String message;          // 메시지 내용
        private ZonedDateTime createdAt; // 생성 시간
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