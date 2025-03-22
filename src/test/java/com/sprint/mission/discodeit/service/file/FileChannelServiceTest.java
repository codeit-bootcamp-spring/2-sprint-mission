package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.channeldto.ChannelDto;
import com.sprint.mission.discodeit.application.channeldto.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.application.userdto.UserRegisterDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.FilePath.*;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileChannelServiceTest {
    private Path userTestFilePath;
    private Path channelTestFilePath;
    private UserService userService;
    private ChannelService channelService;
    private ChannelDto initializedChannel;
    private ReadStatusRepository readStatusRepository;

    @BeforeEach
    void setUp() {
        setUpTestFilePath();
        setUpService();
        setUpChannel(setUpUser());
    }

    private void setUpTestFilePath() {
        userTestFilePath = STORAGE_DIRECTORY.resolve(USER_TEST_FILE);
        channelTestFilePath = STORAGE_DIRECTORY.resolve(CHANNEL_TEST_FILE);
    }

    private void setUpService() {
        FileUserRepository userRepository = new FileUserRepository();
        userRepository.changePath(userTestFilePath);

        FileChannelRepository channelRepository = new FileChannelRepository();
        channelRepository.changePath(channelTestFilePath);

        userService = new FileUserService(userRepository);
        readStatusRepository = new JCFReadStatusRepository();
        channelService = new FileChannelService(channelRepository, readStatusRepository, new JCFMessageRepository());
    }

    private void setUpChannel(UserDto loginUser) {
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PUBLIC, CHANNEL_NAME, new UserDto(loginUser.id(), loginUser.name(), loginUser.email(), null, false));
        initializedChannel = channelService.create(channelRegisterDto);
    }

    private UserDto setUpUser() {
        UserRegisterDto user = new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(),
                LOGIN_USER.getPassword());

        return userService.register(user, null);
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
                OTHER_USER.getPassword()), null);

        ChannelDto channelDto = channelService.addMemberToPrivate(initializedChannel.id(), otherUser.id());
        List<UUID> channelUsers = readStatusRepository.findByChannelId(channelDto.id())
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        assertThat(channelUsers).contains(otherUser.id());
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