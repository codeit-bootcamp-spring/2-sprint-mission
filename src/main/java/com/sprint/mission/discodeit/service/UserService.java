package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.RequestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.RequestToService.UserUpdateDTO;
import com.sprint.mission.discodeit.DTO.legacy.User.UserCRUDDTO;
import com.sprint.mission.discodeit.DTO.legacy.User.UserFindDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    void reset(boolean adminAuth);

    User create(UserCreateDTO userCreateDTO, Optional<BinaryContentCreateDTO> binaryContentDTO);

    UserFindDTO find(UUID userId);

    List<UserFindDTO> findAll();

    boolean delete(String userId);

    User update(String userId, UserUpdateDTO userUpdateDTO, Optional<BinaryContentCreateDTO>binaryContentDTO);

}
