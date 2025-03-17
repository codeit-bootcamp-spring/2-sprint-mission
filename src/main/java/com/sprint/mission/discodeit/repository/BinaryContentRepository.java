package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(byte[] imageFile);
    Optional<BinaryContent> findByProfileId(UUID profileId);
    List<BinaryContent> findAllByMessage(List<UUID> attachmentId);
    void deleteProfileId(UUID profileId);
    void deleteAttachmentId(List<UUID> attachmentId);
}
