package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepositoryPort;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusResult;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.ErrorCode;
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

  private final UserRepositoryPort userRepository;
  private final ReadStatusRepositoryPort readStatusRepository;
  private final ChannelRepositoryPort channelRepository;

  /**
   * <h2>읽기 상태 생성 메서드</h2>
   * 컨트롤러단에서 읽기 상태 생성 요청 시, 생성을 시작함
   *
   * @param command 유저 아이디, 채널 아이디, 읽은 시각
   * @return 읽기상태 아이디, 유저 아이디, 채널 아이디, 읽은 시각
   */
  @Transactional
  @Override
  public ReadStatusResult create(CreateReadStatusCommand command) {
    //유저가 있는지 체크
    User user = userRepository.findById(command.userId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.userId())
    );

    //채널가 있는지 체크
    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, command.channelId())
    );

    //기존에 이미 생성된 읽기 상태가 존재하는 지 체크, 존재하면 오류 발생
    if (readStatusRepository.findAllByUserId(command.userId()).stream()
        .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channel.getId()))) {
      throw new UserAlreadyExistsException(ErrorCode.READ_STATUS_ALREADY_EXISTS,
          command.channelId());
    }

    //읽기 상태 생성
    ReadStatus status = ReadStatus.create(user, channel,
        command.lastReadAt());
    readStatusRepository.save(status);
    log.info(
        "[ReadStatusService] ReadStatus Created: id: {}, user id: {}, channel id: {}, last Read At : {} ",
        status.getId(), user.getId(), channel.getId(), status.getLastReadAt());

    //직접 컨트롤러단과 연결하기에 result로 감싸서 반환
    return ReadStatusResult.create(status);
  }

  /**
   * <h2>읽기 상태 조회 메서드</h2>
   * 읽기 상태 아이디를 통해서 읽기 상태 엔티티를 찾은뒤, <br> result dto로 감싼 뒤 반환한다.
   *
   * @param readStatusId 찾을 읽기 상태
   * @return 읽기상태 아이디, 유저 아이디, 채널 아이디, 읽은 시각
   */
  @Override
  @Transactional(readOnly = true)
  public ReadStatusResult findByReadStatusId(UUID readStatusId) {
    ReadStatus status = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND, readStatusId));
    return ReadStatusResult.create(status);
  }

  /**
   * <h2>읽기 상태 조회 메서드</h2>
   * 유저 아이디를 통해서 읽기 상태 엔티티를 찾은 뒤, <br> result dto로 감싼 뒤 반환한다.
   *
   * @param userId 찾을 유저 아이디
   * @return 읽기상태 아이디, 유저 아이디, 채널 아이디, 읽은 시각
   */
  @Override
  @Transactional(readOnly = true)
  public ReadStatusResult findByUserId(UUID userId) {
    ReadStatus status = readStatusRepository.findByUserId(userId);
    return ReadStatusResult.create(status);
  }

  /**
   * <h2>읽기 상태 전체 조회 메서드</h2>
   * DB에 저장된 모든 읽기 상태를 조회한 뒤, <br> result dto로 감싼 뒤 반환한다.
   *
   * @return 읽기상태 아이디, 유저 아이디, 채널 아이디, 읽은 시각
   */
  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusResult> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream().map(
        ReadStatusResult::create
    ).toList();
  }

  /**
   * <h2>읽기 상태 업데이트 메서드</h2>
   * 읽기 상태의 시간을 업데이트한다.
   *
   * @param command 바꿀 읽기 상태 아이디, 읽기 상태 시간
   * @return 읽기상태 아이디, 유저 아이디, 채널 아이디, 읽은 시각
   */
  @Override
  @Transactional
  public ReadStatusResult update(UpdateReadStatusCommand command) {
    //읽기 상태를 조회한다.
    ReadStatus status = readStatusRepository.findById(command.readStatusId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND, command.readStatusId())
    );
    status.update(command.newLastReadAt());

    log.info("[ReadStatusService] Read Status updated : id {}, last Read At {}", status.getId(),
        status.getLastReadAt());
    return ReadStatusResult.create(status);
  }

  /**
   * <h2>읽기 상태 제거 메서드</h2>
   * 읽기 상태를 제거한다.
   *
   * @param readStatusId
   */
  @Override
  @Transactional
  public void delete(UUID readStatusId) {
    //읽기 상태가 존재하지 않으면 삭제를 진행하지 않음
    if (!readStatusRepository.existsId(readStatusId)) {
      throw new UserNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND, readStatusId);
    }
    log.info("[ReadStatusService] Read Status deleted : id {}", readStatusId);
    readStatusRepository.delete(readStatusId);
  }

}
