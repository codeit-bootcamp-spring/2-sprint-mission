package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Optional;
import java.util.UUID;

public class JCFUserService extends JCFBaseService<UserEntity> implements UserService{

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return data.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public UserEntity updateUsername(UUID userId, String newUsername) {
        Optional<UserEntity> userOptional = findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            boolean exists = data.stream()
                    .anyMatch(existingUser -> !existingUser.getId().equals(userId)
                    && existingUser.getUsername().equals(newUsername));

            if (exists) {
                throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
            }

            user.updateUsername(newUsername, data);
            return update(user);
            }
        throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

    @Override
    public UserEntity updateNickname(String nickname, String newNickname) {
        Optional<UserEntity> userOptional = data.stream()
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst();
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.updateNickname(newNickname);
            return update(user);
        }
        throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
    }
    @Override
    public UserEntity updatePhoneNumber(String phoneNumber, String newPhoneNumber) {
        Optional<UserEntity> userOptional = data.stream()
                .filter(user -> user.getPhonenumber().equals(phoneNumber))
                .findFirst();

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.updatePhoneNumber(newPhoneNumber);
            return update(user);
        }
        throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
    }
    @Override
    public UserEntity updateEmail(String email, String newEmail) {
        Optional<UserEntity> userOptional = data.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.updateEmail(newEmail);
            return update(user);
        }
        throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
    }
    @Override
    public UserEntity updatePassword(String password, String newPassword) {
        Optional<UserEntity> userOptional = data.stream()
                .filter(user->user.getPassword().equals(password))
                .findFirst();
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return update(user);
        }
        throw new IllegalArgumentException("사용자를 찾을 수없습니다.");
    }
}
