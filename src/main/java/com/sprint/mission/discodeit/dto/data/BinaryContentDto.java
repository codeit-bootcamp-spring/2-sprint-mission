package com.sprint.mission.discodeit.dto.data;

import java.util.*;

public record BinaryContentDto
    (UUID id,
     String filename,
     Long size,
     String contentType,
     byte[] content) {

}
