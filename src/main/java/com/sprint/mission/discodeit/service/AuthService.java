package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;

// 향후 OAuth 등의 인증이 추가될 수 있으므로 인터페이스 구현
public interface AuthService {
    LoginDTO login(LoginParam loginParam);
}
