package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@Sql(scripts = {"classpath:schema-test.sql"})
public class MessageRepositoryTest {

    @Autowired
    private MessageJPARepository messageJPARepository;

    @Autowired
    private TestEntityManager entityManager;

    private PageRequest pageRequest(){
        return PageRequest.of(0, 2);
    }

    @Test
    @DisplayName("[MessageRepository] findByChannel_IdEntityGraphCursor 조회 테스트")
    public void findByChannel_IdEntityGraphCursor() {
        User user = new User("메시", "messi@naver.com", "12345", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "바알방", "바알런방입니다.");
        Message message = new Message("액트5에서 만나요.", channel, user, null);

        entityManager.persist(user);
        entityManager.persist(channel);
        entityManager.persist(message);
        entityManager.flush();
        Page<Message> result = messageJPARepository.findByChannel_IdEntityGraphCursor(channel.getId(), message.getCreatedAt() ,pageRequest());

        assertThat(result).isNotNull();
//        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(message.getId());
        assertThat(result.getContent().get(0).getContent()).isEqualTo("액트5에서 만나요.");
    }

    @Test
    @DisplayName("[MessageRepository] findByChannel_IdEntityGraph 조회 테스트")
    public void findByChannel_IdEntityGraph() {
        User user = new User("호날두", "ronaldo@naver.com", "12345", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "디아방", "디아런방입니다.");
        Message message1 = new Message("액트4에서 만나요.", channel, user, null);
        Message message2 = new Message("디아블로 잡으러 갑시다.", channel, user, null);

        entityManager.persist(user);
        entityManager.persist(channel);
        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.flush();

        Page<Message> result = messageJPARepository.findByChannel_IdEntityGraph(channel.getId() ,pageRequest());

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(message1.getId());
        assertThat(result.getContent().get(0).getContent()).isEqualTo("액트4에서 만나요.");
        assertThat(result.getContent().get(1).getContent()).isEqualTo("디아블로 잡으러 갑시다.");
    }

    @Test
    @DisplayName("[MessageRepository] findByIdEntityGraph 조회 테스트")
    public void findByIdEntityGraph() {
        User user = new User("메시", "messi@naver.com", "12345", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "바알방", "바알런방입니다.");
        Message message = new Message("액트5에서 만나요.", channel, user, null);

        entityManager.persist(user);
        entityManager.persist(channel);
        entityManager.persist(message);
        entityManager.flush();

        Page<Message> result = messageJPARepository.findByIdEntityGraph(message.getId() ,pageRequest());

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(message.getId());
        assertThat(result.getContent().get(0).getContent()).isEqualTo("액트5에서 만나요.");
    }

}
