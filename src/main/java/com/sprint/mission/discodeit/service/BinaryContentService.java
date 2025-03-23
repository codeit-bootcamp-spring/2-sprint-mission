package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.BinaryContentDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    void save(BinaryContentDTO binaryContentDTO);
    BinaryContentDTO findById(UUID id);
    List<BinaryContentDTO> findAll();
    void delete(UUID id);
}