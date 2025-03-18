package com.sprint.mission.discodeit.DTO.legacy.User;

import lombok.Builder;

import java.util.UUID;

/**
 * 사용자 정보 관리를 위한 DTO 클래스입니다.
 * 사용자의 생성, 조회, 업데이트, 삭제 및 로그인 관련 데이터를 포함합니다.
 */
@Builder
public record UserCRUDDTO(
        UUID userId,
        UUID profileId,
        String userName,
        String email,
        String password
) {
    /**
     * 주어진 사용자 이름과 이메일을 기반으로 중복 체크용 DTO를 생성합니다.
     *
     * @param userName 중복 체크할 사용자 이름
     * @param email 중복 체크할 사용자 이메일
     * @return 중복 체크용 {@link UserCRUDDTO} 객체
     */
    public static UserCRUDDTO checkDuplicate(String userName,
                                             String email) {
        return UserCRUDDTO.builder().userName(userName).email(email).build();
    }

    /**
     * 주어진 사용자 이름과 비밀번호를 기반으로 로그인용 DTO를 생성합니다.
     *
     * @param userName 로그인할 사용자 이름
     * @param password 로그인할 사용자의 비밀번호
     * @return 로그인 요청을 위한 {@link UserCRUDDTO} 객체
     */
    public static UserCRUDDTO login(String userName,
                                    String password) {
        return UserCRUDDTO.builder()
                .userName(userName)
                .password(password).build();
    }

    /**
     * 새 사용자를 생성하기 위한 DTO를 생성합니다.
     *
     * @param userName 생성할 사용자 이름
     * @param email 생성할 사용자 이메일
     * @param password 생성할 사용자 비밀번호
     * @return 생성 요청을 위한 {@link UserCRUDDTO} 객체
     */
    public static UserCRUDDTO create(String userName,
                                     String email,
                                     String password) {
        return UserCRUDDTO.builder()
                .userName(userName)
                .email(email)
                .password(password).build();
    }

    /**
     * 특정 사용자 ID를 기반으로 삭제 요청을 위한 DTO를 생성합니다.
     *
     * @param userId 삭제할 사용자의 ID
     * @return 삭제 요청을 위한 {@link UserCRUDDTO} 객체
     */
    public static UserCRUDDTO delete(UUID userId) {
        return UserCRUDDTO.builder()
                .userId(userId).build();
    }

    /**
     * 사용자의 정보를 업데이트하기 위한 DTO를 생성합니다.
     *
     * @param replaceId 업데이트할 사용자의 ID
     * @param replaceProfileId 업데이트할 사용자의 프로필 ID
     * @param replaceName 업데이트할 사용자 이름
     * @param replaceEmail 업데이트할 사용자 이메일
     * @return 업데이트 요청을 위한 {@link UserCRUDDTO} 객체
     */
    public static UserCRUDDTO update(UUID replaceId,
                                     UUID replaceProfileId,
                                     String replaceName,
                                     String replaceEmail) {
        return UserCRUDDTO.builder()
                .userId(replaceId)
                .profileId(replaceProfileId)
                .userName(replaceName)
                .email(replaceEmail).build();
    }
}
