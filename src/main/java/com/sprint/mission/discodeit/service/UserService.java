package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO create(CreateUserParam createUserParam, MultipartFile multipartFile) throws IOException;
    UserDTO find(UUID id);
    List<UserDTO> findAll();
    UpdateUserDTO update(UUID id, UpdateUserParam updateUserParam, MultipartFile multipartFile);
    void delete(UUID id);;
}
