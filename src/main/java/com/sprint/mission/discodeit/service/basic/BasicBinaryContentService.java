package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.util.FileUtil;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {
        String originalFilename = request.file().getOriginalFilename();
        String type = request.file().getContentType();
        long size = request.file().getSize();

        if (!FileUtil.isAllowedExtension(originalFilename)) {
            throw new IllegalArgumentException("허용하지 않는 파일");
        }
        String filePath = binaryContentRepository.saveFile(request.file());
        BinaryContent binaryContent = new BinaryContent(
                originalFilename, type, size, filePath
        );
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return idList.stream().map(this::findById).toList();
    }

    @Override
    public void delete(UUID id) {
        BinaryContent binaryContent = findById(id);

        binaryContentRepository.deleteById(binaryContent);
        // d
    }

}
