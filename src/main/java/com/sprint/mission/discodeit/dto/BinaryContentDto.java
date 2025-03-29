package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class BinaryContentDto {
        @Getter
        @Builder(toBuilder = true)
        public static class Upload { // 메타데이터 전용 DTO
            @NotNull()
            private final UUID ownerId;
            @NotBlank()
            private final String ownerType;
            @NotBlank
            private final MultipartFile file;
            private final String fileName;
            private final String contentType;
        }

        @Getter
        @Builder(toBuilder = true)
        public static class Summary{
            private UUID id;
            private String email;
            private String fileName;
            private String contentType;
        }

        @Getter
        @Builder(toBuilder = true)
        public static class DeleteResponse {
            private UUID id;
            private String fileName;
            private boolean success;
            private String message;
        }
    }

