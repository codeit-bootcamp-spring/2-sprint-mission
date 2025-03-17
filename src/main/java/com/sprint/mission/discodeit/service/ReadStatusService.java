package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReadStatusService {
    void create(ReadStatusDTO readStatusDTO);
    ReadStatus find(String readStatusId);
    List<ReadStatus> findAllByUserId(String userId);
    void update(String readStatusId, ReadStatusDTO readStatusDTO);
    void delete(String readStatusId);
}
