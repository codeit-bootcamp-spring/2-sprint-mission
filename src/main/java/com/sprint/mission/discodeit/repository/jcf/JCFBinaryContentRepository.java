package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    public BinaryContent save(BinaryContent binaryContent){
        this.data.put(binaryContent.getId(), binaryContent);

        return binaryContent;
    }

    public BinaryContent findById(UUID binaryContentId){
        return Optional.ofNullable(data.get(binaryContentId))
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found"));
    }

    public List<BinaryContent> findAll(){
        return this.data.values().stream().toList();
    }

    public void delete(UUID binaryContentId){
        data.remove(binaryContentId);
    }
}
