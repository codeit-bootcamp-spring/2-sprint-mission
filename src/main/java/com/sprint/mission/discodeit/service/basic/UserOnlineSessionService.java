package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.security.CustomUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOnlineSessionService {

    private final SessionRegistry sessionRegistry;

    public boolean isOnline(UUID userId) {
        return sessionRegistry.getAllPrincipals().stream()
            .filter(principal -> principal instanceof CustomUserDetails)
            .map(principal -> (CustomUserDetails) principal)
            .anyMatch(details -> details.getUserDto().id().equals(userId));
    }
}
