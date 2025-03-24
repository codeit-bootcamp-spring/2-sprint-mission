package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
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
    private FileUserRepository userRepository;
    private FileChannelRepository channelRepository;
    private JCFMessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        userTestFilePath = STORAGE_DIRECTORY.resolve(USER_TEST_FILE);
        channelTestFilePath = STORAGE_DIRECTORY.resolve(CHANNEL_TEST_FILE);

        userRepository = new FileUserRepository();
        channelRepository = new FileChannelRepository();
        messageRepository = new JCFMessageRepository();
        readStatusRepository = new JCFReadStatusRepository();

        userRepository.changePath(userTestFilePath);
        channelRepository.changePath(channelTestFilePath);

        userService = new FileUserService(userRepository);
        channelService = new FileChannelService(channelRepository, readStatusRepository, messageRepository);

        setUpChannel(setUpUser());
    }

    private void setUpChannel(UserDto loginUser) {
        ChannelRegisterDto channelRegisterDto = new ChannelRegisterDto(ChannelType.PUBLIC, CHANNEL_NAME,
                new UserDto(loginUser.id(), loginUser.name(), loginUser.email(), null));
        initializedChannel = channelService.create(channelRegisterDto);
    }

    private UserDto setUpUser() {
        return userService.register(new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(),
                LOGIN_USER.getPassword()), null);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(userTestFilePath);
        Files.deleteIfExists(channelTestFilePath);
    }

    @DisplayName("친구의 이메일을 통해 같은 채널에 멤버를 추가하면 멤버 리스트에 포함된다.")
    @Test
    void addMemberToChannel() {
        UserDto otherUser = userService.register(new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword()), null);

        ChannelDto channelDto = channelService.addMemberToPrivate(initializedChannel.id(), otherUser.id());
        List<UUID> channelUsers = readStatusRepository.findByChannelId(channelDto.id())
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        assertThat(channelUsers).contains(otherUser.id());
    }

    @DisplayName("로그인된 유저가 채널을 생성하면 올바르게 반환된다.")
    @Test
    void createChannel() {
        assertThat(initializedChannel.name()).isEqualTo(CHANNEL_NAME);
    }

    @DisplayName("채널 ID로 조회하면 해당 채널이 반환된다.")
    @Test
    void findChannelById() {
        ChannelDto channel = channelService.findById(initializedChannel.id());
        assertThat(channel.id()).isEqualTo(initializedChannel.id());
        assertThat(channel.name()).isEqualTo(initializedChannel.name());
    }

    @DisplayName("채널 이름을 변경하면 수정된 이름이 반환된다.")
    @Test
    void updateChannelName() {
        channelService.updateName(initializedChannel.id(), UPDATED_CHANNEL_NAME);
        assertThat(channelService.findById(initializedChannel.id()).name()).isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @DisplayName("채널을 삭제하면 조회 시 예외가 발생한다.")
    @Test
    void deleteChannel() {
        UUID id = initializedChannel.id();
        channelService.delete(id);
        assertThatThrownBy(() -> channelService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}