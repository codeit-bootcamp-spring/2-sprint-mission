package com.sprint.mission.discodeit.dto.common;

import java.util.*;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class ChannelInfo {

  private final String channelName;
  private final String channelType;

}
