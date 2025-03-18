package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ProfileImageRequest {
    private UUID userId; //사용자구분
    private byte[] imageData; //프로필이미지

    public ProfileImageRequest() {}

    //유저 생성
    public ProfileImageRequest(byte[] imageData) {
        this.imageData = imageData;
    }

    //기존 유저의 프로필 변경
    public ProfileImageRequest(UUID userId, byte[] imageData) {
        this.userId = userId;
        this.imageData = imageData;

    }
}
