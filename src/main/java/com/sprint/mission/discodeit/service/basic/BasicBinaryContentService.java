package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exceptions.ErrorCode;
import com.sprint.mission.discodeit.exceptions.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.ResponseMapStruct;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentJPARepository binaryContentJPARepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;
    private final ResponseMapStruct responseMapStruct;


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
        log.debug("[BinaryContent][create] Entity constructed: fileName={}, contentType={}", fileName, contentType);

        log.debug("[BinaryContent][create] Calling binaryContentJPARepository.save()");
        BinaryContent createBinaryContent = binaryContentJPARepository.save(binaryContent);
        log.debug("[BinaryContent][create] Calling binaryContentStorage.put()");
        binaryContentStorage.put(createBinaryContent.getId(), bytes);
        log.info("[BinaryContent][create] Created successfully: binaryContentId={}", createBinaryContent.getId());
        return responseMapStruct.toBinaryContentDto(createBinaryContent);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> download(UUID binaryContentId) {
        log.debug("[BinaryContent][download] Calling binaryContentJPARepository.findById(): binaryContentId={}", binaryContentId);
        BinaryContent findBinaryContent = binaryContentJPARepository.findById(binaryContentId)
                .orElseThrow(() -> new BinaryContentNotFoundException(Instant.now(), ErrorCode.PROFILE_NOT_FOUND, Map.of("binaryContentId", binaryContentId)));
        BinaryContentResponseDto response = responseMapStruct.toBinaryContentDto(findBinaryContent);
        log.debug("[BinaryContent][download] Calling binaryContentStorage.download()");
        ResponseEntity<?> downloadResponse = binaryContentStorage.download(response);
        log.debug("[BinaryContent][download] Download successfully: binaryContentId={}", binaryContentId);
        return downloadResponse;
    }


    @Override
    @Transactional(readOnly = true)
    public BinaryContentResponseDto find(UUID binaryContentId) {
        BinaryContent findBinaryContent = binaryContentJPARepository.findById(binaryContentId)
                .orElseThrow(() -> new BinaryContentNotFoundException(Instant.now(), ErrorCode.PROFILE_NOT_FOUND, Map.of("binaryContentId", binaryContentId)));
        return responseMapStruct.toBinaryContentDto(findBinaryContent);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentResponseDto> findAll(List<UUID> binaryContentIds) {
        List<BinaryContentResponseDto> binaryContentList = new ArrayList<>();
        binaryContentJPARepository.findAllById(binaryContentIds).stream()
                .map(responseMapStruct::toBinaryContentDto)
                .forEach(binaryContentList::add);
        return binaryContentList;
    }


    @Override
    @Transactional
    public BinaryContentResponseDto updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto) {
        BinaryContent matchingBinaryContent = binaryContentJPARepository.findById(binaryContentUpdateDto.Id())
                .orElseThrow(() -> new BinaryContentNotFoundException(Instant.now(), ErrorCode.PROFILE_NOT_FOUND, Map.of("binaryContentId", binaryContentUpdateDto.Id())));

        String fileName = binaryContentUpdateDto.newFileName();
        byte[] bytes = binaryContentUpdateDto.newBytes();
        String contentType = binaryContentUpdateDto.newContentType();

        matchingBinaryContent.updateBinaryContent(fileName, (long) bytes.length, contentType);
        return responseMapStruct.toBinaryContentDto(matchingBinaryContent);
    }


    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        log.debug("[BinaryContent][delete] Calling binaryContentJPARepository.delete(): binaryContentId={}", binaryContentId);
        binaryContentJPARepository.findById(binaryContentId)
                .ifPresent(binaryContentJPARepository::delete);
        log.info("[BinaryContent][delete] Deleted successfully: binaryContentId={}", binaryContentId);
    }
}
