package com.sprint.mission.discodeit.readstatus.service;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.readstatus.dto.ReadStatusResult;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.exception.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.domain.readstatus.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.readstatus.service.ReadStatusService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class BasicReadStatusServiceTest {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReadStatusRepository readStatusRepository;
    @Autowired
    private ReadStatusService readStatusService;

    @AfterEach
    void tearDown() {
        readStatusRepository.deleteAllInBatch();
        userStatusRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("채널내 유저의 읽기 상태를 생성합니다.")
    @Test
    void create() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PRIVATE, "", ""));
        ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(savedUser.getId(), savedChannel.getId());

        // when
        ReadStatusResult readStatusResult = readStatusService.create(readStatusCreateRequest);

        // then
        assertAll(
                () -> Assertions.assertThat(readStatusRepository.findByChannel_Id(savedChannel.getId())).hasSize(1),
                () -> Assertions.assertThat(readStatusResult)
                        .extracting(ReadStatusResult::channelId, ReadStatusResult::userId)
                        .containsExactlyInAnyOrder(readStatusResult.channelId(), readStatusResult.userId())
        );
    }

    @DisplayName("채널내 유저의 읽기 상태를 생성시, 생성할 채널이 없으면 예외를 반환합니다.")
    @Test
    void create_NoChannel_Exception() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(savedUser.getId(), UUID.randomUUID());

        // when & then
        Assertions.assertThatThrownBy(() -> readStatusService.create(readStatusCreateRequest))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("채널내 유저의 읽기 상태를 생성시, 유저가 등록되있지 않으면 예외를 반환합니다.")
    @Test
    void create_NoUser_Exception() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PRIVATE, "", ""));
        ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(UUID.randomUUID(), savedChannel.getId());

        // when & then
        Assertions.assertThatThrownBy(() -> readStatusService.create(readStatusCreateRequest))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("채널내 유저의 읽기 상태를 생성시, 이미 읽기 상태가 있으면 예외를 반환합니다.")
    @Test
    void create_AlreadyReadStatus_Exception() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PRIVATE, "", ""));
        ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(savedUser.getId(), savedChannel.getId());
        readStatusService.create(readStatusCreateRequest);

        // when & then
        Assertions.assertThatThrownBy(() -> readStatusService.create(readStatusCreateRequest))
                .isInstanceOf(ReadStatusAlreadyExistsException.class);
    }

    @DisplayName("유저가 가진 모든 채널내 읽기 상태를 조회합니다.")
    @Test
    void getAllByUserId() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        Channel firstChannel = channelRepository.save(new Channel(ChannelType.PRIVATE, "", ""));
        Channel secondChannel = channelRepository.save(new Channel(ChannelType.PRIVATE, "", ""));
        readStatusRepository.save(new ReadStatus(savedUser, firstChannel));
        readStatusRepository.save(new ReadStatus(savedUser, secondChannel));

        // when
        List<ReadStatusResult> readStatuses = readStatusService.getAllByUserId(savedUser.getId());

        // then
        Assertions.assertThat(readStatuses)
                .extracting(ReadStatusResult::channelId)
                .containsExactlyInAnyOrder(firstChannel.getId(), secondChannel.getId());
    }

    @DisplayName("유저로 읽기 상태를 조회시, 읽기 상태가 없으면 예외를 반환합니다.")
    @Test
    void getAllByUserId_Exception() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));

        // when
        List<ReadStatusResult> readStatuses = readStatusService.getAllByUserId(savedUser.getId());

        // then
        Assertions.assertThat(readStatuses).hasSize(0);
    }

    @DisplayName("읽기 상태의 ID를 통해 유저의 마지막 읽기 상태를 갱신합니다.")
    @Test
    void updateLastReadTime() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PRIVATE, "", ""));
        ReadStatus readStatus = readStatusRepository.save(new ReadStatus(savedUser, savedChannel));
        Instant instant = Instant.parse("2025-05-17T14:00:00Z");

        // when
        ReadStatusResult readStatusResult = readStatusService.updateLastReadTime(readStatus.getId(), instant);

        // then
        Assertions.assertThat(readStatusResult.lastReadAt()).isEqualTo(instant);
    }

    @DisplayName("읽기 상태 업데이트 시도시, 없는 읽기상태면 예외를 반환합니다.")
    @Test
    void updateLastReadTime_Exception() {
        // given
        Instant instant = Instant.parse("2025-05-17T14:00:00Z");

        // when & then
        Assertions.assertThatThrownBy(() -> readStatusService.updateLastReadTime(UUID.randomUUID(), instant))
                .isInstanceOf(ReadStatusNotFoundException.class);
    }

    @DisplayName("읽기 상태를 삭제합니다.")
    @Test
    void delete() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PRIVATE, "", ""));
        ReadStatus readStatus = readStatusRepository.save(new ReadStatus(savedUser, savedChannel));

        // when
        readStatusService.delete(readStatus.getId());

        // then
        Assertions.assertThat(readStatusRepository.findById(readStatus.getId())).isNotPresent();
    }

    @DisplayName("읽기 상태를 삭제시, 해당 읽기 상태가 없으면 예외를 반환합니다.")
    @Test
    void delete_Exception() {
        // when & then
        Assertions.assertThatThrownBy(() -> readStatusService.delete(UUID.randomUUID()))
                .isInstanceOf(ReadStatusNotFoundException.class);
    }

}