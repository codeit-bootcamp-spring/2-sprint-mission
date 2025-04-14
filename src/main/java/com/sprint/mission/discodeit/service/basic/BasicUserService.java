package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.LogicException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
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
    public User create(UserCreateDto userCreateDto, BinaryContentCreateDto binaryContentCreateDto) {
        List<User> users = userRepository.findAll();

        boolean isEmailExist = users.stream().anyMatch(user -> user.getEmail().equals(userCreateDto.email()));
        if (isEmailExist) {
            throw new LogicException(ErrorCode.USER_EMAIL_EXISTS);
        }

        boolean isNameExist = users.stream().anyMatch(user -> user.getUsername().equals(userCreateDto.username()));
        if (isNameExist) {
            throw new LogicException(ErrorCode.USER_NAME_EXISTS);
        }

        UUID nullableProfileId = null;

        if (binaryContentCreateDto != null) {
            String fileName = binaryContentCreateDto.fileName();
            String contentType = binaryContentCreateDto.contentType();
            byte[] bytes = binaryContentCreateDto.bytes();
            BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
            nullableProfileId = binaryContentRepository.save(binaryContent).getId();
        }

        User newUser = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password(),
                nullableProfileId);
        userRepository.save(newUser);
        userStatusService.create(new UserStatusCreateDto(newUser.getId(), Instant.MIN));

        return newUser;
    }

    @Override
    public UserDto findById(UUID userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new LogicException(ErrorCode.USER_NOT_FOUND);
        }

        UserStatus userStatus = userStatusService.findById(user.getId());

        return new UserDto(user, userStatus.isOnline());
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(user -> {
            UserStatus userStatus = userStatusService.findById(user.getId());
            return new UserDto(user, userStatus.isOnline());
        }).toList();
    }

    @Override
    public User update(UUID userId, UserUpdateDto userUpdateDto,
                       BinaryContentCreateDto binaryContentCreateDto) {
        List<User> users = userRepository.findAll();
        boolean isEmailExist = users.stream().anyMatch(
                user -> user.getEmail().equals(userUpdateDto.newEmail()));

        if (isEmailExist) {
            throw new LogicException(ErrorCode.USER_EMAIL_EXISTS);
        }

        boolean isNameExist = users.stream().anyMatch(
                user -> user.getUsername().equals(userUpdateDto.newUsername()));

        if (isNameExist) {
            throw new LogicException(ErrorCode.USER_NAME_EXISTS);
        }

        User user = userRepository.findById(userId);

        if (user == null) {
            throw new LogicException(ErrorCode.USER_NOT_FOUND);
        }

        UUID nullableProfileId = null;

        if (binaryContentCreateDto != null) {
            String fileName = binaryContentCreateDto.fileName();
            String contentType = binaryContentCreateDto.contentType();
            byte[] bytes = binaryContentCreateDto.bytes();
            BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
            nullableProfileId = binaryContentRepository.save(binaryContent).getId();
        }

        user.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), userUpdateDto.newPassword(),
                nullableProfileId);
        userRepository.save(user);

        return user;
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new LogicException(ErrorCode.USER_NOT_FOUND);
        }

        userRepository.delete(user.getId());

        UserStatus userStatus = userStatusService.findById(user.getId());
        userStatusService.delete(userStatus.getId());

        if (user.getProfileId() != null) {
            BinaryContent binaryContent = binaryContentRepository.findById(user.getProfileId());
            binaryContentRepository.delete(binaryContent.getId());
        }
    }
}
