package com.sprint.mission.discodeit.storage.s3.event;

import java.util.UUID;

public record S3UploadEvent(
    UUID id,
    byte[] bytes
) {

}
