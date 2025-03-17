package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.entity.User;
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
     * @return 인증된 {@link User} 객체, 로그인 실패 시 {@code null} 또는 예외 발생 가능
     */
    User loginUser(UserCRUDDTO userLoginDTO);
}
