package com.sprint.mission.discodeit.adapter.outbound.message;

import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepositoryPort {

  private final JpaMessageRepository jpaMessageRepository;

  @Override
  public Message save(Message message) {
    return jpaMessageRepository.save(message);
  }

  @Override
  public Optional<Message> findById(UUID id) {
    return jpaMessageRepository.findById(id);
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return jpaMessageRepository.findAllByChannel_Id(channelId);
  }

  @Override
  public boolean existsById(UUID id) {
    return jpaMessageRepository.existsById(id);
  }

  @Override
  public void delete(UUID id) {
    jpaMessageRepository.deleteById(id);
  }
}
