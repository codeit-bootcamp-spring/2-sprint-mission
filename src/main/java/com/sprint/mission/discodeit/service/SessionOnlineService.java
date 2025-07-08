package com.sprint.mission.discodeit.service;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionOnlineService {

    private final SessionRegistry sessionRegistry;

    public boolean isUserOnline(String username) {
        return sessionRegistry.getAllPrincipals().stream()
            .filter(UserDetails.class::isInstance)
            .map(UserDetails.class::cast)
            .anyMatch(
                user -> user.getUsername().equals(username)
                    && !sessionRegistry.getAllSessions(user, false).isEmpty());
    }

    // 시큐리티는 object형을 반환-> 사용자마다 타입이 다를 경우 (익명,커스텀) 받아서 캐스팅 해야함

    public Set<String> getAllOnlineUser() {
        return sessionRegistry.getAllPrincipals().stream()
            .filter(UserDetails.class::isInstance)
            .map(UserDetails.class::cast)
            .map(UserDetails::getUsername)
            .collect(Collectors.toSet());
    }
}
