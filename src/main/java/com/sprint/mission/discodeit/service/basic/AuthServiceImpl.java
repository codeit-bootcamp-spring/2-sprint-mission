package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthDto;
import com.sprint.mission.discodeit.dto.UserFindDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public UserFindDto login(AuthDto authDto) {
        User user = userRepository.findUserByName(authDto.getUsername());
        if (user == null) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }

        if (!user.getPassword().equals(authDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return mapToUserFindDto(user);
    }

    private UserFindDto mapToUserFindDto(User user) {
        UserFindDto dto = new UserFindDto();
        dto.setUserid(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(UserStatusType.Online);
        return dto;
    }
}
