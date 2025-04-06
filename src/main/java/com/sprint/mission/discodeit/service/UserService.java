package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.application.dto.user.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResult register(UserCreateRequest userRequest, MultipartFile profileImage);

    UserResult getById(UUID userId);

    UserResult getByName(String name);

    List<UserResult> getAll();

    UserResult getByEmail(String email);

    UserResult update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile profileImage); // TODO: 4/6/25 Multipart파일 수정바람

    void delete(UUID userId);
}
