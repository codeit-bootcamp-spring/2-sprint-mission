package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.discodeit.config.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.config.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.constants.FilePath.CHANNEL_FILE;
import static com.sprint.mission.discodeit.constants.FilePath.USER_FILE;
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
import org.junit.jupiter.api.Test;

class FileChannelServiceTest {
    private ChannelService channelService;
    private UserService userService;
    private ChannelDto setUpChannel;
    private UserDto setUpUser;

    @BeforeEach
    void setUp() {
        userService = new FileUserService();
        channelService = new FileChannelService(userService);

        setUpUser = userService.register(
                new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword()));

        setUpChannel = channelService.create(CHANNEL_NAME,
                new UserDto(setUpUser.id(), setUpUser.name(), setUpUser.email()));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(CHANNEL_FILE.getPath());
        Files.deleteIfExists(USER_FILE.getPath());
    }

    @Test
    void addMember() {
        UserDto otherUser = userService.register(new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword()));

        ChannelDto channelDto = channelService.addMember(setUpChannel.id(), OTHER_USER.getEmail());
        assertThat(channelDto.users()).contains(otherUser);
    }

    @Test
    void create() {
        assertThat(setUpChannel.name()).isEqualTo(CHANNEL_NAME);
    }

    @Test
    void findById() {
        ChannelDto channel = channelService.findById(setUpChannel.id());
        assertThat(setUpChannel.id() + setUpChannel.name()).isEqualTo(channel.id() + channel.name());
    }

    @Test
    void updateName() {
        channelService.updateName(setUpChannel.id(), UPDATED_CHANNEL_NAME);

        assertThat(channelService.findById(setUpChannel.id()).name())
                .isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @Test
    void delete() {
        UUID id = setUpChannel.id();
        channelService.delete(id);

        assertThatThrownBy(() -> channelService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}