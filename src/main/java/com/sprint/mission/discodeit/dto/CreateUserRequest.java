package com.sprint.mission.discodeit.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

  private String email;
  private String username;
  private String password;
}
