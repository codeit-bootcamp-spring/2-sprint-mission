package com.sprint.mission.discodeit.domain.user.mapper;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserResultMapper {

    private final UserStatusRepository userStatusRepository;

    public UserResult convertToUserResult(User user) {
        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return UserResult.fromEntity(user, userStatus.isOnline(Instant.now()));
    }

    public List<UserResult> convertToUsersResult(List<User> users) {
        return users.stream()
                .map(user -> UserResult.fromEntity(user, user.getUserStatus().isOnline(Instant.now())))
                .toList();
    }

}
