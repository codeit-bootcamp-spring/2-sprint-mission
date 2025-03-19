package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateParam;
import com.sprint.mission.discodeit.service.dto.UserCreateParam;
import com.sprint.mission.discodeit.service.dto.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.UserStatusParam;
import com.sprint.mission.discodeit.service.dto.UserUpdateParam;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusService userStatusService; // 레파지토리로 변경해야 함.
    private final BasicBinaryContentService basicBinaryContentService; // 얘도..ㅎㅎ

    public BasicUserService(@Qualifier("fileUserRepository") UserRepository userRepository,
                            UserStatusService userStatusService,
                            BasicBinaryContentService basicBinaryContentService) {
        this.userRepository = userRepository;
        this.userStatusService = userStatusService;
        this.basicBinaryContentService = basicBinaryContentService;
    }

    private void duplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(username + " 은 중복된 username.");
        }
    }

    private void duplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(email + " 은 중복된 email.");
        }
    }

    private UUID profileCreate(BinaryContentType type, List<MultipartFile> file) {
        BinaryContentCreateParam binaryContentCreateParam = new BinaryContentCreateParam(type, file);
        List<UUID> idList = basicBinaryContentService.create(binaryContentCreateParam);
        return idList.get(0);
    }

    @Override
    public User create(UserCreateParam createParam) {
        duplicateUsername(createParam.getUsername());
        duplicateEmail(createParam.getEmail());
        UUID binaryContentId = null;

        if (createParam.getType() != null && createParam.getFile() != null && !createParam.getFile().isEmpty()) {
            binaryContentId = profileCreate(createParam.getType(), List.of(createParam.getFile()));
        }

        User user = new User(createParam.getUsername(), createParam.getEmail(), createParam.getPassword(),
                binaryContentId);

        UserStatusParam statusParam = new UserStatusParam(user.getId(), UserStatusType.ONLINE);
        userStatusService.create(statusParam);

        return userRepository.save(user);
    }

    @Override
    public UserInfoResponse find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 userStatus를 찾을 수 없음"));
        UserStatus userStatus = userStatusService.findByUserId(user.getId());

        return new UserInfoResponse(user.getId(),
                user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
                user.getEmail(), user.getProfileId(), userStatus.getStatus());
    }

    @Override
    public List<UserInfoResponse> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusService.findByUserId(user.getId());
                    return new UserInfoResponse(
                            user.getId(), user.getCreatedAt(),
                            user.getUpdatedAt(), user.getUsername(),
                            user.getEmail(), user.getProfileId(),
                            userStatus.getStatus()
                    );
                }).toList();
    }

    @Override
    public User update(UserUpdateParam updateParam) {
        duplicateUsername(updateParam.getNewUsername());
        duplicateEmail(updateParam.getNewEemail());

        User user = userRepository.findById(updateParam.getId())
                .orElseThrow(() -> new NoSuchElementException(updateParam.getId() + " 에 해당하는 userStatus를 찾을 수 없음"));
        UUID binaryContentId = user.getProfileId();

        if (updateParam.getNewType() != null && updateParam.getNewFile() != null
                && !updateParam.getNewFile().isEmpty()) {
            if (binaryContentId != null) {
                basicBinaryContentService.delete(user.getProfileId());
            }
            binaryContentId = profileCreate(updateParam.getNewType()
                    , List.of(updateParam.getNewFile()));
        }

        user.update(updateParam.getNewUsername(), updateParam.getNewEemail()
                , updateParam.getNewPassword(), binaryContentId);

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException(userId + " 에 해당하는 userStatus를 찾을 수 없음");
        }
        basicBinaryContentService.delete(userId);
        UserStatus userStatus = userStatusService.findByUserId(userId);
        userStatusService.delete(userStatus.getId());

        userRepository.deleteById(userId);
    }
}
