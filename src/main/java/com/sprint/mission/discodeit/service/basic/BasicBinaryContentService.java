package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.SaveBinaryContentParamDto;
import com.sprint.mission.discodeit.dto.SaveFileDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final FileManager fileManager;

    @Override
    public BinaryContent save(SaveBinaryContentParamDto saveBinaryContentParamDto) {
        String originalFileName = Paths.get(saveBinaryContentParamDto.filePath()).getFileName().toString();
        byte[] profile = saveBinaryContentParamDto.profile();
        SaveFileDto saveImageDto = fileManager.writeToFile(saveBinaryContentParamDto.subDirectory(), profile, originalFileName);
        BinaryContent binaryContent = new BinaryContent(saveImageDto.filePath(), saveImageDto.fileName(), originalFileName);
        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID binaryContentUUID) {
        return binaryContentRepository.findById(binaryContentUUID)
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public List<BinaryContent> findByIdIn(List<UUID> binaryContentUUIDList) {
        return binaryContentRepository.findAll().stream()
                .filter(binaryContent -> binaryContentUUIDList.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void delete(UUID userStatusUUID) {
        binaryContentRepository.delete(userStatusUUID);
    }
}
