package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateParam;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicBinaryContentService {
    private BinaryContentRepository binaryContentRepository;

    public List<UUID> create(BinaryContentCreateParam createParam) {
        // 1. file을 하나씩 빼서 BinaryContent를 생성하고 UUID와 파일이름을 사용하여 새로운 파일명을 만듦.
        // 2. 해당 파일을 저장. 3. 해당 파일의 메타데이터 저장
        return createParam.files().stream()
                .map(file -> {
                    BinaryContent bc = new BinaryContent(createParam.type(), file.getOriginalFilename(),
                            file.getSize());
                    BinaryContent resBinaryContent = binaryContentRepository.save(bc, file);
                    return resBinaryContent.getId();
                }).toList();
    }

    public BinaryContent findMetaDataById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음"));
    }

    public List<BinaryContent> findAllMetaDataByIdIn(List<UUID> idList) {
        return idList.stream().map(this::findMetaDataById).toList();
    }

    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음");
        }
        binaryContentRepository.deleteById(id);
    }
}
