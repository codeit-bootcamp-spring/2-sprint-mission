package com.sprint.mission.discodeit.dto.user;

// 사용자 생성 DTO -> 전달할 데이터 이름, 이메일, 비번, 프로필 이미지
public record UserCreateRequest(
        String username,
        String email,
        String password,
        byte[] profileImage // 프로필 이미지 (선택적), Optional<>혹은 @Nullable 사용 가능 하지만, 그냥 둬도 NUll 가능.
) {}
