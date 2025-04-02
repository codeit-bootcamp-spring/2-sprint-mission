package com.sprint.mission.discodeit.dto.common;

import java.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class UserChannels {

  private Set<UUID> belongChannels;
}

