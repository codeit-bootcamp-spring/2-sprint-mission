package com.sprint.mission.discodeit.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class UserTest {

  BinaryContent profile;

  @BeforeEach
  void setUp() {
    profile = mock(BinaryContent.class);
  }

  @Test
  void createUser_success() {
    // given
    // when
    User user = User.create("test", "abc@abc.com", "aaa", profile);
    // then
    assertEquals("test", user.getName());
    assertEquals("abc@abc.com", user.getEmail());
    assertEquals("aaa", user.getPassword());
    assertEquals(profile, user.getProfile());
  }

  @Test
  @Disabled
  void createUser_fail_whenNameNull() {
    //when & then
    assertThrows(UserException.class, () ->
        User.create(null, "john@example.com", "securePass123", profile));
  }

  @Test
  @Disabled
  void createUser_fail_whenPasswordBlank() {
    //when & then
    assertThrows(UserException.class, () ->
        User.create("John", "john@example.com", " ", profile));
  }

  @Test
  void updateUser_success() {
    BinaryContent oldProfile = mock(BinaryContent.class);
    BinaryContent newProfile = mock(BinaryContent.class);
    User user = User.create("John", "john@example.com", "pw123", oldProfile);

    user.update("Johnny", "johnny@sample.com", newProfile);

    assertEquals("Johnny", user.getName());
    assertEquals("johnny@sample.com", user.getEmail());
    assertEquals("pw123", user.getPassword());
    assertEquals(newProfile, user.getProfile());
  }

  @Test
  void updateUser_ignoreNullValues() {
    BinaryContent profile = mock(BinaryContent.class);
    User user = User.create("John", "john@example.com", "pw123", profile);

    user.update(null, null, null);

    assertEquals("John", user.getName());
    assertEquals("john@example.com", user.getEmail());
    assertEquals("pw123", user.getPassword());
    assertEquals(profile, user.getProfile());
  }
}
