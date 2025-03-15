package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.UserStatusType;
import com.sprint.mission.discodeit.dto.FindUserDto;
import com.sprint.mission.discodeit.dto.UserSaveDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserSaveDto save(String username, String password, String nickname, String email, byte[] profile) {
        if (userRepository.findUserByUsername(username).isPresent()) {
            System.out.println("[실패] 회원아이디 중복");
            return null;
        }

        if (userRepository.findUserByEmail(email).isPresent()) {
            System.out.println("[실패] 회원이메일 중복");
            return null;
        }

        UUID profileId = (profile != null) ? binaryContentRepository.save(profile).getId() : null;

        User user = userRepository.save(username, password, nickname, email, profileId);
        userStatusRepository.save(user.getId());

        if (user == null) {
            System.out.println("[실패] 저장 실패.");
            return null;
        }

        System.out.println("[성공] 회원가입 성공");
        UserSaveDto userSaveDto = new UserSaveDto(user.getId(), user.getNickname(), user.getProfile(), user.getCreatedAt());
        return userSaveDto;
    }

    @Override
    public FindUserDto findByUser(UUID userUUID) {
        UserStatusType userStatusType = UserStatusType.CONNECTING;
        User user = userRepository.findUserById(userUUID)
                .orElseGet(() -> {
                    System.out.println("[실패] 사용자가 존재하지 않습니다.");
                    return null;
                });

        UserStatus userStatus = userStatusRepository.findByUser(userUUID)
                .orElseGet(() -> {
                    System.out.println("[실패] 접속 상태 오류");
                    return null;
                });

        if (user == null || userStatus == null) {
            System.out.println("[실패] 잘못된 찾기");
            return null;
        }

        if (userStatus.getUpdatedAt() == null ||
                Duration.between(userStatus.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant(),
                        Instant.now()).toMinutes() >= 5) {
            userStatusType = UserStatusType.NOTCONNECTIED;
        }

        FindUserDto findUserDto = new FindUserDto(user.getId(), user.getNickname(), userStatus.getUpdatedAt(), userStatusType);

        return findUserDto;
    }

    @Override
    public List<User> findAllUser() {
        List<User> userList = userRepository.findAllUser();

        if (userList.isEmpty()) {
            System.out.println("회원이 존재하지 않습니다");
        }
        return userList;
    }

    @Override
    public void update(UUID userUUID, String nickname) {
        User user = userRepository.updateUserNickname(userUUID, nickname);
        if (user == null) {
            System.out.println("[실패] 닉네임 변경을 실패하였습니다.");
            return;
        }
        System.out.println("[성공]" + user);

    }

    @Override
    public void delete(UUID userUUID) {
        boolean isDeleted = userRepository.deleteUserById(userUUID);
        if (!isDeleted) {
            System.out.println("삭제 실패");
            return;
        }
        userStatusRepository.delete(userUUID);
        System.out.println("[성공]");
    }
}
