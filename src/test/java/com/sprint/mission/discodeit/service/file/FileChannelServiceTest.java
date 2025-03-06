package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.config.FilePath.CHANNEL_FILE;
import static com.sprint.mission.config.FilePath.USER_FILE;
import static com.sprint.mission.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.config.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.constants.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.constants.ChannelInfo.UPDATED_CHANNEL_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileChannelServiceTest {
    private final UserService userService = new FileUserService();
    private final ChannelService channelService = new FileChannelService(userService);
    private ChannelDto setUpChannel;

    @BeforeEach
    void setUp() {
        UserDto loginUser = userService.register(
                new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword()));

        setUpChannel = channelService.create(CHANNEL_NAME,
                new UserDto(loginUser.id(), loginUser.name(), loginUser.email()));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(CHANNEL_FILE.getPath());
        Files.deleteIfExists(USER_FILE.getPath());
    }

    @DisplayName("친구의 이메일을 통해 같은 채널에 멤버를 추가합니다")
    @Test
    void addMember() {
        UserDto otherUser = userService.register(new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword()));

        ChannelDto channelDto = channelService.addMember(setUpChannel.id(), OTHER_USER.getEmail());
        assertThat(channelDto.users()).contains(otherUser);
    }

    @DisplayName("로그인된 유저와 채널 이름을 통해 채널 생성합니다")
    @Test
    void create() {
        assertThat(setUpChannel.name()).isEqualTo(CHANNEL_NAME);
    }

    @DisplayName("채널 ID를 바탕으로 해당채널을 찾아서 반환합니다")
    @Test
    void findById() {
        ChannelDto channel = channelService.findById(setUpChannel.id());
        assertThat(setUpChannel.id() + setUpChannel.name()).isEqualTo(channel.id() + channel.name());
    }

    @DisplayName("채널 이름을 변경합니다")
    @Test
    void updateName() {
        channelService.updateName(setUpChannel.id(), UPDATED_CHANNEL_NAME);

        assertThat(channelService.findById(setUpChannel.id()).name())
                .isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @DisplayName("채널을 삭제합니다")
    @Test
    void delete() {
        UUID id = setUpChannel.id();
        channelService.delete(id);

        assertThatThrownBy(() -> channelService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}