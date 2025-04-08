package com.sprint.mission.discodeit.dto.common;

import java.util.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class CompositeIdentifier {

  private final UUID id;
  private final UUID ownerId;
}
