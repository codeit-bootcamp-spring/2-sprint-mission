package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentUpdateDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentJPARepository binaryContentJPARepository;
    private final BinaryContentStorage binaryContentStorage;
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
                contentType
        );

        BinaryContent createBinaryContent = binaryContentJPARepository.save(binaryContent);
        binaryContentStorage.put(createBinaryContent.getId(), bytes);
        return binaryContentMapper.toDto(createBinaryContent);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> download(UUID binaryContentId) {
        BinaryContent findBinaryContent = binaryContentJPARepository.findById(binaryContentId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
        BinaryContentResponseDto response = binaryContentMapper.toDto(findBinaryContent);
        return binaryContentStorage.download(response);
    }


    @Override
    @Transactional(readOnly = true)
    public BinaryContentResponseDto find(UUID binaryContentId) {
        BinaryContent findBinaryContent = binaryContentJPARepository.findById(binaryContentId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
        return binaryContentMapper.toDto(findBinaryContent);
    }


    @Override
    @Transactional(readOnly = true)
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
        byte[] bytes = binaryContentUpdateDto.newBytes();
        String contentType = binaryContentUpdateDto.newContentType();

        matchingBinaryContent.updateBinaryContent(fileName, (long) bytes.length, contentType);
        return binaryContentMapper.toDto(matchingBinaryContent);
    }


    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        binaryContentJPARepository.findById(binaryContentId)
                .ifPresent(binaryContentJPARepository::delete);
    }
}
