package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record BinaryContentPutFailedEvent(
    UUID binaryContentId
){

}
