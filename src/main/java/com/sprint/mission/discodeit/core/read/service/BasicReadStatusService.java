package com.sprint.mission.discodeit.core.read.service;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.read.ReadStatusException;
import com.sprint.mission.discodeit.core.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.read.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.core.read.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.core.read.entity.ReadStatus;
import com.sprint.mission.discodeit.core.read.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.channel.ChannelException;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.core.user.UserException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

  private final JpaUserRepository userRepository;
  private final JpaReadStatusRepository readStatusRepository;
  private final JpaChannelRepository channelRepository;

  @Transactional
  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    User user = userRepository.findById(request.userId()).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND, request.userId())
    );

    Channel channel = channelRepository.findById(request.channelId()).orElseThrow(
        () -> new ChannelException(ErrorCode.CHANNEL_NOT_FOUND, request.channelId())
    );

    //TODO. 동일한 readStatus 있는 지 체크하는 로직 추가 가능할 듯
    if (readStatusRepository.findAllByUser_Id(request.userId()).stream()
        .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channel.getId()))) {
      throw new DiscodeitException(ErrorCode.READ_STATUS_ALREADY_EXISTS,
          request.channelId());
    }

    ReadStatus status = ReadStatus.create(user, channel, request.lastReadAt());
    readStatusRepository.save(status);

    log.info(
        "[ReadStatusService] ReadStatus Created: id: {}, user id: {}, channel id: {}, last Read At : {} ",
        status.getId(), user.getId(), channel.getId(), status.getLastReadAt());

    return ReadStatusDto.from(status);
  }


  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUser_Id(userId).stream().map(
        ReadStatusDto::from
    ).toList();
  }


  @Override
  @Transactional
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus status = readStatusRepository.findById(readStatusId).orElseThrow(
        () -> new ReadStatusException(ErrorCode.READ_STATUS_NOT_FOUND, readStatusId)
    );
    status.update(request.newLastReadAt());

    log.info("[ReadStatusService] Read Status updated : id {}, last Read At {}", status.getId(),
        status.getLastReadAt());
    return ReadStatusDto.from(status);
  }

  @Override
  @Transactional
  public void delete(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(
        () -> new ReadStatusException(ErrorCode.READ_STATUS_NOT_FOUND, readStatusId)
    );
    readStatusRepository.delete(readStatus);
    log.info("[ReadStatusService] Read Status deleted : id {}", readStatusId);
  }

}
