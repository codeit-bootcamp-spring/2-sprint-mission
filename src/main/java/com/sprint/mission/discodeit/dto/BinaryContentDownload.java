package com.sprint.mission.discodeit.dto;

import java.io.InputStream;
import org.springframework.core.io.InputStreamResource;

public record BinaryContentDownload(
    InputStream inputStream,
    String filename,
    String contentType
) {

  public InputStreamResource getResource() {
    return new InputStreamResource(this.inputStream);
  }
}
