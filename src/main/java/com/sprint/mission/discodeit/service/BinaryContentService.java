package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResponseDto create(BinaryContentCreateDto binaryContentCreateDto);
    BinaryContentResponseDto find(UUID binaryContentId);
    List<BinaryContentResponseDto> findAll(List<UUID> binaryContentIds);
    BinaryContentResponseDto updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto);
    void delete(UUID binaryContentId);
    ResponseEntity<?> download(UUID binaryContentId);

}
