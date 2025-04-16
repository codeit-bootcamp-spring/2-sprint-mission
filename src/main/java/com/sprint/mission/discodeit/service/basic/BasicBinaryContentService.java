package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentJPARepository binaryContentJPARepository;
    private final BinaryContentMapper binaryContentMapper;


    @Override
    @Transactional
    public BinaryContentResponseDto create(BinaryContentCreateDto request) {
        String fileName = request.fileName();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();
        BinaryContent binaryContent = new BinaryContent(
                fileName,
                (long) bytes.length,
                contentType,
                bytes
        );
        BinaryContent createBinaryContent = binaryContentJPARepository.save(binaryContent);

        return binaryContentMapper.toDto(createBinaryContent);
    }


    @Override
    public BinaryContentResponseDto find(UUID binaryContentId) {
        BinaryContent findBinaryContent = binaryContentJPARepository.findById(binaryContentId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));

        return binaryContentMapper.toDto(findBinaryContent);
    }


    @Override
    public List<BinaryContentResponseDto> findAll(List<UUID> binaryContentIds) {
        List<BinaryContentResponseDto> binaryContentList = new ArrayList<>();
        binaryContentJPARepository.findAllById(binaryContentIds).stream()
                .map(binaryContentMapper::toDto)
                .forEach(binaryContentList::add);
        return binaryContentList;
    }


    @Override
    @Transactional
    public BinaryContentResponseDto updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto) {
        BinaryContent matchingBinaryContent = binaryContentJPARepository.findById(binaryContentUpdateDto.Id())
                .orElseThrow(() -> new NotFoundException("Profile not found."));

        String fileName = binaryContentUpdateDto.newFileName();
        String contentType = binaryContentUpdateDto.newContentType();
        byte[] bytes = binaryContentUpdateDto.newBytes();

        matchingBinaryContent.updateBinaryContent(fileName, (long) bytes.length, contentType, bytes);
        return binaryContentMapper.toDto(matchingBinaryContent);
    }



    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        binaryContentJPARepository.findById(binaryContentId)
                .ifPresent(binaryContentJPARepository::delete);
    }
}
