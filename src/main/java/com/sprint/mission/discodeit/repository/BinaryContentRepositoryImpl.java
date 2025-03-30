package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BinaryContentRepositoryImpl implements BinaryContentRepository {

    private final List<BinaryContent> contents = new ArrayList<>();

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return contents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<BinaryContent> findByUserId(UUID userId) {
        return contents.stream()
                .filter(content -> userId != null && userId.equals(content.getUserId()))
                .findFirst(); // 프로필 이미지 하나만 저장하는 설계 기준
    }

    @Override
    public List<BinaryContent> findByMessageId(UUID messageId) {
        List<BinaryContent> result = new ArrayList<>();
        for (BinaryContent content : contents) {
            if (messageId != null && messageId.equals(content.getMessageId())) {
                result.add(content);
            }
        }
        return result;
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        contents.add(binaryContent);
        return binaryContent;
    }

    @Override
    public void deleteById(UUID id) {
        contents.removeIf(content -> content.getId().equals(id));
    }
}
