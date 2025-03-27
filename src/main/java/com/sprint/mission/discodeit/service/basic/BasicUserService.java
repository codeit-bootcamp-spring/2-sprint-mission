package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserRequest;
import com.sprint.mission.discodeit.dto.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.UserInfoDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    private void saveUser() {
        userRepository.save();
    }

    @Override
    public User createUser(CreateUserRequest request) {
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 Email입니다");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 Username입니다");
        }

        User user = new User(request.getUsername(), request.getEmail(), hashedPassword);
        userRepository.addUser(user);
        userStatusRepository.addUserStatus(new UserStatus(user.getId()));

        return user;
    }

    @Override
    public UserInfoDto getUserById(UUID userId) {
        return mapToUserFindDto(userRepository.findUserById(userId));
    }

    @Override
    public String getUserNameById(UUID userId) {
        return userRepository.findUserById(userId).getUsername();
    }

    @Override
    public List<UserInfoDto> findUsersByIds(Set<UUID> userIds) {
        return userRepository.findUsersByIds(userIds).stream()
                .map(this::mapToUserFindDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInfoDto> getAllUsers() {
        return userRepository.findUserAll().stream()
                .map(this::mapToUserFindDto)
                .collect(Collectors.toList());
    }

    @Override
    public BinaryContent findProfileById(UUID userId) {
        return Optional.ofNullable(userRepository.findUserById(userId))
                .map(User::getProfileId)
                .map(binaryContentRepository::findBinaryContentById)
                .orElse(null);
    }

    @Override
    public void updateProfile(UUID userId, UUID profileId) {
        userRepository.findUserById(userId).updateProfile(profileId);
    }

    @Override
    public void updateUser(UUID userId, UpdateUserRequest request) {
        validateUserExists(userId);
        User user = userRepository.findUserById(userId);

        if (request.getUsername() != null) user.updateUsername(request.getUsername());
        if (request.getPassword() != null) user.updatePassword(request.getPassword());
        if (request.getEmail() != null) user.updateEmail(request.getEmail());
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteUserById(userId);
        userStatusRepository.deleteUserStatusById(userId);
        binaryContentRepository.deleteBinaryContentById(userId);
    }

    @Override
    public void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
    }

    private UserInfoDto mapToUserFindDto(User user) {
        UserInfoDto dto = new UserInfoDto();
        dto.setUserid(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(userStatusRepository.findUserStatusById(user.getId()).getStatus()); // ✅ 온라인 상태 추가
        return dto;
    }


}
