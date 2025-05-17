package com.sprint.mission.discodeit.userstatus.service.basic;

import com.sprint.mission.discodeit.userstatus.dto.UserStatusResult;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Transactional
    @Override
    public UserStatusResult updateByUserId(UUID userId, Instant instant) {
        UserStatus userStatus = userStatusRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        userStatus.updateLastActiveAt(instant);
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);

        return UserStatusResult.fromEntity(updatedUserStatus, updatedUserStatus.isOnline(Instant.now()));
    }

}
