package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResDto;
import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentReqDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResDto create(CreateBinaryContentReqDto createBinaryContentReqDto);
    BinaryContentResDto find(UUID binaryContentId);
    List<BinaryContentResDto> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);}
