package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.constant.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.FilePath.CHANNEL_TEST_FILE;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constant.FilePath.USER_TEST_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileChannelServiceTest {
    private Path userTestFilePath;
    private Path channelTestFilePath;
    private UserService userService;
    private ChannelService channelService;
    private ChannelDto initializedChannel;

    @BeforeEach
    void setUp() {
        setUpTestFilePath();
        setUpService();
        setUpChannel(setUpUser());
    }

    private void setUpTestFilePath() {
        String random = UUID.randomUUID().toString();
        userTestFilePath = STORAGE_DIRECTORY.resolve(random + USER_TEST_FILE);
        channelTestFilePath = STORAGE_DIRECTORY.resolve(random + CHANNEL_TEST_FILE);
    }

    private void setUpService() {
        FileUserRepository userRepository = new FileUserRepository();
        userRepository.changePath(userTestFilePath);

        FileChannelRepository channelRepository = new FileChannelRepository();
        channelRepository.changePath(channelTestFilePath);

        userService = new FileUserService(userRepository);
        channelService = new FileChannelService(channelRepository, userRepository);
    }

    private void setUpChannel(UserDto loginUser) {
        initializedChannel = channelService.create(CHANNEL_NAME,
                new UserDto(loginUser.id(), loginUser.name(), loginUser.email(), null));
    }

    private UserDto setUpUser() {
        UserRegisterDto user = new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(),
                LONGIN_USER.getPassword(), null);

        return userService.register(user);
    }

    @AfterEach
    void tearDown() throws IOException {
        initializeFiles();
    }

    private void initializeFiles() throws IOException {
        Files.deleteIfExists(userTestFilePath);
        Files.deleteIfExists(channelTestFilePath);
    }

    @DisplayName("친구의 이메일을 통해 같은 채널에 멤버를 추가합니다")
    @Test
    void addMember() {
        UserDto otherUser = userService.register(new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword(), null));

        ChannelDto channelDto = channelService.addMember(initializedChannel.id(), OTHER_USER.getEmail());
        assertThat(channelDto.usersDto().users()).contains(otherUser);
    }

    @DisplayName("로그인된 유저와 채널 이름을 통해 채널 생성합니다")
    @Test
    void create() {
        assertThat(initializedChannel.name()).isEqualTo(CHANNEL_NAME);
    }

    @DisplayName("채널 ID를 바탕으로 해당채널을 찾아서 반환합니다")
    @Test
    void findById() {
        ChannelDto channel = channelService.findById(initializedChannel.id());
        assertThat(initializedChannel.id() + initializedChannel.name()).isEqualTo(channel.id() + channel.name());
    }

    @DisplayName("채널 이름을 변경합니다")
    @Test
    void updateName() {
        channelService.updateName(initializedChannel.id(), UPDATED_CHANNEL_NAME);

        assertThat(channelService.findById(initializedChannel.id()).name())
                .isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @DisplayName("채널을 삭제합니다")
    @Test
    void delete() {
        UUID id = initializedChannel.id();
        channelService.delete(id);

        assertThatThrownBy(() -> channelService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}