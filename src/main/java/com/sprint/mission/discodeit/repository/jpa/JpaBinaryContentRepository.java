package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataBinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jpa", matchIfMissing = true)
@Repository
@RequiredArgsConstructor
public class JpaBinaryContentRepository implements BinaryContentRepository {

    private final SpringDataBinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return binaryContentRepository.findById(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids);
    }

    @Override
    public boolean existsById(UUID id) {
        return binaryContentRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
