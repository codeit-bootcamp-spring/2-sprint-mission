package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserActivityService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserActivityService implements UserActivityService {

    private final UserRepository userRepository;

    @Transactional
    public void updateLastActiveAt(UUID userId, Instant lastActiveAt) {
        log.debug("활동 갱신: {}", userId);
        userRepository.updateLastActiveAt(userId, lastActiveAt);
    }
}
