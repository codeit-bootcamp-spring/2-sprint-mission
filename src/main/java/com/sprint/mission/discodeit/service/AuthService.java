package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    User loginUser(UserCRUDDTO userLoginDTO);
}
