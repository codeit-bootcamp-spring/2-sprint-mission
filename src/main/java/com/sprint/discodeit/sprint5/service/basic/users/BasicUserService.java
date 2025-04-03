package com.sprint.discodeit.sprint5.service.basic.users;

import com.sprint.discodeit.sprint5.domain.StatusType;
import com.sprint.discodeit.sprint5.domain.dto.userDto.*;
import com.sprint.discodeit.sprint5.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint5.domain.entity.User;
import com.sprint.discodeit.sprint5.domain.entity.UserStatus;
import com.sprint.discodeit.sprint5.domain.mapper.UserMapper;
import com.sprint.discodeit.sprint5.global.AuthException;
import com.sprint.discodeit.sprint5.global.ErrorCode;
import com.sprint.discodeit.sprint5.global.RequestException;
import com.sprint.discodeit.sprint5.repository.file.BaseBinaryContentRepository;
import com.sprint.discodeit.sprint5.repository.file.BaseUserStatusRepository;
import com.sprint.discodeit.sprint5.repository.file.FileUserRepository;
import com.sprint.discodeit.sprint5.service.basic.util.BinaryGenerator;
import com.sprint.discodeit.sprint5.service.basic.util.UserStatusEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserServiceV1 {

    private final FileUserRepository fileUserRepository;
    private final BinaryGenerator binaryGenerator;
    private final UserStatusEvaluator userStatusEvaluator;
    private final BaseUserStatusRepository baseUserStatusRepository;
    private final BaseBinaryContentRepository baseBinaryContentRepository;

    @Override
    public UserNameStatusResponseDto create(UserRequestDto userRequestDto, UserProfileImgResponseDto userProfileImgResponseDto) {
        if (userRequestDto == null) {
            throw new RequestException(ErrorCode.USER_REQUEST_NULL);
        }
        if (userProfileImgResponseDto == null) {
            throw new RequestException(ErrorCode.PROFILE_IMAGE_NULL);
        }

        if (fileUserRepository.findByUsername(userRequestDto.username()).isPresent()) {
            throw new RequestException(ErrorCode.DUPLICATE_USERNAME);
        }

        if (fileUserRepository.findByEmail(userRequestDto.email()).isPresent()) {
            throw new RequestException(ErrorCode.DUPLICATE_EMAIL);
        }

        BinaryContent profileImage = binaryGenerator.createProfileImage(userProfileImgResponseDto.imgUrl());
        User user = UserMapper.toUser(userRequestDto);
        UserStatus userStatus = new UserStatus(user.getCreatedAt(), StatusType.Active.getExplanation());

        user.associateStatus(userStatus);
        user.associateProfileId(profileImage);

        baseUserStatusRepository.save(userStatus);
        fileUserRepository.save(user);
        baseBinaryContentRepository.save(profileImage);

        return new UserNameStatusResponseDto(user.getUsername(), userStatus.getStatusType(), user.getId());
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = fileUserRepository.findById(userId)
                .orElseThrow(() -> new RequestException(ErrorCode.USER_NOT_FOUND));

        UserStatus userStatus = baseUserStatusRepository.findById(user.getUserStatusId())
                .orElseThrow(() -> new RequestException(ErrorCode.USER_STATUS_NOT_FOUND));

        String status = userStatusEvaluator.determineUserStatus(userStatus.getLastLoginTime());

        return new UserResponseDto(user.getProfileId(), user.getUsername(), user.getEmail(), status);
    }

    @Override
    public List<User> findAll() {
        return fileUserRepository.findByAll();
    }

    @Override
    public UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto, String userId) {
        User user = fileUserRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RequestException(ErrorCode.USER_NOT_FOUND));

        BinaryContent profileImage = binaryGenerator.createProfileImage(userUpdateRequestDto.profileImg());

        user.associateProfileId(profileImage);
        baseBinaryContentRepository.save(profileImage);

        user.update(userUpdateRequestDto.newUsername(), userUpdateRequestDto.newEmail(), userUpdateRequestDto.newPassword());
        fileUserRepository.save(user);

        return new UserResponseDto(user.getProfileId(), user.getUsername(), user.getEmail(), StatusType.Active.toString());
    }

    @Override
    public void delete(UUID userId) {
        Optional<User> user = fileUserRepository.findById(userId);
        if (user.isPresent()) {
            user.get().softDelete();
            UserStatus userStatus = new UserStatus(Instant.now(), StatusType.Inactive.getExplanation());
            baseUserStatusRepository.save(userStatus);
            fileUserRepository.save(user.get());
        } else {
            throw new RequestException(ErrorCode.ALREADY_DELETED_USER);
        }
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
            User user = fileUserRepository.findByUsername(userLoginRequestDto.username())
                    .orElseThrow(() -> new AuthException(ErrorCode.UNAUTHORIZED));

            if (!user.getPassword().equals(userLoginRequestDto.password())) {
                throw new AuthException(ErrorCode.UNAUTHORIZED);
            }
            return new UserLoginResponseDto(user.getId().toString(), user.getUsername());
        }

}
