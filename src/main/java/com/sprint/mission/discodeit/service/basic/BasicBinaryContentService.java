package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicBinaryContentService implements BinaryContentService {
    BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(String filePath){
        if(binaryContentRepository.findAll().stream()
                .anyMatch(binaryContent ->
                        (binaryContent.getFilePath().equals(filePath)))) {
            throw new IllegalArgumentException("관련된 객체가 이미 존재합니다.");
        }

        BinaryContent binaryContent = new BinaryContent(filePath);

        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId);
    }

    @Override
    public List<BinaryContent> findAll(){
        return binaryContentRepository.findAll();
    }

    @Override
    public void delete(UUID binaryContentId) {
        binaryContentRepository.delete(binaryContentId);
    }
}
