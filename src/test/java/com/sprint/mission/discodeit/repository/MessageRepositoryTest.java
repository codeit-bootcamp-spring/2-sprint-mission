package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

//    @Test
//    void findLastMessageCreatedAtByChannelId() {
//        messageRepository.findByChannelIdOrderByCreatedAtDesc();
//    }
//
}