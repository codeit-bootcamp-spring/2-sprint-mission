package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserFindDTO;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.request.CreateUserRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    void reset(boolean adminAuth);

    UUID create(CreateUserRequestDTO userCreateDTO, Optional<CreateBinaryContentRequestDTO> binaryContentDTO);

    UserFindDTO findById(UUID userId);

    List<UserFindDTO> listAllUsers();

    void delete(UUID userId);

//    User update(String userId, UserUpdateDTO userUpdateDTO, Optional<CreateBinaryContentRequestDTO>binaryContentDTO);

}
