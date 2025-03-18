package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Request.UserLoginDTO;
import org.springframework.stereotype.Service;

/**
 * 인증(Authentication) 관련 서비스를 제공하는 인터페이스입니다.
 * 사용자 로그인 기능을 제공합니다.
 */
@Service
public interface AuthService {
    /**
     * 사용자 로그인을 처리하고 인증된 사용자 객체를 반환합니다.
     *
     * @param userLoginDTO 로그인할 사용자 정보 DTO (사용자 ID 및 비밀번호 포함)
     * @return 로그인 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean loginUser(UserLoginDTO userLoginDTO);
}
