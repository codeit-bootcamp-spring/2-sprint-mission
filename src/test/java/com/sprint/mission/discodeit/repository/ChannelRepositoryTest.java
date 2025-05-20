package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = TestAuditingConfig.class)
public class ChannelRepositoryTest {

  @Autowired
  private JpaUserRepository userRepository;

  @Autowired
  private JpaChannelRepository channelRepository;

  @Autowired
  private JpaReadStatusRepository readStatusRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  void findAllByIdInSuccess() {
    // given
    User user1 = User.create("a", "a", "test", null);
    user1.setUserStatus(UserStatus.create(user1, Instant.now()));

    User user2 = User.create("b", "b", "test", null);
    user2.setUserStatus(UserStatus.create(user2, Instant.now()));

    Channel publicChannel = Channel.create("public", "public", ChannelType.PUBLIC);
    Channel privateChannel = Channel.create("private", "private", ChannelType.PRIVATE);

    userRepository.saveAll(List.of(user1, user2));
    channelRepository.saveAll(List.of(publicChannel, privateChannel));

    ReadStatus rs1 = ReadStatus.create(user1, publicChannel, Instant.now());
    ReadStatus rs2 = ReadStatus.create(user2, publicChannel, Instant.now());
    readStatusRepository.saveAll(List.of(rs1, rs2));

    em.flush();
    em.clear();

    List<UUID> channelIds = List.of(publicChannel.getId(), privateChannel.getId());
    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(null,
        channelIds);
    // then
    assertThat(result).size().isEqualTo(2);
    assertThat(result).anyMatch(c -> c.getType() == ChannelType.PUBLIC);
    assertThat(result).anyMatch(
        c -> c.getType() == ChannelType.PRIVATE && c.getId().equals(privateChannel.getId()));
  }

  @Test
  void findAllByTypeSuccess() {
    // given
    User user1 = User.create("a", "a", "test", null);
    user1.setUserStatus(UserStatus.create(user1, Instant.now()));

    User user2 = User.create("b", "b", "test", null);
    user2.setUserStatus(UserStatus.create(user2, Instant.now()));

    Channel publicChannel = Channel.create("public", "public", ChannelType.PUBLIC);
    Channel privateChannel = Channel.create("private", "private", ChannelType.PRIVATE);

    userRepository.saveAll(List.of(user1, user2));
    channelRepository.saveAll(List.of(publicChannel, privateChannel));

    ReadStatus rs1 = ReadStatus.create(user1, publicChannel, Instant.now());
    ReadStatus rs2 = ReadStatus.create(user2, publicChannel, Instant.now());
    readStatusRepository.saveAll(List.of(rs1, rs2));

    em.flush();
    em.clear();

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, null);
    // then
    assertThat(result).size().isEqualTo(1);
    assertThat(result).anyMatch(c -> c.getType() == ChannelType.PUBLIC);
    assertThat(result)
        .extracting(Channel::getType)
        .contains(ChannelType.PUBLIC)
        .doesNotContain(ChannelType.PRIVATE);
  }

  @Test
  void findAllByTypeOrIdInSuccess() {
    // given
    User user1 = User.create("a", "a", "test", null);
    user1.setUserStatus(UserStatus.create(user1, Instant.now()));

    User user2 = User.create("b", "b", "test", null);
    user2.setUserStatus(UserStatus.create(user2, Instant.now()));

    Channel publicChannel = Channel.create("public", "public", ChannelType.PUBLIC);
    Channel privateChannel = Channel.create("private", "private", ChannelType.PRIVATE);

    userRepository.saveAll(List.of(user1, user2));
    channelRepository.saveAll(List.of(publicChannel, privateChannel));

    ReadStatus rs1 = ReadStatus.create(user1, publicChannel, Instant.now());
    ReadStatus rs2 = ReadStatus.create(user2, publicChannel, Instant.now());
    readStatusRepository.saveAll(List.of(rs1, rs2));

    em.flush();
    em.clear();

    List<UUID> channelIds = List.of(publicChannel.getId(), privateChannel.getId());

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, channelIds);
    // then
    assertThat(result).size().isEqualTo(2);
    assertThat(result).anyMatch(c -> c.getType() == ChannelType.PUBLIC);
    assertThat(result)
        .extracting(Channel::getType)
        .contains(ChannelType.PUBLIC);
  }


}
