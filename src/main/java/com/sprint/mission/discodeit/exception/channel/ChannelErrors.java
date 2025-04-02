package com.sprint.mission.discodeit.exception.channel;

import java.util.UUID;

public final class ChannelErrors {

  private ChannelErrors() {
  }

  public static final String CHANNEL_ALREADY_EXISTS_MESSAGE = "Channel with id %s already exists";
  public static final String UNMODIFIABLE_CHANNEL_MESSAGE = "Cannot modify a private channel: %s";
  public static final String CHANNEL_NOT_FOUND_MESSAGE = "Channel not found: %s";

  public static ChannelNotFoundError channelIdNotFoundError(UUID channelId) {
    throw new ChannelNotFoundError(String.format(CHANNEL_NOT_FOUND_MESSAGE, channelId));
  }

  public static UnmodifiableChannelError unmodifiableChannelError(UUID channelId) {
    throw new UnmodifiableChannelError(String.format(UNMODIFIABLE_CHANNEL_MESSAGE, channelId));
  }

}
