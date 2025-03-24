package com.sprint.mission.discodeit.DTO.UserService;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;


public record UserCreateDTO(
        String userName,
        String email,
        String password

) {
  public static UserCreateDTO toDTO(User user) {
      return new UserCreateDTO(user.getUserName(), user.getEmail(), user.getPassword());
  }

  public User toEntity() {
      return new User(this.userName, this.email, this.password);
  }
}
