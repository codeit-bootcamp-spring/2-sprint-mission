package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserReadResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUserNameException;
import com.sprint.mission.discodeit.exception.binarycontent.FileIdNotFoundException;
import com.sprint.mission.discodeit.provider.UserUpdaterProvider;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.updater.user.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentService binaryContentService;
    private final UserUpdaterProvider userUpdaterProvider;

    // 이 부분도 createMessage처럼 컨트롤러에서 createBinaryContent 와 userCreate을 분리하는게 좋을듯
    @Override
    public UUID createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByUserName(userCreateRequest.userName())) {
            throw new DuplicateUserNameException(userCreateRequest.userName());
        }
        if (userRepository.existsByEmail(userCreateRequest.userEmail())) {
            throw new DuplicateEmailException(userCreateRequest.userEmail());
        }
        if (!binaryContentService.existsById(userCreateRequest.profileId())) {
            throw new FileIdNotFoundException(userCreateRequest.profileId());
        }
        User newUser = new User(userCreateRequest.userName(), userCreateRequest.userEmail(), userCreateRequest.password(), userCreateRequest.profileId()); //각 요소에 대한 유효성 검증은 User 생성자에게 맡긴다
        this.userRepository.add(newUser);
        this.readStatusRepository.addUserIdMap(newUser.getId());
        UserStatus newUserStatus = new UserStatus(newUser.getId());
        this.userStatusRepository.add(newUserStatus);
        return newUser.getId();
    }

    @Override
    public UserReadResponse readUser(UUID userId) {
        User findUser = this.userRepository.findById(userId);
        UserStatus findUserStatus = this.userStatusRepository.findUserStatusByUserId(userId);
        return new UserReadResponse(
                findUser.getId(),
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
                                user.getId(),
                                user.getUserName(),
                                user.getUserEmail(),
                                user.getProfileId(),
                                this.userStatusRepository.findUserStatusByUserId(user.getId()).isOnline()
                        )
                )
                .collect(Collectors.toList());
    }

    // binaryContent를 업데이트할지 다음 미션의 컨트롤러에서 결정할 것!
    @Override
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User findUser = this.userRepository.findById(userUpdateRequest.userId());
        List<UserUpdater> applicableUpdaters = userUpdaterProvider.getApplicableUpdaters(findUser, userUpdateRequest);
        applicableUpdaters.forEach(updater -> updater.update(userUpdateRequest, this.userRepository));
    }

    @Override
    public void deleteUser(UUID userId) {
        User deleteUser = this.userRepository.findById(userId);
        this.userStatusRepository.deleteById(userStatusRepository.findUserStatusByUserId(deleteUser.getId()).getId());
        this.readStatusRepository.deleteByUserId(userId);
        this.binaryContentService.deleteByID(deleteUser.getProfileId());
        this.userRepository.deleteById(userId);
    }
}
