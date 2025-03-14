package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserReadResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.DuplicateUserNameException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public User createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByUserName(userCreateRequest.userName())) {
            throw new DuplicateUserNameException(userCreateRequest.userName());
        }
        if (userRepository.existsByEmail(userCreateRequest.userEmail())) {
            throw new DuplicateEmailException(userCreateRequest.userEmail());
        }
        User newUser = new User(userCreateRequest.userName(), userCreateRequest.userEmail(), userCreateRequest.password(), userCreateRequest.profileId()); //각 요소에 대한 유효성 검증은 User 생성자에게 맡긴다
        this.userRepository.add(newUser);
        UserStatus newUserStatus = new UserStatus(newUser.getId());
        this.userStatusRepository.add(newUserStatus);
        return newUser;
    }

    @Override
    public UserReadResponse readUser(UUID userId) {
        User findUser = this.userRepository.findById(userId);
        UserStatus findUserStatus = this.userStatusRepository.findById(userId);
        return new UserReadResponse(
                findUser.getUserName(),
                findUser.getUserEmail(),
                findUser.getProfileId(),
                findUserStatus.isOnline());
    }

    @Override
    public List<UserReadResponse> readAllUsers() {
        return this.userRepository.getAll().values().stream()
                .map(user ->
                        new UserReadResponse(
                                user.getUserName(),
                                user.getUserEmail(),
                                user.getProfileId(),
                                this.userStatusRepository.findById(user.getId()).isOnline()
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User findUser = this.userRepository.findById(userUpdateRequest.userId());
        if (!findUser.getUserName().equals(userUpdateRequest.userName())) {
            this.userRepository.updateUserName(findUser.getId(), userUpdateRequest.userName());
        }
        if (!findUser.getPassword().equals(userUpdateRequest.password())) {
            this.userRepository.updatePassword(findUser.getId(), userUpdateRequest.password());
        }
        if (!findUser.getProfileId().equals(userUpdateRequest.profileId())) {
            this.userRepository.updateProfileId(findUser.getId(), userUpdateRequest.profileId());
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        User deleteUser = this.userRepository.findById(userId);
        this.userStatusRepository.deleteById(userStatusRepository.findUserStatusIDByUserId(deleteUser.getId()));
        this.binaryContentRepository.deleteById(deleteUser.getProfileId());
        this.userRepository.deleteById(userId);
    }
}
