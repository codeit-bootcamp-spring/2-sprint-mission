package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusParam;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  @Transactional
  public ReadStatusDTO create(CreateReadStatusParam createReadStatusParam) {
    checkUserExists(createReadStatusParam);
    checkChannelExists(createReadStatusParam);

    // 중복체크 - 원래 있던 ReadStatus면 원래 있던 값 반환
    // 프론트측에서 메시지가 없는 경우엔 계속 Post 요청을 보내서 중복이 있는경우 예외를 던지지 않고 원래 값을 반환
    Optional<ReadStatus> exist = readStatusRepository.findByUserIdAndChannelId(
        createReadStatusParam.userId(),
        createReadStatusParam.channelId());
    if (exist.isPresent()) {
      return readStatusMapper.toReadStatusDTO(exist.get());
    }

    ReadStatus readStatus = readStatusMapper.toEntity(createReadStatusParam);
    readStatusRepository.save(readStatus);
    return readStatusMapper.toReadStatusDTO(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDTO find(UUID id) {
    ReadStatus readStatus = findReadStatusById(id);
    return readStatusMapper.toReadStatusDTO(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDTO> findAllByUserId(UUID userId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
    return readStatuses.stream()
        .map(rs -> readStatusMapper.toReadStatusDTO(rs))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDTO> findAllByChannelId(UUID channelId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);
    return readStatuses.stream()
        .map(rs -> readStatusMapper.toReadStatusDTO(rs))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UpdateReadStatusDTO update(UUID id, UpdateReadStatusParam updateReadStatusParam) {
    ReadStatus readStatus = findReadStatusById(id);
    readStatus.updateReadStatus(updateReadStatusParam.newLastReadAt());
    return new UpdateReadStatusDTO(id, readStatus.getLastReadAt());
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    readStatusRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void deleteByChannelId(UUID channelId) {
    readStatusRepository.deleteByChannelId(channelId);
  }

  private void checkUserExists(CreateReadStatusParam createReadStatusParam) {
    userRepository.findById(createReadStatusParam.userId())
        .orElseThrow(() -> {
          logger.error("읽음상태 생성 중 유저 찾기 실패: {}", createReadStatusParam.userId());
          return RestExceptions.USER_NOT_FOUND;
        });
  }

  private void checkChannelExists(CreateReadStatusParam createReadStatusParam) {
    channelRepository.findById(createReadStatusParam.channelId())
        .orElseThrow(() -> {
          logger.error("읽음상태 생성 중 채널 찾기 실패: {}", createReadStatusParam.channelId());
          return RestExceptions.CHANNEL_NOT_FOUND;
        });
  }


  private ReadStatus findReadStatusById(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> {
          logger.error("읽음상태 찾기 실패: {}", id);
          return RestExceptions.READ_STATUS_NOT_FOUND;
        });
  }
}
