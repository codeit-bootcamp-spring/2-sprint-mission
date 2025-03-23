package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusDto create(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        userStatusRepository.findByUserId(userId)
                .ifPresent(status -> {
                    throw new IllegalArgumentException("해당 유저의 상태가 이미 존재합니다.");
                });

        UserStatus userStatus = userStatusRepository.save(new UserStatus(userId));

        return UserStatusDto.fromEntity(userStatus);
    }
}
