package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> metaData; // 메타데이터 저장
    private final Map<UUID, MultipartFile> fileData; // 파일데이터 저장

    public JCFBinaryContentRepository() {
        this.metaData = new HashMap<>();
        this.fileData = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent content, MultipartFile file) {
        this.metaData.put(content.getId(), content);
        this.fileData.put(content.getId(), file);
        return content;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(this.metaData.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return this.metaData.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.fileData.remove(id);
        this.metaData.remove(id);
    }

}
