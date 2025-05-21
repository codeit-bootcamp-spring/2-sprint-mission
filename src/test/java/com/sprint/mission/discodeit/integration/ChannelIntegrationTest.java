package com.sprint.mission.discodeit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.channel.usecase.ChannelService;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelUpdateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PrivateChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PublicChannelCreateCommand;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ChannelIntegrationTest {

  @Autowired
  private ChannelService channelService;

  @Autowired
  private JpaChannelRepository channelRepository;

  private UUID publicId;
  private UUID privateId;

  @BeforeEach
  void setUp() {
    Channel publicChannel = Channel.create("public", "public", ChannelType.PUBLIC);
    Channel privateChannel = Channel.create("private", "private", ChannelType.PRIVATE);

    publicId = channelRepository.save(publicChannel).getId();
    privateId = channelRepository.save(privateChannel).getId();
  }

  @Test
  void create_Public() {
    // given
    PublicChannelCreateCommand createCommand = new PublicChannelCreateCommand("a",
        "b");
    // when
    ChannelDto channelDto = channelService.create(createCommand);
    // then
    assertNotNull(channelDto.id());
    assertEquals("a", channelDto.name());
    assertEquals("b", channelDto.description());
    assertEquals(ChannelType.PUBLIC, channelDto.type());
    Optional<Channel> optionalChannel = channelRepository.findById(channelDto.id());
    assertTrue(optionalChannel.isPresent());
  }

  @Test
  void create_Private() {
    // given
    UUID u1Id = UUID.randomUUID();
    UUID u2Id = UUID.randomUUID();
    PrivateChannelCreateCommand privateChannelCreateCommand = new PrivateChannelCreateCommand(
        List.of(u1Id, u2Id));
    // when
    ChannelDto channelDto = channelService.create(privateChannelCreateCommand);
    // then
    assertNotNull(channelDto.id());
    assertNull(channelDto.name());
    assertNull(channelDto.description());
    Optional<Channel> optionalChannel = channelRepository.findById(channelDto.id());
    assertTrue(optionalChannel.isPresent());
  }

  @Test
  void update_public() {
    // given
    ChannelUpdateCommand channelUpdateCommand = new ChannelUpdateCommand(publicId, "test",
        "test123");
    // when
    ChannelDto channelDto = channelService.update(channelUpdateCommand);
    // then
    assertNotNull(channelDto.id());
    assertNotEquals("public", channelDto.name());
    assertNotEquals("public", channelDto.description());
    assertEquals("test", channelDto.name());
    assertEquals("test123", channelDto.description());
  }

  @Test
  void update_Private_Throw400() {
    // given
    ChannelUpdateCommand channelUpdateCommand = new ChannelUpdateCommand(privateId, "test",
        "test123");
    // when & then
    assertThrows(ChannelUnmodifiableException.class, () -> {
          channelService.update(channelUpdateCommand);
        }
    );
  }

  @Test
  void delete_Success() {
    // when
    channelService.delete(publicId);
    // then
    Optional<Channel> optionalChannel = channelRepository.findById(publicId);
    assertFalse(optionalChannel.isPresent());
  }

  @Test
  void delete_ChannelNotFound_Throw404() {
    UUID fakeId = UUID.randomUUID();
    // when
    assertThrows(ChannelNotFoundException.class, () -> {
      channelService.delete(fakeId);
    });
    // then
    Optional<Channel> optionalChannel = channelRepository.findById(fakeId);
    assertFalse(optionalChannel.isPresent());
  }
}
