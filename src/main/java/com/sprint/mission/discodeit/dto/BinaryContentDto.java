package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class BinaryContentDto {
    @NotBlank
    @Getter
    @Builder(toBuilder = true)
    public static class Create {
        private final UUID ownerId;
        private final String ownerType;
        private  String fileName;
        private MultipartFile file;
        private String contentType;
    }
    @NotBlank
    @Getter
    @Builder(toBuilder = true)
    public static class  Summary{
            private String email; //유저의 이메일
            private String filename;//이미지 이름
            private String contentType; //이미지 타입
            private byte[] data; //이미지 바이트 코드
    }
    @Getter
    @Builder(toBuilder = true)
    public static class DeleteResponse {
        private String fileName;
        private String message; //성공/실패 메세지
    }
}
