package com.sprint.mission.discodeit.adapter.outbound.status.read;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReadStatusRepositoryAdapter implements ReadStatusRepositoryPort {

  private final JpaReadStatusRepository jpaReadStatusRepository;

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    return jpaReadStatusRepository.save(readStatus);
  }

  @Override
  public Optional<ReadStatus> findById(UUID readStatusId) {
    return jpaReadStatusRepository.findById(readStatusId);
  }

  @Override
  public ReadStatus findByUserId(UUID userId) {
    return jpaReadStatusRepository.findByUser_Id(userId);
  }

  @Override
  public ReadStatus findByChannelId(UUID channelId) {
    return jpaReadStatusRepository.findByChannel_Id(channelId);
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return jpaReadStatusRepository.findAllByUser_Id(userId);
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return jpaReadStatusRepository.findAllByChannel_Id(channelId);
  }

  @Override
  public boolean existsId(UUID readStatusId) {
    return jpaReadStatusRepository.existsById(readStatusId);
  }

  @Override
  public void delete(UUID readStatusId) {
    jpaReadStatusRepository.deleteById(readStatusId);
  }
}
