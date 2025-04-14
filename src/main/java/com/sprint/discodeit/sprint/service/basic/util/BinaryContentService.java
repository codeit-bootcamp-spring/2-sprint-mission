package com.sprint.discodeit.sprint.service.basic.util;

import com.sprint.discodeit.sprint.domain.dto.binaryContentDto.BinaryContentRequestDto;
import com.sprint.discodeit.sprint.domain.dto.binaryContentDto.BinaryContentUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint.repository.file.BaseBinaryContentRepository;
import java.util.ArrayList;
import java.util.List;
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

        // null 또는 빈 리스트일 경우 그대로 비어있는 결과 반환
        if (binaryContent == null || binaryContent.isEmpty()) {
            return binaryContentList;
        }
        for (BinaryContent file : binaryContent) {
            binaryContentList.add(new BinaryContent(file.getFileType(), file.getFilePath()));
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
