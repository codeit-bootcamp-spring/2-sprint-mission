package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("channel id 로 메시지 검색")
    public void findAllByChannelIdTest() {

        // given
        Channel channel1 = Channel.createPublicChannel("테스트 채널1", "설명1");
        channelRepository.save(channel1);

        Channel channel2 = Channel.createPublicChannel("테스트 채널2", "설명2");
        channelRepository.save(channel2);

        User user = User.createUser("테스트 유저", "user1@email.com", "password123");
        userRepository.save(user);

        Message message1 = Message.createMessage(channel1, user, "메시지1");
        Message message2 = Message.createMessage(channel1, user, "메시지2");
        messageRepository.save(message1);
        messageRepository.save(message2);

        // when
        List<Message> messages1 = messageRepository.findAllByChannelId(channel1.getId());
        List<Message> messages2 = messageRepository.findAllByChannelId(channel2.getId());

        // then
        Assertions.assertEquals(2, messages1.size());
        Assertions.assertEquals(0, messages2.size());
    }
}
