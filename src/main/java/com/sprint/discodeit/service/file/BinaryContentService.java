package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.entity.BinaryContent;
import com.sprint.discodeit.repository.file.BaseBinaryContentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentService {

    private final BaseBinaryContentRepository baseBinaryContentRepository;

    public List<BinaryContent> convertToBinaryContents(List<BinaryContent> binaryContent) {
        List<BinaryContent> binaryContentList = new ArrayList<>();
        if (binaryContent == null || binaryContent.size() == 0) {
            for(BinaryContent file : binaryContent){
                binaryContentList.add(new BinaryContent(file.getFileType(), file.getFilePath()));
            }
        }
        return binaryContentList;
    }
    public void saveBinaryContents(List<BinaryContent> binaryContent) {
        for(BinaryContent file : binaryContent){
            baseBinaryContentRepository.save(file);
        }
    }
    public List<UUID> convertToUUIDs(List<BinaryContent> binaryContent) {
        List<UUID> uuidList = new ArrayList<>();
        for (BinaryContent file : binaryContent) {
            uuidList.add(file.getId());
        }
        return uuidList;
    }
}
