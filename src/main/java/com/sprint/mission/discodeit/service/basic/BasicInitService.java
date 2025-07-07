package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.InitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicInitService implements InitService {

    @Value("${discodeit.admin.username}")
    private String adminUsername;

    @Value("${discodeit.admin.password}")
    private String adminPassword;

    @Value("${discodeit.admin.email}")
    private String adminEmail;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createAdminIfNotExists() {

        if (userRepository.existsByUsername(adminUsername) || userRepository.existsByEmail(adminEmail)) {
            log.info("어드민 계정이 이미 존재 합니다.");
            return null;
        }

        String encodedPassword = passwordEncoder.encode(adminPassword);

        User admin = User.adminUser(
            adminUsername,
            encodedPassword,
            adminEmail
        );

        userRepository.saveAndFlush(admin);

        UserDto adminDto = userMapper.toDto(admin);
        log.info("어드민 계정 초기화 완료: {}", adminDto);
        return adminDto;
    }
}
