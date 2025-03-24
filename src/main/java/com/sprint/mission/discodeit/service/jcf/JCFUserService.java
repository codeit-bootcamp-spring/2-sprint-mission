package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserFindDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID,User> userData;

    public JCFUserService() {
        this.userData = new HashMap<>();
    }

    @Override
    public User create(UserCreateDTO userCreateDTO, BinaryContentDTO binaryContentDTO) {
        User newUser = userCreateDTO.toEntity();
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
    public UserFindDTO findWithStatus(UUID id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return userData.values().stream().toList();
    }

    @Override
    public List<UserFindDTO> findAllWithStatus() {
        return List.of();
    }

    @Override
    public User update(UserUpdateDTO userUpdateDTO) {
        User userNullable = userData.get(userUpdateDTO.id());
        User user = Optional.ofNullable(userNullable).orElseThrow(() -> new NoSuchElementException(userUpdateDTO.id() + "가 존재하지 않습니다."));
        user.updateUser(userUpdateDTO.userName(), userUpdateDTO.email(), userUpdateDTO.password());

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
