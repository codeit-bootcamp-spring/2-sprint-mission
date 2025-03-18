package com.example.discodeit;

import com.sprint.mission.discodeit.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private BinaryContentRepository binaryContentRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ReadStatusRepository readStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @Test
    public void testRepositoryBeanLoading() {
        assertNotNull(binaryContentRepository);
        assertNotNull(channelRepository);
        assertNotNull(messageRepository);
        assertNotNull(readStatusRepository);
        assertNotNull(userRepository);
        assertNotNull(userStatusRepository);
    }
}
