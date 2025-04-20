package com.sprint.mission.discodeit.dto.common;

import lombok.Getter;

@Getter
public class PageableRequest {

  private int page = 0;
  private int size = 50;
  private String sort = "createdAt, desc";
}
