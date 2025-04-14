package com.sprint.discodeit.sprint.domain.dto.binaryContentDto;

import java.util.UUID;

public record BinaryContentUpdateRequestDto(String fileType, String filePath, UUID binaryContentId) {
}
