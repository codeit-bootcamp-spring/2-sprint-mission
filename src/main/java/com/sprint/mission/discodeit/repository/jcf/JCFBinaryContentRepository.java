package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> metaData; // 메타데이터 저장
    private final Map<String, byte[]> fileData; // 파일데이터 저장

    public JCFBinaryContentRepository() {
        this.metaData = new HashMap<>();
        this.fileData = new HashMap<>();
    }

    @Override
    public String saveFile(BinaryContentType type, MultipartFile file, UUID id) {
        try {
            byte[] fileData = file.getBytes();
            this.fileData.put(id.toString(), fileData);
            return id.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BinaryContent save(BinaryContent content) {
        this.metaData.put(content.getId(), content);
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
        this.fileData.remove(id.toString());
        this.metaData.remove(id);
    }

}
