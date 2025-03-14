package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateDto;
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
    public User findById(UUID userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new NoSuchElementException(userId + " 유저를 찾을 수 없습니다.");
        }

        UserStatus userStatus = new UserStatus(user.getId());
        UserResponseDto userResponseDto = new UserResponseDto(user, userStatus.isActive());

        return userResponseDto;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        boolean isExistUser = findAll().stream().anyMatch(user -> user.getEmail().equals(newEmail)); // 동일한 이메일 == 같은 유저

        if (isExistUser) {
            throw new RuntimeException(newEmail + " 이메일은 이미 가입되어 수정할 수 없습니다.");
        }

        User user = findById(userId);
        user.update(newUsername, newEmail, newPassword);

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = findById(userId);
        userRepository.delete(user.getId());
    }
}
