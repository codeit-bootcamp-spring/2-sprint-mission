package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;

// 향후 OAuth 등의 인증이 추가될 수 있으므로 인터페이스 구현
public interface AuthService {
    UserDTO login(LoginParam loginParam);
}
