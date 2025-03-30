package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChannelServiceTest {
    private ChannelService channelService;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private ReadStatusRepository readStatusRepository;
    private ChannelRepository channelRepository;
    private ChannelRequest setUpChannel;
    private User setUpUser;

    @BeforeEach
    void setUp() {
        messageRepository = new JCFMessageRepository();
        readStatusRepository = new JCFReadStatusRepository();
        userRepository = new JCFUserRepository();
        channelRepository = new JCFChannelRepository();
        channelService = new BasicChannelService(channelRepository, readStatusRepository, messageRepository);

        setUpUser = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        ChannelRegisterRequest channelRegisterRequest = new ChannelRegisterRequest(ChannelType.PUBLIC, CHANNEL_NAME, setUpUser.getId());
        setUpChannel = channelService.createPublic(channelRegisterRequest);
    }

    @DisplayName("Public 채널 생성 시 채널 이름과 정보 반환")
    @Test
    void createPublicChannel() {
        assertThat(setUpChannel.name()).isEqualTo(CHANNEL_NAME);
    }

    @DisplayName("Private 채널 생성 시 readStatus도 같이 생성 합니다.")
    @Test
    void createPrivateChannel() {
        User otherUser = userRepository.save(new User(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword(), null));
        ChannelRegisterRequest channelRegisterRequest = new ChannelRegisterRequest(ChannelType.PUBLIC, CHANNEL_NAME, setUpUser.getId());
        ChannelRequest privateChannel = channelService.createPrivate(channelRegisterRequest, List.of(otherUser.getId()));

        List<UUID> readStatusUserIds = readStatusRepository.findByChannelId(privateChannel.id())
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        assertThat(readStatusUserIds).containsExactlyInAnyOrder(setUpUser.getId(), otherUser.getId());
    }

    @DisplayName("채널 ID로 조회하면 올바른 채널을 반환한다.")
    @Test
    void findChannelById() {
        ChannelRequest channel = channelService.getById(setUpChannel.id());
        assertThat(setUpChannel.id() + setUpChannel.name()).isEqualTo(channel.id() + channel.name());
    }

    @DisplayName("Public 채널의 이름을 변경하면 변경된 정보가 반환된다.")
    @Test
    void updatePublicChannelName() {
        channelService.updatePublicChannelName(setUpChannel.id(), UPDATED_CHANNEL_NAME);
        assertThat(channelService.getById(setUpChannel.id()).name()).isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @DisplayName("Private 채널의 이름을 변경하려고 하면 예외가 발생한다.")
    @Test
    void updatePrivateChannelNameThrowsException() {
        ChannelRegisterRequest privateChannelDto = new ChannelRegisterRequest(ChannelType.PRIVATE, CHANNEL_NAME, setUpUser.getId());
        ChannelRequest privateChannel = channelService.createPrivate(privateChannelDto, new ArrayList<>());

        assertThatThrownBy(() -> channelService.updatePublicChannelName(privateChannel.id(), UPDATED_CHANNEL_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 채널 조회 시 공개 채널과 사용자가 속한 비공개 채널만 반환된다.")
    @Test
    void findAllChannelsForUser() {
        User otherUser = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        channelService.createPrivate(new ChannelRegisterRequest(ChannelType.PRIVATE, CHANNEL_NAME, otherUser.getId()), new ArrayList<>());

        ChannelRequest userPrivateChannel = channelService.createPrivate(new ChannelRegisterRequest(ChannelType.PRIVATE, CHANNEL_NAME, setUpUser.getId()), new ArrayList<>());

        List<UUID> setUpUserChannelIds = channelService.getAllByUserId(setUpUser.getId())
                .stream()
                .map(ChannelRequest::id)
                .toList();

        assertThat(setUpUserChannelIds).containsExactlyInAnyOrder(setUpChannel.id(), userPrivateChannel.id());
    }

    @DisplayName("채널 삭제 시 채널 삭제 확인")
    @Test
    void deleteChannelRemovesMessagesAndReadStatus() {
        UUID channelId = setUpChannel.id();
        channelService.delete(channelId);

        assertThatThrownBy(() -> channelService.getById(channelId)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("채널 삭제 시 채널에 속한 메시지도 함께 삭제된다.")
    @Test
    void deleteChannelRemovesMessages() {
        UUID channelId = setUpChannel.id();
        channelService.delete(channelId);

        boolean isExisting = messageRepository.findAll()
                .stream()
                .anyMatch(message -> message.getChannelId().equals(channelId));

        assertThat(isExisting).isFalse();
    }

    @DisplayName("비공개 채널 삭제 시 읽음 상태도 함께 삭제된다.")
    @Test
    void deletePrivateChannelRemovesReadStatus() {
        ChannelRequest privateChannel = channelService.createPrivate(new ChannelRegisterRequest(ChannelType.PRIVATE, CHANNEL_NAME, setUpUser.getId()), new ArrayList<>());

        UUID privateChannelId = privateChannel.id();
        channelService.delete(privateChannelId);

        boolean isExisting = readStatusRepository.findByChannelId(privateChannelId)
                .stream()
                .anyMatch(status -> status.getChannelId().equals(privateChannelId));

        assertThat(isExisting).isFalse();
    }

    @DisplayName("채널 조회 시 가장 최근 메시지의 생성 시간을 반환한다.")
    @Test
    void findChannelByIdReturnsLastMessageTimestamp() {
        messageRepository.save(new Message(MESSAGE_CONTENT, setUpChannel.id(), setUpUser.getId(), new ArrayList<>()));
        Message message = messageRepository.save(new Message(MESSAGE_CONTENT + "123", setUpChannel.id(), setUpUser.getId(), new ArrayList<>()));

        ChannelRequest channel = channelService.getById(setUpChannel.id());
        assertThat(channel.lastMessageCreatedAt()).isEqualTo(message.getCreatedAt());
    }
}