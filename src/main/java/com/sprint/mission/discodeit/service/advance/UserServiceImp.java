package com.sprint.mission.discodeit.service.advance;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDeleteRequest;
import com.sprint.mission.discodeit.dto.user.UserIsOnlineResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    // 1. 유저 생성 (프로필 이미지 포함)
    // 2. 유저 조회 (패스워드 제외, 온라인 상태 포함)
    // 3. 유저 전체 조회
    // 4. 유저 정보 업데이트 (선택적으로 프로필 이미지 변경)
    // 5. 유저 삭제 (관련 데이터도 삭제)

    @Override
    public User create(UserCreateRequest userCreateRequest) {
        // 1. username과 email 중복 검사
        validateUniqueUser(userCreateRequest.username(), userCreateRequest.email());

        // 2. 선택적으로 프로필 이미지 저장 및 객체 생성 (binaryContentRepository에 저장)
        UUID profileId = null;
        if (userCreateRequest.profileImage() != null && userCreateRequest.profileImage().length > 0) {
            profileId = saveImage(userCreateRequest.profileImage(), "profileImage");
        }

        // 3. 유저 객체 생성
        User user = new User(userCreateRequest.username(), userCreateRequest.email(), userCreateRequest.password(), profileId);

        // 4. UserStatus 생성 (유저 온라인 상태 관리), 현재 시각을 생성과 마지막 시간으로 적용
        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        return userRepository.save(user);
    }

    @Override
    public UserIsOnlineResponse find(UUID userId) {
        // 1. 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        // 2. 사용자 온라인 상태 조회 (마지막 시간이 300초 이내라면 true)
        boolean isOnline = userStatusRepository.findByUserId(userId)
                .map(userStatus -> userStatus.getLastActiveAt().isAfter(Instant.now().minusSeconds(300)))
                .orElse(false);

        // 3. DTO 생성해서 내용 담고 반환
        return new UserIsOnlineResponse(user.getId(), isOnline);
    }

    @Override
    public List<UserIsOnlineResponse> findAll() {
        // 1. 전체 사용자 조회 (비밀번호 제외)
        List<User> users = userRepository.findAll();

        // 2. DTO 변환하여 반환 (각 사용자에 대해 온라인 상태 조회)
        return users.stream()
                .map(user -> {
                    boolean isOnline = userStatusRepository.findByUserId(user.getId())
                            .map(userStatus -> userStatus.getLastActiveAt().isAfter(Instant.now().minusSeconds(300)))
                            .orElse(false);
                    return new UserIsOnlineResponse(user.getId(), isOnline);
                })
                .collect(Collectors.toList());
    }

    @Override
    public User update(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.userId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + userUpdateRequest.userId() + " not found"));

        UUID newProfileId = null;
        if (userUpdateRequest.newProfileImage() != null && userUpdateRequest.newProfileImage().length > 0) {
            newProfileId = saveImage(userUpdateRequest.newProfileImage(), "attachmentContent");
        }

        user.updateUserInfo(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword(), newProfileId);
        return userRepository.save(user);
    }

    @Override
    public void delete(UserDeleteRequest userDeleteRequest) {
        if (!userRepository.existsById(userDeleteRequest.userId())) {
            throw new NoSuchElementException("User with id " + userDeleteRequest.userId() + " not found");
        }

        // 관련 도메인들도 삭제 (Binary Content, UserStatus)
        userRepository.deleteById(userDeleteRequest.userId());
        binaryContentRepository.deleteByUserId(userDeleteRequest.userId());
        userStatusRepository.deleteByUserId(userDeleteRequest.userId());
    }

    private void validateUniqueUser(String username, String email) {
        boolean existingUsername = userRepository.findByUsername(username);
        boolean existingEmail = userRepository.findByEmail(email);
        // 하나라도 중복되면, 예외 문구 던지기
        if (existingUsername || existingEmail) {
            throw new IllegalArgumentException("이미 사용 중인 username 또는 email입니다.");
        }
    }

    private UUID saveImage(byte[] profileImage, String type) {
        BinaryContent binaryContent = new BinaryContent(profileImage, type);
        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
        return savedBinaryContent.getId(); //profileId or attachmentId
    }
}
