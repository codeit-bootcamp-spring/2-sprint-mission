package com.sprint.mission.discodeit.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Profile {

  private UUID profile;

}
