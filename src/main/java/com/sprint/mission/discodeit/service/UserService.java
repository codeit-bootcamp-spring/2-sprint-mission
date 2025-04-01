package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserResult register(UserCreateRequest userRequest, MultipartFile profileImage);

  UserResult getById(UUID userId);

  UserResult getByName(String name);

  List<UserResult> getAll();

  UserResult getByEmail(String email);

  List<UserResult> getAllByIds(List<UUID> userIds);

  UserResult updateName(UUID userId, String name);

  UserResult updateProfileImage(UUID userId, MultipartFile profileImage);

  void delete(UUID userId);

}
