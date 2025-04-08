package com.sprint.mission.discodeit.dto.common;

import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListSummary<T> {

  private List<T> data;

  public ListSummary(List<T> data) {
    this.data = data;
  }
}


