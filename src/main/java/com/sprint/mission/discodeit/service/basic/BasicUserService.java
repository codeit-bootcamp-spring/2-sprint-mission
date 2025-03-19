package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public UserDTO create(CreateUserParam createUserParam) {
        validateUserField(createUserParam);
        checkDuplicateUsername(createUserParam);
        checkDuplicateEmail(createUserParam);
        User user = createUserEntity(createUserParam);
        userRepository.save(user);
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);
        return UserMapper.userEntityToDTO(user, userStatus);
    }

    @Override
    public UserDTO find(UUID userId) {
        User findUser = findUserById(userId);
        UserStatus findUserStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + "유저의 UserStatus가 존재하지 않습니다."));
        return UserMapper.userEntityToDTO(findUser, findUserStatus);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        Map<UUID, UserStatus> userStatusMap = userStatusRepository.findAll().stream()
                .collect(Collectors.toMap(userStatus -> userStatus.getUserId(), userStatus -> userStatus));
        return users.stream()
                .map(user -> UserMapper.userEntityToDTO(user, userStatusMap.get(user.getId())))
                .toList();
    }

    @Override
    public UUID update(UUID userId, UpdateUserParam updateUserParam) {
        User findUser = findUserById(userId);
        findUser.updateUserInfo(updateUserParam.username(), updateUserParam.email(), updateUserParam.password());
        findUser.updateProfile(updateUserParam.profileId());
        userRepository.save(findUser);
        return userId;
    }

    @Override
    public void delete(UUID userId) {
        User user = findUserById(userId);
        userRepository.deleteById(userId);
        binaryContentRepository.deleteById(user.getProfileId());
        userStatusRepository.deleteByUserId(userId);
    }

    private void validateUserField(CreateUserParam createUserParam) {
        if (Stream.of(createUserParam.username(), createUserParam.email(), createUserParam.password())
                .anyMatch(field -> field == null || field.isBlank())) {
            throw new IllegalArgumentException("username, email, password는 필수 입력값입니다.");
        }
    }

    private void checkDuplicateUsername(CreateUserParam createUserParam) {
        if (userRepository.existsByUsername(createUserParam.username())) {
            throw new IllegalStateException("중복된 username입니다.");
        }
    }

    private void checkDuplicateEmail(CreateUserParam createUserParam) {
        if(userRepository.existsByEmail(createUserParam.email())) {
            throw new IllegalStateException("중복된 email입니다.");
        }
    }

    public static User createUserEntity(CreateUserParam createUserParam) {
        return User.builder()
                .username(createUserParam.username())
                .password(createUserParam.password())
                .email(createUserParam.email())
                .profileId(createUserParam.profileId())
                .build();
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));
    }


}
