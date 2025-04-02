package com.sprint.mission.discodeit.dto.common;

import jakarta.validation.constraints.NotBlank;
import java.util.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileMetadata {

  @NotBlank
  private String ownerType;
  @NotBlank
  private Long size;
  @NotBlank
  private String fileName;
  @NotBlank
  private String contentType;
}
