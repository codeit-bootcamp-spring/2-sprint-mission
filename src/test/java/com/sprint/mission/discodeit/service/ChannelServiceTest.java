package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
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

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.mock.message.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChannelServiceTest {

    private ChannelService channelService;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private ReadStatusRepository readStatusRepository;
    private ChannelResult setUpPublicChannel;
    private User setUpUser;

    @BeforeEach
    void setUp() {
        messageRepository = new JCFMessageRepository();
        readStatusRepository = new JCFReadStatusRepository();
        userRepository = new JCFUserRepository();
        channelService = new BasicChannelService(new JCFChannelRepository(), readStatusRepository,
                messageRepository);

        setUpUser = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
        setUpPublicChannel = channelService.createPublic(new PublicChannelCreateRequest(CHANNEL_NAME,
                setUpUser.getId()));
    }

    @DisplayName("Public 채널을 생성할 경우, 저장한 채널 정보를 반환합니다.")
    @Test
    void createPublicChannel() {
        assertThat(setUpPublicChannel.name()).isEqualTo(CHANNEL_NAME);
    }

    @DisplayName("Public 채널을 생성할 경우, 채널내 멤버는 null을 반환합니다.")
    @Test
    void createPublicChannel_NoMember() {
        assertThat(setUpPublicChannel.privateMemberIds()).isNull();
    }

    @DisplayName("Private 채널을 생성할 경우, 채널 멤버도 반홥합니다.")
    @Test
    void createPrivateChannel() {
        PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(
                CHANNEL_NAME, setUpUser.getId(), List.of());
        ChannelResult privateChannel = channelService.createPrivate(privateChannelCreateRequest);

        assertThat(privateChannel.privateMemberIds()).containsExactlyInAnyOrder(setUpUser.getId());
    }

    @DisplayName("채널 ID로 조회할 경우, 같은 ID를 가진 채널을 반환합니다.")
    @Test
    void getChannelById() {
        ChannelResult channel = channelService.getById(setUpPublicChannel.id());

        assertThat(setUpPublicChannel.id()).isEqualTo(channel.id());
    }

    @DisplayName("채널을 조회할 경우, 채널내 가장 최근 메시지의 생성 시간을 반환합니다.")
    @Test
    void getChannelByIdReturnsLastMessageTimestamp() {
        Message message = messageRepository.save(
                new Message(MESSAGE_CONTENT, setUpPublicChannel.id(), setUpUser.getId(),
                        List.of()));

        ChannelResult channel = channelService.getById(setUpPublicChannel.id());

        assertThat(channel.lastMessageCreatedAt()).isEqualTo(message.getCreatedAt());
    }

    @DisplayName("전체 채널을 조회할 경우, Public 채널과 사용자가 속한 Private 채널만 반환합니다.")
    @Test
    void getAllChannelsForUser() {
        User otherUser = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
        channelService.createPrivate(
                new PrivateChannelCreateRequest(CHANNEL_NAME, otherUser.getId(), List.of()));
        ChannelResult userPrivateChannel = channelService.createPrivate(
                new PrivateChannelCreateRequest(CHANNEL_NAME, setUpUser.getId(), List.of()));

        List<ChannelResult> channels = channelService.getAllByUserId(setUpUser.getId());

        assertThat(channels).extracting(ChannelResult::id)
                .containsExactlyInAnyOrder(setUpPublicChannel.id(), userPrivateChannel.id());
    }

    @DisplayName("Private 채널에 맴버를 추가할 경우, 해당 Private 채널의 추가된 전체 멤버들을 반환합니다.")
    @Test
    void getByIdPrivateChannel_MemberTogether() {
        User friend = userRepository.save(
                new User(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword(), null));
        PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(
                CHANNEL_NAME, setUpUser.getId(), List.of());
        ChannelResult privateChannel = channelService.createPrivate(privateChannelCreateRequest);

        ChannelResult addedPrivateChannel = channelService.addPrivateChannelMember(privateChannel.id(),
                friend.getId());

        assertThat(addedPrivateChannel.privateMemberIds())
                .containsExactlyInAnyOrder(setUpUser.getId(), friend.getId());
    }

    @DisplayName("Public 채널의 이름을 업데이트할 경우, 변경된 정보가 반환합니다.")
    @Test
    void updatePublicChannelName() {
        channelService.updatePublicChannelName(setUpPublicChannel.id(), UPDATED_CHANNEL_NAME);

        assertThat(channelService.getById(setUpPublicChannel.id()).name()).isEqualTo(
                UPDATED_CHANNEL_NAME);
    }

    @DisplayName("Private 채널의 이름을 업데이트할 경우, 예외를 반환합니다.")
    @Test
    void updatePrivateChannelNameThrowsException() {
        PrivateChannelCreateRequest privateChannelDto = new PrivateChannelCreateRequest(
                CHANNEL_NAME, setUpUser.getId(), List.of());
        ChannelResult privateChannel = channelService.createPrivate(privateChannelDto);

        assertThatThrownBy(
                () -> channelService.updatePublicChannelName(privateChannel.id(), UPDATED_CHANNEL_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("채널을 삭제할 경우, 반환값은 없습니다..")
    @Test
    void deleteChannelRemovesMessagesAndReadStatus() {
        UUID channelId = setUpPublicChannel.id();
        channelService.delete(channelId);

        assertThatThrownBy(() -> channelService.getById(channelId)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("채널을 삭제할 경우, 채널에 속한 메시지도 함께 삭제됩니다.")
    @Test
    void deleteChannelRemovesMessages() {
        UUID channelId = setUpPublicChannel.id();
        channelService.delete(channelId);

        List<Message> messages = messageRepository.findByChannelId(channelId);

        assertThat(messages).isEmpty();
    }

    @DisplayName("비공개 채널을 삭제할 경우, 채널내 유저의 메세지 읽음 상태도 함께 삭제됩니다.")
    @Test
    void deletePrivateChannelRemovesReadStatus() {
        ChannelResult privateChannel = channelService.createPrivate(
                new PrivateChannelCreateRequest(CHANNEL_NAME, setUpUser.getId(), List.of()));

        UUID privateChannelId = privateChannel.id();
        channelService.delete(privateChannelId);
        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(privateChannelId);

        assertThat(readStatuses).isEmpty();
    }
}