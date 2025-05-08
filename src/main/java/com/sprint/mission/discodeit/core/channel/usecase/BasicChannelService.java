package com.sprint.mission.discodeit.core.channel.usecase;


import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepositoryPort;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

  private final UserRepositoryPort userRepository;
  private final ChannelRepositoryPort channelRepository;
  private final MessageRepositoryPort messageRepository;
  private final ReadStatusRepositoryPort readStatusRepository;

  /**
   * <h2>공개 채널 생성</h2>
   * 공개 채널을 생성한다. <br> 채널 반환 DTO를 만들기 위해 DTO 생성 메서드를 진행한다.<br>
   *
   * @param command 채널명, 채널 설명
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  @Override
  @Transactional
  public ChannelResult create(CreatePublicChannelCommand command) {

    Channel channel = Channel.create(command.name(), command.description(),
        ChannelType.PUBLIC);
    channelRepository.save(channel);
    log.info("Public Channel created {}", channel.getId());
    return toChannelResult(channel);
  }

  /**
   * <h2>비공개 채널 생성</h2>
   * 비공개 채널을 생성한다.<br> 읽기 정보를 따로 생성한다.<br> 채널 반환 DTO를 만들기 위해 DTO 생성 메서드를 진행한다.<br>
   *
   * @param command 참가 유저 리스트
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  @Override
  @Transactional
  public ChannelResult create(CreatePrivateChannelCommand command) {
    //채널 명, 채널 설명을 null값을 주고, 채널 타입을 private로 설정한다.
    Channel channel = Channel.create(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);

    //매개변수로 받은 command의 참가 유저 리스트를 검증한다.
    command.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId).orElseThrow(
              () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId)
          );
          //유저 아이디를 바탕으로 읽기 상태를 생성함
          //각각의 읽기 상태를 저장함
          return ReadStatus.create(user, channel, channel.getCreatedAt());
        })
        .forEach(readStatusRepository::save);

    log.info("Private Channel created {}", channel.getId());
    //반환 DTO 제작을 위한 메서드 실행
    return toChannelResult(channel);
  }

  /**
   * <h2>채널 찾기 메서드</h2>
   * 채널 아이디를 바탕으로 채널을 찾는다. <br> 채널 반환 DTO를 만들기 위해 DTO 생성 메서드를 진행한다. <br>
   *
   * @param channelId
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  @Override
  @Transactional(readOnly = true)
  public ChannelResult findByChannelId(UUID channelId) {
    Channel channel = channelRepository.findByChannelId(channelId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, channelId)
    );
    return toChannelResult(channel);
  }

  /**
   * <h2>채널 전체 리스트 메서드</h2>
   * 유저 아이디를 바탕으로 채널 목록을 찾는다. <br> 채널 반환 DTO를 만들기 위해 DTO 생성 메서드를 진행한다. <br>
   *
   * @param userId
   * @return List [ 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각 ]
   */
  @Override
  @Transactional(readOnly = true)
  public List<ChannelResult> findAllByUserId(UUID userId) {
    //읽기 상태를 토대로 채널 목록의 아이디를 전부 가져온다.
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> {
          //유저의 읽기 상태 엔티티에서 채널을 뽑아온다.
          Channel channel = readStatus.getChannel();
          //채널의 아이디를 추출한다.
          return channel.getId();
          //추출한 채널 아이디를 리스트로 만든다.
        })
        .toList();

    //Public 채널만 가져와야하기에 유저 레포지토리에서 채널 타입과 채널 아이디 목록을 보낸다.
    //만약 채널 아이디 목록이 없으면 공개채널 전부 가져온다.
    List<Channel> channels = channelRepository.findAccessibleChannels(ChannelType.PUBLIC,
        mySubscribedChannelIds);
    //각각의 채널에 대해 채널 반환 DTO를 만들기 위해 DTO 생성 메서드를 진행한다.
    return channels.stream()
        .map(this::toChannelResult)
        .toList();
  }

  /**
   * <h2>채널 업데이트 메서드</h2>
   * 채널을 업데이트한다.
   *
   * @param command 바꿀 채널 아이디, 새로운 채널 명, 새로운 채널 설명
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  @Override
  @Transactional
  public ChannelResult update(UpdateChannelCommand command) {
    //채널이 있는지 없는지 확인한다.
    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, command.channelId())
    );

    //만약 채널이 private 이면 수정할 수 없기에 오류 발생
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new ChannelUnmodifiableException(ErrorCode.UNMODIFIABLE_ERROR, channel.getId());
    }

    //채널 업데이트 시작
    channel.update(command.newName(), command.newDescription());

    log.info("Channel Updated: username {}, newDescription {}", channel.getName(),
        channel.getDescription());
    return toChannelResult(channel);
  }

  /**
   * <h2>채널 반환 DTO 생성 메서드</h2>
   * 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각 등을 묶어서 보내야하기에 <br> 메시지 시각을 추출하고, 참가자의 유저 정보 묶어서
   * 생성함<br>
   *
   * @param channel
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  private ChannelResult toChannelResult(Channel channel) {
    //채널의 최근 메시지 시각을 추출함
    Instant lastMessageAt = findLastMessageAt(channel);

    //참가 유저 리스트를 저장할 리스트 생성
    List<UserResult> userIdList = new ArrayList<>();
    //읽기 상태 레포지토리에서 읽기 상태 엔티티들을 추출함
    readStatusRepository.findAllByChannelId(channel.getId())
        .stream().map(readStatus -> {
          User user = readStatus.getUser();
          //유저 엔티티를 그대로 노출시킬 수 없기에 UserResult로 감싸서 보냄
          return UserResult.create(user, user.getUserStatus().isOnline());
        })
        .forEach(userIdList::add);

    //채널 DTO 엔티티 생성 후 반환
    //채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
    return ChannelResult.create(channel, userIdList, lastMessageAt);
  }

  /**
   * <h2>최근 메시지 시각 추출 메서드</h2>
   * 매개변수로 받은 채널에서 메시지를 목록을 얻는다. <br> 메시지 목록 중에서 최근에 생성된 메시지의 생성 시각을 추출한 뒤 반환한다.
   *
   * @param channel 찾을 채널
   * @return 최신 메시지 시각
   */
  private Instant findLastMessageAt(Channel channel) {
    //메시지 레포지토리에서 채널 아이디를 검색함
    return messageRepository.findAllByChannelId(channel.getId())
        .stream()
        //메시지 생성 시각을 비교해서 생성시간 최신순 정렬해서 가장 큰 값 = 최신 값만 메시지 한개 추출
        .max(Comparator.comparing(Message::getCreatedAt).reversed())
        //메시지의 생성 시각을 추출
        .map(Message::getCreatedAt)
        //없으면 현재시각
        .orElse(Instant.now());
  }

  /**
   * <h2>채널 삭제 메서드</h2>
   * 채널과 채널 내에 작성한 모든 메시지, 읽기 상태를 제거함
   *
   * @param channelId
   */
  @Override
  @Transactional
  public void delete(UUID channelId) {
    channelRepository.delete(channelId);
    deleteAllMessage(channelId);
    deleteAllReadStatus(channelId);
    log.info("Channel deleted {}", channelId);
  }

  private void deleteAllMessage(UUID channelId) {
    List<Message> list = messageRepository.findAllByChannelId(channelId);
    for (Message message : list) {
      messageRepository.delete(message.getId());
      log.info("message deleted {}", message.getId());
    }
  }

  private void deleteAllReadStatus(UUID channelId) {
    List<ReadStatus> readStatusList = readStatusRepository.findAllByChannelId(channelId);
    for (ReadStatus status : readStatusList) {
      readStatusRepository.delete(status.getId());
      log.info("Read Status deleted {}", status.getId());
    }
  }
}
