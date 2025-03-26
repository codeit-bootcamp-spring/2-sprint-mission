package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.binaryContent.CreateBinaryContentParam;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.dto.service.userStatus.UserStatusDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;


    @Override
    public UserDTO create(CreateUserParam createUserParam, MultipartFile multipartFile) {
        validateUserField(createUserParam);
        checkDuplicateUsername(createUserParam);
        checkDuplicateEmail(createUserParam);
        BinaryContent binaryContent = createBinaryContentEntity(multipartFile);
        if (binaryContent != null) {
            binaryContentService.create(binaryContent);
        }
        User user = createUserEntity(createUserParam, binaryContent);
        userRepository.save(user);
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusService.create(userStatus);
        return UserMapper.userEntityToDTO(user, userStatus);
    }

    @Override
    public UserDTO find(UUID userId) {
        User findUser = findUserById(userId);
        UserStatus findUserStatus = userStatusService.findById(userId);
        return UserMapper.userEntityToDTO(findUser, findUserStatus);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        // UserDTO에 isLogin 정보를 담기 위함
        // UserStatusMap을 만들고, User와 해당하는 UserStatus 객체를 userEntityToDTO 메서드에 넘겨줌
        // userEntityDTO에서 userStatus.isLoginUser() 메서드를 실행시켜 isLogin에 대한 정보를 생성
        Map<UUID, UserStatus> userStatusMap = userStatusService.findAll().stream()
                .collect(Collectors.toMap(userStatus -> userStatus.getUserId(), userStatus -> userStatus));
        return users.stream()
                .map(user -> UserMapper.userEntityToDTO(user, userStatusMap.get(user.getId())))
                .toList();
    }

    @Override
    public UUID update(UUID userId, UpdateUserParam updateUserParam, MultipartFile multipartFile) {
        User findUser = findUserById(userId);
        findUser.updateUserInfo(updateUserParam.username(), updateUserParam.email(), updateUserParam.password());
        if (multipartFile != null) { // 프로필 변경 요청이 있을 때만 업데이트
            // 기본 프로필 ID가 아닐 때만 기존 이미지 삭제
            if (!findUser.getProfileId().equals(User.DEFAULT_PROFILE_ID)) {
                binaryContentService.delete(findUser.getProfileId());
            }
            BinaryContent binaryContent = createBinaryContentEntity(multipartFile);
            BinaryContent createdBinaryContent = binaryContentService.create(binaryContent);
            findUser.updateProfile(createdBinaryContent.getId());
        }
        userRepository.save(findUser);
        return userId;
    }

    @Override
    public void delete(UUID userId) {
        User user = findUserById(userId);
        userRepository.deleteById(userId);
        // 기본 프로필 ID가 아닐 때만 삭제
        if (!user.getProfileId().equals(User.DEFAULT_PROFILE_ID)) {
           binaryContentService.delete(user.getProfileId());
        }
        userStatusService.delete(userId);
    }

    private void validateUserField(CreateUserParam createUserParam) {
        if (Stream.of(createUserParam.username(), createUserParam.email(), createUserParam.password())
                .anyMatch(field -> field == null || field.isBlank())) {
            throw RestExceptions.BAD_REQUEST;
        }
    }

    private void checkDuplicateUsername(CreateUserParam createUserParam) {
        if (userRepository.existsByUsername(createUserParam.username())) {
            throw RestExceptions.DUPLICATE_USERNAME;
        }
    }

    private void checkDuplicateEmail(CreateUserParam createUserParam) {
        if (userRepository.existsByEmail(createUserParam.email())) {
            throw RestExceptions.DUPLICATE_EMAIL;
        }
    }

    public static User createUserEntity(CreateUserParam createUserParam, BinaryContent binaryContent) {
        return User.builder()
                .username(createUserParam.username())
                .password(createUserParam.password())
                .email(createUserParam.email())
                .profileId(binaryContent != null ? binaryContent.getId() : User.DEFAULT_PROFILE_ID)
                .build();
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> RestExceptions.USER_NOT_FOUND);
    }

    private BinaryContent createBinaryContentEntity(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;  // 파일이 없으면 BinaryContent를 생성하지 않음
        }

        try {
            return BinaryContent.builder()
                    .contentType(multipartFile.getContentType())
                    .bytes(multipartFile.getBytes())
                    .size(multipartFile.getSize())
                    .filename(multipartFile.getOriginalFilename())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage(), e);
        }
    }

}
