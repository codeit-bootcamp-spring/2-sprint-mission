package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.dto.binaryContentDto.BinaryContentRequestDto;
import com.sprint.discodeit.domain.dto.binaryContentDto.BinaryContentUpdateRequestDto;
import com.sprint.discodeit.domain.entity.BinaryContent;
import com.sprint.discodeit.repository.file.BaseBinaryContentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentService {

    private final BaseBinaryContentRepository baseBinaryContentRepository;

    public BinaryContent creat(BinaryContentRequestDto binaryContentRequestDto){
        BinaryContent binaryContent = new BinaryContent(binaryContentRequestDto.fileType(), binaryContentRequestDto.filePath());
        return binaryContent;
    }

    public BinaryContent find(UUID binaryContentId){
        BinaryContent binaryContent = baseBinaryContentRepository.findById(binaryContentId).orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 파일 입니다. "));
        return binaryContent;
    }

    public List<BinaryContent> findAll(){
        return baseBinaryContentRepository.findByAll();
    }

    public void update(BinaryContentUpdateRequestDto binaryContentRequestDto){
        BinaryContent binaryContent = baseBinaryContentRepository.findById(binaryContentRequestDto.binaryContentId()).orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 파일 입니다. "));
        binaryContent.update(binaryContentRequestDto.fileType(), binaryContentRequestDto.filePath());
    }

    public void delete(UUID binaryContentId){
        baseBinaryContentRepository.delete(binaryContentId);
    }





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
