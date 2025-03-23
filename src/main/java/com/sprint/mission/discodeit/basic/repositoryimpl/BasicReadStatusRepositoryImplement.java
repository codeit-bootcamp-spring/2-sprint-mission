package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("basicReadStatusRepository")
public class BasicReadStatusRepositoryImplement implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusRepository = new HashMap<>();

    @Override
    public boolean register(ReadStatus readStatus) {
        readStatusRepository.put(readStatus.getId(), readStatus);
        return true;
    }
    
    @Override
    public Optional<ReadStatus> findById(UUID id) {
        ReadStatus status = readStatusRepository.get(id);
        return Optional.ofNullable(status);
    }
    
    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        List<ReadStatus> result = new ArrayList<>();
        for (ReadStatus status : readStatusRepository.values()) {
            if (userId.equals(status.getUserId())) {
                result.add(status);
            }
        }
        return result;
    }
    
    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatusRepository.values());
    }
    
    @Override
    public boolean updateReadStatus(ReadStatus readStatus) {
        if (readStatusRepository.containsKey(readStatus.getId())) {
            readStatusRepository.put(readStatus.getId(), readStatus);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean deleteReadStatus(UUID id) {
        return readStatusRepository.remove(id) != null;
    }
    
    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        if (userId == null || channelId == null) {
            return Optional.empty();
        }
        
        for (ReadStatus status : readStatusRepository.values()) {
            if (userId.equals(status.getUserId()) && channelId.equals(status.getChannelId())) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteAllByUserId(UUID userId) {
        boolean success = true;
        List<ReadStatus> statusesToDelete = new ArrayList<>();
        
        // 먼저 삭제할 항목 수집
        for (ReadStatus status : readStatusRepository.values()) {
            if (userId.equals(status.getUserId())) {
                statusesToDelete.add(status);
            }
        }
        
        // 삭제 실행
        for (ReadStatus status : statusesToDelete) {
            if (!deleteReadStatus(status.getId())) {
                success = false;
            }
        }
        
        return success;
    }

    @Override
    public boolean deleteAllByChannelId(UUID channelId) {
        boolean success = true;
        List<ReadStatus> statusesToDelete = new ArrayList<>();
        
        // 삭제할 항목 수집
        for (ReadStatus status : readStatusRepository.values()) {
            if (channelId.equals(status.getChannelId())) {
                statusesToDelete.add(status);
            }
        }
        
        // 삭제 실행
        for (ReadStatus status : statusesToDelete) {
            if (!deleteReadStatus(status.getId())) {
                success = false;
            }
        }
        
        return success;
    }
} 