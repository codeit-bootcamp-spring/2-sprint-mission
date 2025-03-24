package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.UserResponseDto;
import com.sprint.mission.discodeit.service.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void createUser(UserCreateDto createDto) {
        UUID profileId = null;
        if(StringUtils.hasText(createDto.filePath())) {
            profileId = binaryContentRepository.createBinaryContent(createDto.convertDtoToBinaryContent());
        }

        UUID userId = userRepository.createUser(createDto.convertDtoToUser(profileId));
        userStatusRepository.createUserStatus(new UserStatus(userId));
    }

    @Override
    public UserResponseDto findById(UUID id) {
        User user = userRepository.findById(id);
        boolean isOnline = checkUserOnlineStatus(id);

        return UserResponseDto.convertToResponseDto(user, isOnline);
    }

    @Override
    public UserResponseDto findByNickname(String nickname) {
//        if (nickname == null) {
//            return Optional.empty();
//        }
        User user = userRepository.findByNickname(nickname);
        boolean isOnline = checkUserOnlineStatus(user.getId());

        return UserResponseDto.convertToResponseDto(user, isOnline);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
//        if (email == null) {
//            return Optional.empty();
//        }
        User user = userRepository.findByEmail(email);
        boolean isOnline = checkUserOnlineStatus(user.getId());

        return UserResponseDto.convertToResponseDto(user, isOnline);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    boolean isOnline = checkUserOnlineStatus(user.getId());
                    return UserResponseDto.convertToResponseDto(user, isOnline);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(UserUpdateDto updateDto) {
        UUID profileId = null;
        if (StringUtils.hasText(updateDto.filePath())) {
            if (updateDto.profileId() != null) {
                binaryContentRepository.deleteBinaryContent(updateDto.profileId());
            }
            profileId = binaryContentRepository.createBinaryContent(updateDto.convertDtoToBinaryContent());
        } else {
            profileId = updateDto.profileId();
        }

        userRepository.updateUser(updateDto.id(), updateDto.password(), updateDto.nickname(), updateDto.status(), updateDto.role(), profileId);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id);

        userRepository.deleteUser(id);
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteBinaryContent(user.getProfileId());
        }
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
        userStatusRepository.deleteUserStatus(userStatus.getId());
    }

    /****************************
     * Validation check
     ****************************/
    private boolean checkUserOnlineStatus(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        return userStatus.isOnline();
    }

}
