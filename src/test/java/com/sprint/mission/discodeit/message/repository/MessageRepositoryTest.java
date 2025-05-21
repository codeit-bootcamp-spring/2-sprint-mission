package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @AfterEach
    void tearDown() {
        messageRepository.deleteAllInBatch();
        channelRepository.deleteAllInBatch();
        userStatusRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("채널에 저장된 가장 최근 메세지의 생성시간을 반환합니다.")
    @Test
    void findLastMessageCreatedAtByChannelId() {
        // given
        User user = userRepository.save(new User("", "", "", null));
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        messageRepository.save(new Message(channel, user, "", List.of()));
        Message lastSavedMessage = messageRepository.save(new Message(channel, user, "", List.of()));

        // when
        Optional<Instant> lastMessageCreatedAtByChannelId = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId());

        // then
        Assertions.assertThat(lastMessageCreatedAtByChannelId)
                .isPresent()
                .get()
                .isEqualTo(lastSavedMessage.getCreatedAt());
    }


    @DisplayName("채널에 저장된 메세지를 생성날짜 기준 내림차순으로 반환합니다.")
    @Test
    void findAllByChannelIdWithAuthorDesc() {
        // given
        User user = userRepository.save(new User("", "", "", null));
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        Message firstMessage = messageRepository.save(new Message(channel, user, "", List.of()));
        Message secondMessage = messageRepository.save(new Message(channel, user, "", List.of()));
        Message thridMessage = messageRepository.save(new Message(channel, user, "", List.of()));
        Message fourthMessage = messageRepository.save(new Message(channel, user, "", List.of()));
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 2, sort);

        // when
        Slice<Message> allByChannelId = messageRepository.findAllByChannelIdWithAuthorDesc(
                channel.getId(),
                fourthMessage.getCreatedAt(),
                pageable
        );

        // then
        Assertions.assertThat(allByChannelId.getContent())
                .extracting(message -> message.getChannel().getId(), Message::getId)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(thridMessage.getChannel().getId(), thridMessage.getId()),
                        Tuple.tuple(secondMessage.getChannel().getId(), secondMessage.getId())
                );
    }

}

