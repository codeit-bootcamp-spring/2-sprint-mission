package com.sprint.mission.discodeit.exception.content;

import java.util.UUID;

public final class BinaryContentErrors {

  private BinaryContentErrors() {
  }

  public static final String BINARY_CONTENT_NOT_FOUND_MESSAGE = "Binary Content not found : %s";
  public static final String NULL_POINT_BINARY_CONTENT_ID = "Binary Content ID is null";

  public static BinaryContentNotFoundError binaryContentNotFoundError(UUID binaryContentId) {
    throw new BinaryContentNotFoundError(
        String.format(BINARY_CONTENT_NOT_FOUND_MESSAGE, binaryContentId));
  }

  public static NullPointBinaryContentIdError nullPointBinaryContentIdError() {
    throw new NullPointBinaryContentIdError(NULL_POINT_BINARY_CONTENT_ID);
  }

}
