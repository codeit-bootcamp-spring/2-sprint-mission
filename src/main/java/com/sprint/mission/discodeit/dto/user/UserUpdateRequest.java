package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

//사용자 정보 업데이트(수정) 요청 DTO -> 아이디, 수정하려는 새로운 이름, 이메일, 비번, 프로필 이미지
public record UserUpdateRequest(
        UUID userId,
        String newUsername,
        String newEmail,
        String newPassword,
        byte[] newProfileImage // 선택적
) { }
