package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;


    @Override
    public BinaryContent create(BinaryContentCreateDto request) {
        String fileName = request.fileName();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();
        BinaryContent binaryContent = new BinaryContent(
                fileName,
                (long) bytes.length,
                contentType,
                bytes
        );
        return binaryContentRepository.save(binaryContent);
    }


    @Override
    public BinaryContent find(UUID binaryContentId) {
        return binaryContentRepository.loadToId(binaryContentId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
    }


    @Override
    public List<BinaryContent> findAll(List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContentList = binaryContentRepository.load();
        if (binaryContentList.isEmpty()) {
            throw new NotFoundException("Profile not found.");
        }
        return binaryContentList.stream()
                .filter(m -> binaryContentIds.contains(m.getId()))
                .toList();
    }


    @Override
    public BinaryContent updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto) {
        BinaryContent matchingBinaryContent = binaryContentRepository.loadToId(binaryContentUpdateDto.Id())
                .orElseThrow(() -> new NotFoundException("Profile not found."));

        String fileName = binaryContentUpdateDto.newFileName();
        String contentType = binaryContentUpdateDto.newContentType();
        byte[] bytes = binaryContentUpdateDto.newBytes();

        matchingBinaryContent.updateBinaryContent(fileName, (long) bytes.length, contentType, bytes);
        return binaryContentRepository.save(matchingBinaryContent);
    }



    @Override
    public void delete(UUID binaryContentId) {
        BinaryContent matchingBinaryContent = binaryContentRepository.loadToId(binaryContentId)
                .orElse(null);

        binaryContentRepository.remove(matchingBinaryContent);
    }
}
