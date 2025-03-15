package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    List<BinaryContent> binaryContentList = new ArrayList<>();

    @Override
    public BinaryContent save(byte[] imageFile) {
        BinaryContent binaryContent = new BinaryContent(imageFile);
        binaryContentList.add(binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findByProfileId(UUID profileId) {
        return binaryContentList.stream()
                .filter(binaryContent -> binaryContent.getId().equals(profileId))
                .findAny();
    }

    @Override
    public List<BinaryContent> findAllByMessage(List<UUID> attachmentId) {
        return binaryContentList.stream()
                .filter(binaryContent -> attachmentId.contains(binaryContent.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProfileId(UUID profileId) {
        binaryContentList.removeIf(binaryContent -> binaryContent.getId().equals(profileId));
    }

    @Override
    public void deleteAttachmentId(List<UUID> attachmentId) {
        binaryContentList.removeIf(binaryContent -> attachmentId.contains(binaryContent.getId()));
    }
}
