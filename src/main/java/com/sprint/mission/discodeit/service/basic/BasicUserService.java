package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final BinaryContentRepository binaryContentRepository;

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
        userRepository.save(newUser);
        userStatusService.create(new UserStatusCreateDto(newUser.getId(), Instant.MIN));

        return newUser;
    }

    @Override
    public UserDto findById(UUID userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new NoSuchElementException(userId + " 유저를 찾을 수 없습니다.");
        }

        UserStatus userStatus = userStatusService.findById(user.getId());

        return new UserDto(user, userStatus.isActive());
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(user -> {
            UserStatus userStatus = userStatusService.findById(user.getId());
            return new UserDto(user, userStatus.isActive());
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
        userRepository.save(user);

        return user;
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId);
        userRepository.delete(user.getId());

        UserStatus userStatus = userStatusService.findById(user.getId());
        userStatusService.delete(userStatus.getId());

        if (user.getProfileId() != null) {
            BinaryContent binaryContent = binaryContentRepository.findById(user.getProfileId());
            binaryContentRepository.delete(binaryContent.getId());
        }
    }
}
