package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.common.entity.base.BaseEntity;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.dto.request.ChannelMessagePageRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.message.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import net.bytebuddy.description.annotation.AnnotationValue;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static com.sprint.mission.discodeit.message.service.BasicMessageServiceTest.MESSAGE_CONTENT;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
public class MessageIntegrationTest {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private BinaryContentRepository binaryContentRepository;
    @Autowired
    private BasicMessageService messageService;

    @AfterEach
    void tearDown() {
        messageRepository.deleteAllInBatch();
        userStatusRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();
    }

    @DisplayName("유저가 메세지 내용을 입력하면, 채널에 메세지를 생성합니다.")
    @Test
    void createTest() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        User savedUser = userRepository.save(new User("", "", "", null));
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(MESSAGE_CONTENT, savedChannel.getId(), savedUser.getId());

        // when
        MessageResult savedMessage = messageService.create(messageCreateRequest, List.of());

        // then
        assertAll(
                () -> Assertions.assertThat(messageRepository.findByChannel_Id(savedMessage.channelId())).hasSize(1),
                () -> Assertions.assertThat(savedMessage)
                        .extracting(MessageResult::content, MessageResult::channelId, messageResult -> messageResult.author().id())
                        .containsExactlyInAnyOrder(MESSAGE_CONTENT, savedChannel.getId(), savedUser.getId())
        );
    }

    @DisplayName("생성되지않은 채널에 메세지를 입력하면, 예외를 반환합니다.")
    @Test
    void createTest_ChannelNotExisting() {
        // given
        User savedUser = userRepository.save(new User("", "", "", null));
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(MESSAGE_CONTENT, UUID.randomUUID(), savedUser.getId());

        // when & then
        Assertions.assertThatThrownBy(() -> messageService.create(messageCreateRequest, List.of()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @DisplayName("등록되지않은 유저가 메세지를 입력하면, 예외를 반환합니다.")
    @Test
    void createTest_UserNotExisting() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(MESSAGE_CONTENT, savedChannel.getId(), UUID.randomUUID());

        // when & then
        Assertions.assertThatThrownBy(() -> messageService.create(messageCreateRequest, List.of()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("메세지 아이디로 조회하면, 메세지를 반환한다.")
    @Test
    void getByIdTest() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        User savedUser = userRepository.save(new User("", "", "", null));
        Message savedMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));

        // when
        MessageResult message = messageService.getById(savedMessage.getId());

        // then
        Assertions.assertThat(message)
                .extracting(MessageResult::id)
                .isEqualTo(savedMessage.getId());
    }

    @DisplayName("메세지 아이디로 조회했지만 없는 메세지라면, 예외를 반환한다.")
    @Test
    void getByIdTest_Exception() {
        // when & then
        Assertions.assertThatThrownBy(() -> messageService.getById(UUID.randomUUID()))
                .isInstanceOf(MessageNotFoundException.class);
    }

    private static Stream<Arguments> provideSortAndCursorCase() {
        return Stream.of(
                Arguments.of(null, null, List.of("msg5", "msg4")),
                Arguments.of(null, Sort.by(Sort.Direction.DESC, "createdAt"), List.of("msg5", "msg4"))
        );
    }

    @DisplayName("채널내 메세지 조회시, 생성날짜 순 내림차순(default)으로 반환합니다")
    @ParameterizedTest
    @MethodSource("provideSortAndCursorCase")
    void getAllByChannelId(Instant cursor, Sort sort, List<String> expectedMessages) {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        User savedUser = userRepository.save(new User("", "", "", null));
        Message firstMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));
        Message secondMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));
        Message thirdMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));
        Message fourthMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));
        Message fifthMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));

        ChannelMessagePageRequest channelMessagePageRequest = new ChannelMessagePageRequest(cursor, 2, 0, sort);

        Map<String, Message> messageMap = Map.of(
                "msg1", firstMessage,
                "msg2", secondMessage,
                "msg3", thirdMessage,
                "msg4", fourthMessage,
                "msg5", fifthMessage
        );

        // when
        PageResponse<MessageResult> allByChannelId = messageService.getAllByChannelId(savedChannel.getId(), channelMessagePageRequest);

        // then
        assertAll(
                () -> Assertions.assertThat(allByChannelId).extracting(PageResponse::size).isEqualTo(2),
                () -> Assertions.assertThat(allByChannelId.content())
                        .extracting(MessageResult::id)
                        .containsExactlyInAnyOrder(
                                messageMap.get(expectedMessages.get(0)).getId(),
                                messageMap.get(expectedMessages.get(1)).getId()
                        )
        );
    }

    @DisplayName("채널내 메세지 조회시, 입력된 생성날짜(커서) 이후 내림차순(default) 반환합니다")
    @Test
    void getAllByChannelIdDesc_NextCursor() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        User savedUser = userRepository.save(new User("", "", "", null));
        Message firstMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));
        Message secondMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));
        Message thirdMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));
        Message fourthMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));
        Message fifthMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));

        ChannelMessagePageRequest channelMessagePageRequest = new ChannelMessagePageRequest(null, 2, 0, null);
        PageResponse<MessageResult> noCursorMessage = messageService.getAllByChannelId(savedChannel.getId(), channelMessagePageRequest);
        ChannelMessagePageRequest nextCursorPageRequest = new ChannelMessagePageRequest((Instant) noCursorMessage.nextCursor(), 2, 0, null);

        // when
        PageResponse<MessageResult> nextCursorMessages = messageService.getAllByChannelId(savedChannel.getId(), nextCursorPageRequest);

        // then
        assertAll(
                () -> Assertions.assertThat(noCursorMessage.content())
                        .extracting(MessageResult::id)
                        .containsExactlyInAnyOrder(fourthMessage.getId(), fifthMessage.getId()),
                () -> Assertions.assertThat(nextCursorMessages.content())
                        .extracting(MessageResult::id)
                        .containsExactlyInAnyOrder(thirdMessage.getId(), secondMessage.getId())
        );
    }

    @DisplayName("메세지 내용 수정본을 받으면, 메세지 내용을 덮어쓴다.")
    @Test
    void updateContextTest() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        User savedUser = userRepository.save(new User("", "", "", null));
        Message savedMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of()));

        // when
        MessageResult updatedMessage = messageService.updateContext(savedMessage.getId(), "updated");

        // then
        Assertions.assertThat(updatedMessage).extracting(MessageResult::content).isEqualTo("updated");
    }

    @DisplayName("메세지 내용 수정시도했지만 해당 메세지가 없다면, 예외를 반환한다.")
    @Test
    void updateContextTest_EntityNotFoundException() {
        // when & then
        Assertions.assertThatThrownBy(() -> messageService.updateContext(UUID.randomUUID(), ""))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @DisplayName("메세지를 삭제하면, 메세지와 메세지와 연관된 첨부파일도 삭제한다.")
    @Test
    void deleteTest() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        User savedUser = userRepository.save(new User("", "", "", null));
        Message savedMessage = messageRepository.save(new Message(savedChannel, savedUser, "", List.of(new BinaryContent("", "", 0))));
        List<UUID> binaryContentIds = savedMessage.getAttachments()
                .stream()
                .map(BaseEntity::getId)
                .toList();

        // when
        messageService.delete(savedMessage.getId());

        // then
        assertAll(
                () -> Assertions.assertThat(messageRepository.findById(savedMessage.getId())).isNotPresent(),
                () -> Assertions.assertThat(binaryContentRepository.findAllById(binaryContentIds)).isEmpty()
        );
    }

    @DisplayName("삭제하려는 메세지가 없으면 예외를 반환한다")
    @Test
    void deleteTest_EntityNotFound() {
        // when & then
        Assertions.assertThatThrownBy(() -> messageService.delete(UUID.randomUUID()))
                .isInstanceOf(MessageNotFoundException.class);
    }

}
