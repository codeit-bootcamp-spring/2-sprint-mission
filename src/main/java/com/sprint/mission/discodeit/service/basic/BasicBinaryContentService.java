package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentJPARepository binaryContentJPARepository;


    @Override
    @Transactional
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
        return binaryContentJPARepository.save(binaryContent);
    }


    @Override
    public BinaryContent find(UUID binaryContentId) {
        return binaryContentJPARepository.findById(binaryContentId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
    }


    @Override
    public List<BinaryContent> findAll(List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContentList = binaryContentJPARepository.findAllById(binaryContentIds);
        if (binaryContentList.isEmpty()) {
            throw new NotFoundException("Profile not found.");
        }
        return binaryContentList;
    }


    @Override
    @Transactional
    public BinaryContent updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto) {
        BinaryContent matchingBinaryContent = binaryContentJPARepository.findById(binaryContentUpdateDto.Id())
                .orElseThrow(() -> new NotFoundException("Profile not found."));

        String fileName = binaryContentUpdateDto.newFileName();
        String contentType = binaryContentUpdateDto.newContentType();
        byte[] bytes = binaryContentUpdateDto.newBytes();

        matchingBinaryContent.updateBinaryContent(fileName, (long) bytes.length, contentType, bytes);
        return binaryContentJPARepository.save(matchingBinaryContent);
    }



    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        binaryContentJPARepository.findById(binaryContentId).ifPresent(binaryContentJPARepository::delete);
    }
}
