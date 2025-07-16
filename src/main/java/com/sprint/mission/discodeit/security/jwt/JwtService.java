package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface JwtService {

    JwtSession registerJwtSession(UserDto userDto); // 로그인 시 사용

    boolean validate(String token); // access, refresh 유효성 체크

    JwtObject parse(String token); // iat, exp, userDto 포함된 payload 파싱

    JwtSession refreshJwtSession(String refreshToken); // 리프레시 토큰 기반 재발급 + session 업데이트

    void invalidateJwtSession(String refreshToken); // 로그아웃 처리용

    JwtSession getJwtSession(String refreshToken);

    void invalidateAllJwtSessionsByUserId(UUID userId);

    List<JwtSession> getActiveJwtSessions();
}
