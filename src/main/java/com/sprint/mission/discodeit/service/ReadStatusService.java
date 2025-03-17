package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusCRUDDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ReadStatusService {
    UUID create(ReadStatusCRUDDTO readStatusCRUDDTO);
    ReadStatus find(String readStatusId);
    List<ReadStatus> findAllByUserId(String userId);
    void update(String readStatusId, ReadStatusCRUDDTO readStatusCRUDDTO);
    void delete(String readStatusId);
}
