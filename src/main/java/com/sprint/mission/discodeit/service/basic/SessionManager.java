package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.security.CustomUserDetails;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionManager {

    private final SessionRegistry sessionRegistry;

    public void invalidateSession(UUID userId) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof CustomUserDetails userDetails) {
                if (userDetails.getUserDto().id().equals(userId)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal,
                        false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
    }
}

