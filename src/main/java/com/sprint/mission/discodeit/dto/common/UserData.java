package com.sprint.mission.discodeit.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.*;

public class UserData {

  @NotNull
  private UUID id;
  @NotBlank
  private String email;

}
