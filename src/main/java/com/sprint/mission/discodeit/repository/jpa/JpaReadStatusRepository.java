package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jpa", matchIfMissing = true)
@Repository
@RequiredArgsConstructor
public class JpaReadStatusRepository implements ReadStatusRepository {

    private final SpringDataReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        return readStatusRepository.save(readStatus);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return readStatusRepository.findById(id);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUser_Id(userId);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannel_Id(channelId);
    }

    @Override
    public boolean existsById(UUID id) {
        return readStatusRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        readStatusRepository.deleteById(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        readStatusRepository.deleteAllByChannel_Id(channelId);
    }
}
