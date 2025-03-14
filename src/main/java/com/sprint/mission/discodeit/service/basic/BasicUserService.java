package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User create(UserCreateDto userCreateDto) {
        List<User> users = userRepository.findAll();

        boolean isEmailExist = users.stream().anyMatch(user -> user.getEmail().equals(userCreateDto.email()));
        if (isEmailExist) {
            throw new RuntimeException(userCreateDto.email() + " 이메일은 이미 가입되었습니다.");
        }

        boolean isNameExist = users.stream().anyMatch(user -> user.getUsername().equals(userCreateDto.username()));
        if (isNameExist) {
            throw new RuntimeException(userCreateDto.username() + " 이름은 이미 가입되었습니다.");
        }

        User newUser = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password());
        UserStatus newUserStatus = new UserStatus(newUser.getId());

        userStatusRepository.save(newUserStatus);
        return userRepository.save(newUser);
    }

    @Override
    public UserResponseDto findById(UUID userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new NoSuchElementException(userId + " 유저를 찾을 수 없습니다.");
        }

        UserStatus userStatus = new UserStatus(user.getId());
        UserResponseDto userResponseDto = new UserResponseDto(user, userStatus.isActive());

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(user -> {
            UserStatus userStatus = userStatusRepository.findById(user.getId());
            return new UserResponseDto(user, userStatus.isActive());
        }).toList();
    }

    @Override
    public User update(UserUpdateDto userUpdateDto) {
        List<User> users = userRepository.findAll();
        boolean isEmailExist = users.stream().anyMatch(
                user -> user.getEmail().equals(userUpdateDto.newEmail()));

        if (isEmailExist) {
            throw new RuntimeException(userUpdateDto.newEmail() + " 이메일은 이미 존재하여 수정할 수 없습니다.");
        }

        boolean isNameExist = users.stream().anyMatch(
                user -> user.getUsername().equals(userUpdateDto.newUsername()));

        if (isNameExist) {
            throw new RuntimeException(userUpdateDto.newEmail() + " 유저는 이미 존재하여 수정할 수 없습니다.");
        }

        User user = userRepository.findById(userUpdateDto.id());
        user.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), userUpdateDto.newPassword(),
                userUpdateDto.newProfileId());

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = findById(userId);
        userRepository.delete(user.getId());
    }
}
