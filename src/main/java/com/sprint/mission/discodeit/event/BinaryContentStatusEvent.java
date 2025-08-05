package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContentStatusEvent {
    private final BinaryContentDto binaryContentDto;
    private final UUID userId;  // 파일을 업로드한 사용자 ID
} 