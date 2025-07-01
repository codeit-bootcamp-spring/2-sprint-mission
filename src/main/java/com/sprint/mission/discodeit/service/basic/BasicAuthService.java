package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.auth.LoginCommand;
import com.sprint.mission.discodeit.dto.service.auth.LoginResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidPasswordException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.AuthMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.MaskingUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final AuthMapper authMapper;

}


