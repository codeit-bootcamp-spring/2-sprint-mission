package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

}

