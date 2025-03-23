package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.Optional;

@Service
public class AuthService implements com.sprint.mission.discodeit.service.AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Autowired
    public AuthService(
            @Qualifier("basicUserRepository") UserRepository userRepository, 
            @Qualifier("basicUserStatusRepository") UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserDto.Response login(UserDto.Login loginDto) {
        // 이메일과 비밀번호 검증
        Optional<User> userOptional = userRepository.findByEmail(loginDto.getEmail());
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            if (user.checkPassword(loginDto.getPassword())) {
                // 로그인 성공
                return UserDto.Response.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .build();
            }
        }
        
        return null;
    }
    private void updateUserLastSeen(UUID userId) {
        UserStatus userStatus = userStatusRepository.findById(userId)
                .orElseGet(() -> {
                    UserStatus newStatus = new UserStatus(userId);
                    userStatusRepository.register(newStatus);
                    return newStatus;
                });
                
        // 마지막 접속 시간 업데이트
        userStatus.setLastSeenAt(ZonedDateTime.now());
        userStatusRepository.update(userStatus);
    }
}

