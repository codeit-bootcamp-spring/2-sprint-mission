package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.domain.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.service.ChannelResult;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.exception.PrivateChannelUpdateForbiddenException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class BasicChannelServiceTest {

    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private ReadStatusRepository readStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelService channelService;

    @AfterEach
    void tearDown() {
        userStatusRepository.deleteAllInBatch();
        readStatusRepository.deleteAllInBatch();
        messageRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();
    }

    @DisplayName("채넣이름과 설명으로 Public 채널을 생성한다.")
    @Test
    void createPublic() {
        // given
        String channelName = UUID.randomUUID().toString();
        PublicChannelCreateRequest channelRegisterRequest = new PublicChannelCreateRequest(channelName, "");

        // when
        ChannelResult channel = channelService.createPublic(channelRegisterRequest);

        // then
        assertAll(
                () -> Assertions.assertThat(channelRepository.findAll()).hasSize(1),
                () -> Assertions.assertThat(channel)
                        .extracting(ChannelResult::type, ChannelResult::participants, ChannelResult::name)
                        .containsExactlyInAnyOrder(ChannelType.PUBLIC, null, channelName)
        );
    }

    @DisplayName("채널 유저만 입력해서 Private 채널을 생성한다.")
    @Test
    void createPrivate() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(List.of(savedUser.getId()));

        // when
        ChannelResult channel = channelService.createPrivate(privateChannelCreateRequest);

        // then
        assertAll(
                () -> Assertions.assertThat(channel)
                        .extracting(ChannelResult::type, c -> c.participants().stream().map(UserResult::id).toList())
                        .containsExactly(ChannelType.PRIVATE, List.of(savedUser.getId())),
                () -> Assertions.assertThat(readStatusRepository.findByChannel_Id(channel.id()))
                        .extracting(
                                readStatus -> readStatus.getUser().getId(),
                                readStatus -> readStatus.getChannel().getId()
                        )
                        .containsExactlyInAnyOrder(Tuple.tuple(savedUser.getId(), channel.id()))
        );
    }

    @DisplayName("채널 아이디로 조회시 채널이 없을 경우, 예외를 반환한다.")
    @Test
    void getById_Exception() {
        // when & then
        Assertions.assertThatThrownBy(() -> channelService.getById(UUID.randomUUID()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("채널 아이디로 Public 채널을 조회한다.")
    @Test
    void getById_Public() {
        // given
        ChannelResult savedChannel = channelService.createPublic(new PublicChannelCreateRequest("", ""));

        // when
        ChannelResult channel = channelService.getById(savedChannel.id());

        // then // 이거
        Assertions.assertThat(channel)
                .extracting(ChannelResult::id, ChannelResult::type)
                .containsExactlyInAnyOrder(savedChannel.id(), ChannelType.PUBLIC);
    }

    @DisplayName("채널 아이디로 Private 채널을 조회한다.")
    @Test
    void getById_Private() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        ChannelResult savedChannel = channelService.createPrivate(new PrivateChannelCreateRequest(List.of(savedUser.getId())));

        // when
        ChannelResult channel = channelService.getById(savedChannel.id());

        // then
        Assertions.assertThat(channel)
                .extracting(ChannelResult::id, ChannelResult::type,
                        c -> c.participants().stream().map(UserResult::id).toList())
                .containsExactlyInAnyOrder(savedChannel.id(), savedChannel.type(), List.of(savedUser.getId()));
    }

    @DisplayName("유저로 채널을 검색하면, 유저가 속하는 채널 전체를 반환한다.")
    @Test
    void getAllByUserId() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        ChannelResult aPublic = channelService.createPublic(new PublicChannelCreateRequest("", ""));
        ChannelResult aPrivate = channelService.createPrivate(new PrivateChannelCreateRequest(List.of(savedUser.getId())));

        // when
        List<ChannelResult> channelsByUser = channelService.getAllByUserId(savedUser.getId());

        // then
        Assertions.assertThat(channelsByUser)
                .extracting(ChannelResult::id, ChannelResult::type,
                        channel -> {
                            List<UserResult> participants = channel.participants();
                            return participants == null ? null : participants.stream().map(UserResult::id).toList();
                        })
                .containsExactlyInAnyOrder(
                        Tuple.tuple(aPublic.id(), aPublic.type(), null),
                        Tuple.tuple(aPrivate.id(), aPrivate.type(), List.of(savedUser.getId()))
                );
    }

    @DisplayName("Public 채널을 수정합니다.")
    @Test
    void updatePublic() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("newName", "newDescription");

        // when
        ChannelResult updatedPublic = channelService.updatePublic(savedChannel.getId(), publicChannelUpdateRequest);

        // then
        Assertions.assertThat(updatedPublic)
                .extracting(ChannelResult::name, ChannelResult::description)
                .containsExactlyInAnyOrder("newName", "newDescription");
    }

    @DisplayName("채널 업데이트 시도시 해당채널이 없으면, 예외를 반환합니다.")
    @Test
    void updatePublic_Exception() {
        // given
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("newName", "newDescription");

        // when & then
        Assertions.assertThatThrownBy(() -> channelService.updatePublic(UUID.randomUUID(), publicChannelUpdateRequest))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("Private 채널을 수정을 시도하면, 예외를 반환합니다.")
    @Test
    void updatePrivate_Exception() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PRIVATE, "", ""));
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("newName", "newDescription");

        // when & then
        Assertions.assertThatThrownBy(() -> channelService.updatePublic(savedChannel.getId(), publicChannelUpdateRequest))
                .isInstanceOf(PrivateChannelUpdateForbiddenException.class);
    }

    @DisplayName("채널 삭제시 해당 채널이 없으면, 예외를 반환합니다.")
    @Test
    void delete_Exception() {
        // when & then
        Assertions.assertThatThrownBy(() -> channelService.delete(UUID.randomUUID()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("Public 채널을 삭제하면, 채널과 채널의 메세지를 삭제한다.")
    @Test
    void delete() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));

        // when
        channelService.delete(savedChannel.getId());

        // then
        assertAll(
                () -> Assertions.assertThat(channelRepository.findById(savedChannel.getId())).isNotPresent(),
                () -> Assertions.assertThat(messageRepository.findByChannel_Id(savedChannel.getId())).hasSize(0)
        );
    }

    @DisplayName("Private 채널을 삭제하면, 채널의 읽기 상태와 채널의 메세지를 삭제한다.")
    @Test
    void delete_Private() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        ChannelResult aPrivate = channelService.createPrivate(new PrivateChannelCreateRequest(List.of(savedUser.getId())));
        messageRepository.save(new Message(channelRepository.findById(aPrivate.id()).get(), savedUser, "", List.of()));

        // when
        channelService.delete(aPrivate.id());

        // then
        assertAll(
                () -> Assertions.assertThat(channelRepository.findById(aPrivate.id())).isNotPresent(),
                () -> Assertions.assertThat(readStatusRepository.findByChannel_Id(aPrivate.id())).hasSize(0),
                () -> Assertions.assertThat(messageRepository.findByChannel_Id(aPrivate.id())).hasSize(0)
        );
    }

}