package com.sprint.mission.discodeit.domain.userstatus.service.basic;

import com.sprint.mission.discodeit.domain.userstatus.dto.UserStatusResult;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.exception.UserStatusNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Transactional
    @Override
    public UserStatusResult updateByUserId(UUID userId, Instant instant) {
        UserStatus userStatus = userStatusRepository.findByUser_Id(userId)
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", userId)));

        userStatus.updateLastActiveAt(instant);
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);

        return UserStatusResult.fromEntity(updatedUserStatus, updatedUserStatus.isOnline(Instant.now()));
    }

}
