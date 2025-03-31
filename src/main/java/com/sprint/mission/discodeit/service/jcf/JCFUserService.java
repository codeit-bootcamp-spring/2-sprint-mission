package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.UserService.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserService.UserFindDto;
import com.sprint.mission.discodeit.dto.UserService.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID,User> userData;

    public JCFUserService() {
        this.userData = new HashMap<>();
    }

    @Override
    public User create(UserCreateRequest userCreateRequest, BinaryContentDTO binaryContentDTO) {
        User newUser = userCreateRequest.toEntity();
        userData.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = userData.get(userId);

        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다."));
    }

    @Override
    public UserFindDto findWithStatus(UUID id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return userData.values().stream().toList();
    }

    @Override
    public List<UserFindDto> findAllWithStatus() {
        return List.of();
    }

    @Override
    public User update(UUID id, UserUpdateRequest userUpdateRequest) {
        User userNullable = userData.get(id);
        User user = Optional.ofNullable(userNullable).orElseThrow(() -> new NoSuchElementException(id + "가 존재하지 않습니다."));
        user.updateUser(userUpdateRequest.userName(), userUpdateRequest.email(), userUpdateRequest.password());

        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!userData.containsKey(userId)) {
            throw new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다.");
        }
        userData.remove(userId);
    }

}
