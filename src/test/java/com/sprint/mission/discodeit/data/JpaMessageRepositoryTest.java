package com.sprint.mission.discodeit.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jpa.JpaMessageRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataChannelRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataMessageRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DataJpaTest
@ActiveProfiles("test")
public class JpaMessageRepositoryTest {

    @Autowired
    private SpringDataMessageRepository messageRepository;
    @Autowired
    private SpringDataChannelRepository springDataChannelRepository;
    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    private Channel testChannel;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 테스트 시작 전 Channel과 User를 저장
        testChannel = springDataChannelRepository.save(new Channel(ChannelType.PUBLIC, "general", "일반 채널"));
        testUser = springDataUserRepository.save(new User("user1", "user1@email.com", "password", null));
    }

    @Test
    @DisplayName("메시지 저장 및 조회 성공")
    void saveAndFindById() {
        // given: @BeforeEach에서 저장된 testChannel, testUser 사용
        Message message = new Message("Hello", testChannel, testUser);
        Message saved = messageRepository.save(message);

        // when
        Optional<Message> found = messageRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("채널 ID로 메시지 리스트 조회")
    void findAllByChannelId() {
        // given: @BeforeEach에서 저장된 testChannel, testUser 사용
        Message msg1 = new Message("msg1", testChannel, testUser);
        Message msg2 = new Message("msg2", testChannel, testUser);
        messageRepository.save(msg1);
        messageRepository.save(msg2);

        // when
        List<Message> messages = messageRepository.findAllByChannel_Id(testChannel.getId());

        // then
        assertThat(messages).hasSize(2);
        assertThat(messages).extracting(Message::getContent).containsExactlyInAnyOrder("msg1", "msg2");
    }

    @Test
    @DisplayName("채널 ID로 페이징된 메시지 조회")
    void findAllByChannelIdPaging() {
        // given: @BeforeEach에서 저장된 testChannel, testUser 사용
        for (int i = 0; i < 10; i++) {
            messageRepository.save(new Message("msg" + i, testChannel, testUser));
        }
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());

        // when
        Page<Message> page = messageRepository.findAllByChannel_IdOrderByCreatedAtDesc(testChannel.getId(), pageable);

        // then
        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("메시지 존재 여부 확인")
    void existsById() {
        // given: @BeforeEach에서 저장된 testChannel, testUser 사용
        Message message = new Message("Hello", testChannel, testUser);
        Message saved = messageRepository.save(message);

        // when
        boolean exists = messageRepository.existsById(saved.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("메시지 삭제")
    void deleteById() {
        // given: @BeforeEach에서 저장된 testChannel, testUser 사용
        Message message = new Message("Hello", testChannel, testUser);
        Message saved = messageRepository.save(message);
        UUID id = saved.getId();

        // when
        messageRepository.deleteById(id);

        // then
        assertThat(messageRepository.existsById(id)).isFalse();
    }

    @Test
    @DisplayName("채널 ID로 메시지 전체 삭제")
    void deleteAllByChannelId() {
        // given: @BeforeEach에서 저장된 testChannel, testUser 사용
        Message msg1 = new Message("msg1", testChannel, testUser);
        Message msg2 = new Message("msg2", testChannel, testUser);
        messageRepository.save(msg1);
        messageRepository.save(msg2);

        // when
        messageRepository.deleteAllByChannel_Id(testChannel.getId());

        // then
        List<Message> messages = messageRepository.findAllByChannel_Id(testChannel.getId());
        assertThat(messages).isEmpty();
    }
}
