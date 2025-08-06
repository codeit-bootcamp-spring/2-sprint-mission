package com.sprint.mission.discodeit.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SseEvent {
    private String id;
    private String name;
    private Object data;
}
