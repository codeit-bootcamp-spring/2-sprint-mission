package com.sprint.mission.discodeit.service.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserUpdateRequest (
        UUID id,
        String username,
        String email,
        MultipartFile profileImage // 선택적 프로필 이미지 업데이트
        // 기본 생성자 및 Getter, Setter 추가
){}
