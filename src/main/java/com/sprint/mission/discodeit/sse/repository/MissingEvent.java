package com.sprint.mission.discodeit.sse.repository;

public record MissingEvent(
    String id,
    String messageName,
    Object data
) implements Comparable<MissingEvent> {

  @Override
  public int compareTo(MissingEvent other) {
    return this.id.compareTo(other.id);
  }

}
