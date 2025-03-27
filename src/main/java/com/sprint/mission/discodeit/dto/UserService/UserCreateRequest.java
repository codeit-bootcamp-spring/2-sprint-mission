package com.sprint.mission.discodeit.dto.UserService;

import com.sprint.mission.discodeit.entity.User;


public record UserCreateRequest(
        String userName,
        String email,
        String password

) {
  public static UserCreateRequest toDTO(User user) {
      return new UserCreateRequest(user.getUserName(), user.getEmail(), user.getPassword());
  }

  public User toEntity() {
      return new User(this.userName, this.email, this.password);
  }
}
