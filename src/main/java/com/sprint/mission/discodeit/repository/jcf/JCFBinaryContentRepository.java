package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data; // 메타데이터 저장
    private final Map<String, byte[]> fileData;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
        this.fileData = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent content) {
        this.data.put(content.getId(), content);
        return content;
    }

    @Override
    public String saveFile(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String uuidName = UUID.randomUUID() + "_" + originalName;

        try {
            byte[] bytes = file.getBytes();
            this.fileData.put(uuidName, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return uuidName;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(BinaryContent binaryContent) {
        this.fileData.remove(binaryContent.getFilePath());
        this.data.remove(binaryContent.getId());
    }

}
