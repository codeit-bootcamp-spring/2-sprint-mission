package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.user.request.UserCreateDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TestData {
    private final UserService userService;
    private final ChannelService channelService;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        userService.create(new UserCreateDto("user123", "Password12!@", "user@example1.com", null, null));
        userService.create(new UserCreateDto("user1234", "Password12!@", "user1@example1.com", null, null));

    }
}
