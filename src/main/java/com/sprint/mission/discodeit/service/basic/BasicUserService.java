package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserReadResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.DuplicateUserNameException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

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
        List<UserReadResponse> UserReadResponses = new ArrayList<>();
        for(User user : this.userRepository.getAll().values()) {
            UserStatus userStatus = this.userStatusRepository.findById(user.getId());
            UserReadResponse userReadResponse = new UserReadResponse(user.getUserName(), user.getUserEmail(), user.getProfileId(), userStatus.isOnline());
            UserReadResponses.add(userReadResponse);
        }
        return UserReadResponses;


    }

    @Override
    public void updateUserName(UUID userId, String newUserName) {
        this.userRepository.updateUserName(userId, newUserName);
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        this.userRepository.updatePassword(userId, newPassword);
    }

    @Override
    public void deleteUser(UUID userId) {
        this.userRepository.deleteById(userId);
    }
}
