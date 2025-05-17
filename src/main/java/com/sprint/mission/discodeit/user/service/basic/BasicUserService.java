package com.sprint.mission.discodeit.user.service.basic;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentStorageService;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.mapper.UserResultMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sprint.mission.discodeit.common.constant.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.common.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentStorageService binaryContentStorageService;
    private final UserResultMapper userResultMapper;

    @Transactional
    @Override
    public UserResult register(UserCreateRequest userRequest, BinaryContentRequest binaryContentRequest) {
        validateDuplicateEmail(userRequest.email());
        validateDuplicateUserName(userRequest.username());

        BinaryContent binaryContent = binaryContentStorageService.createBinaryContent(binaryContentRequest);
        User savedUser = userRepository.save(new User(userRequest.username(), userRequest.email(), userRequest.password(), binaryContent));

        return UserResult.fromEntity(savedUser, savedUser.getUserStatus().isOnline(Instant.now()));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        return userResultMapper.convertToUserResult(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        return userResultMapper.convertToUserResult(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResult> getAllIn() {
        return userRepository.findAll()
                .stream()
                .map(userResultMapper::convertToUserResult)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

        return userResultMapper.convertToUserResult(user);
    }

    @Transactional
    @Override
    public UserResult update(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContentRequest binaryContentRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        binaryContentStorageService.deleteBinaryContent(user);
        BinaryContent binaryContent = binaryContentStorageService.createBinaryContent(binaryContentRequest);

        user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword(), binaryContent);
        User updatedUser = userRepository.save(user);

        return userResultMapper.convertToUserResult(updatedUser);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }

        userRepository.deleteById(userId);
    }

    private void validateDuplicateUserName(String name) {
        if (userRepository.existsUserByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 이름 입니다");
        }
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsUserByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
    }

}
