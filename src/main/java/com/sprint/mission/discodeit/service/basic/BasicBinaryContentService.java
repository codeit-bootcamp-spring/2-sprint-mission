package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateParam;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public List<UUID> create(BinaryContentCreateParam createParam) {
        return createParam.files().stream()
                .map(file -> {
                    UUID id = UUID.randomUUID();
                    String filePath = binaryContentRepository.saveFile(createParam.type(), file, id);
                    BinaryContent bc = new BinaryContent(createParam.type(), file.getOriginalFilename(),
                            filePath, file.getSize(), getFileExtension(file));
                    BinaryContent resBinaryContent = binaryContentRepository.save(bc);
                    return resBinaryContent.getId();
                }).toList();
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
        if (!binaryContentRepository.existsById(id)) {
            throw new IllegalArgumentException(id + " 에 해당하는 BinaryContent를 찾을 수 없음");
        }
        binaryContentRepository.deleteById(id);
    }

    private String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return "";
    }
}
