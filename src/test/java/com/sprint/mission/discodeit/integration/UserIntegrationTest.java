package com.sprint.mission.discodeit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.UserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserUpdateCommand;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserIntegrationTest {

  @Autowired
  private UserService userService;

  @Autowired
  private JpaUserRepository userRepository;

  private UUID u1Id;
  private UUID u2Id;

  @BeforeEach
  void setUp() {
    Instant now = Instant.now();
    BinaryContent binaryContent = BinaryContent.create(
        "test.png", 1L, "image/png");
    User beforeUser1 = User.create("a", "a@a.com", "a", null);
    beforeUser1.setUserStatus(UserStatus.create(beforeUser1, now));
    User beforeUser2 = User.create("b", "b@b.com", "b", binaryContent);
    beforeUser2.setUserStatus(UserStatus.create(beforeUser2, now));

    User u1 = userRepository.save(beforeUser1);
    User u2 = userRepository.save(beforeUser2);
    u1Id = u1.getId();
    u2Id = u2.getId();
  }

  @Test
  void register_NoProfile() {
    // given
    UserCreateCommand createCommand = new UserCreateCommand("c", "c@c.com", "c");
    // when
    UserDto userDto = userService.create(createCommand, Optional.empty());
    // then
    assertNotNull(userDto.id());
    assertEquals("c", userDto.username());
    Optional<User> optionalUser = userRepository.findById(userDto.id());
    assertTrue(optionalUser.isPresent());
  }

  @Test
  void register_WithProfile() {
    // given
    UserCreateCommand createCommand = new UserCreateCommand("c", "c@c.com", "c");

    BinaryContentCreateCommand binaryContentCreateCommand = new BinaryContentCreateCommand(
        "test.png", "image/png", new byte[0]);
    // when
    UserDto userDto = userService.create(createCommand, Optional.of(binaryContentCreateCommand));
    // then
    assertNotNull(userDto.id());
    assertNotNull(userDto.profile());
    assertEquals("c", userDto.username());
    assertEquals("test.png", userDto.profile().fileName());
    Optional<User> optionalUser = userRepository.findById(userDto.id());
    assertTrue(optionalUser.isPresent());
  }

  @Test
  void update_NoProfile() {
    // given
    UserUpdateCommand updateCommand = new UserUpdateCommand(u1Id, "aaa", "bbb@bbb.com", "ccc");
    // when
    UserDto userDto = userService.update(updateCommand, Optional.empty());
    // then
    assertNotNull(userDto.id());
    assertNotEquals("c", userDto.username());
    assertEquals("aaa", userDto.username());
    Optional<User> optionalUser = userRepository.findById(userDto.id());
    assertTrue(optionalUser.isPresent());
  }

  @Test
  void update_WithProfile() {
    // given
    UserUpdateCommand updateCommand = new UserUpdateCommand(u2Id, "aaa", "bbb@bbb.com", "ccc");
    BinaryContentCreateCommand binaryContentCreateCommand = new BinaryContentCreateCommand(
        "test123.png", "image/png", new byte[1]);
    // when
    UserDto userDto = userService.update(updateCommand, Optional.of(binaryContentCreateCommand));
    // then
    assertNotNull(userDto.id());
    assertNotNull(userDto.profile());
    assertNotEquals("c", userDto.username());
    assertEquals("aaa", userDto.username());
    assertEquals("test123.png", userDto.profile().fileName());
    Optional<User> optionalUser = userRepository.findById(userDto.id());
    assertTrue(optionalUser.isPresent());
  }

  @Test
  void delete_NoProfile() {
    // when
    userService.delete(u1Id);
    // then
    Optional<User> optionalUser = userRepository.findById(u1Id);
    assertFalse(optionalUser.isPresent());
  }

  @Test
  void delete_WithProfile() {
    // when
    userService.delete(u2Id);
    // then
    Optional<User> optionalUser = userRepository.findById(u2Id);
    assertFalse(optionalUser.isPresent());
  }

  @Test
  void findAll() {
    // when
    List<UserDto> userDtoList = userService.findAll();
    // then
    for (UserDto userDto : userDtoList) {
      assertNotNull(userDto.id());
    }
    assertNotNull(userDtoList.get(1).profile());
    assertEquals("a", userDtoList.get(0).username());
    assertEquals("b", userDtoList.get(1).username());
  }
}
