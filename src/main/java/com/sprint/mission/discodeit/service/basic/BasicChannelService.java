package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;

  @Override
  public ChannelResponse createPublicChannel(PublicChannelCreateRequest request) {
    log.info("공개 채널 생성 시도: name={}", request.name());
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
    channelRepository.save(channel);
    log.info("공개 채널 생성 성공: channelId={}", channel.getId());
    return assembleChannelDto(channel);
  }

  @Override
  public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request) {
    log.info("비공개 채널 생성 시도: participantIds={}", request.participantIds());
    List<User> participants = userRepository.findAllById(request.participantIds());

    if (participants.size() != request.participantIds().size()) {
      log.warn("비공개 채널 생성 실패 - 일부 참가자를 찾을 수 없음: requestedIds={}", request.participantIds());
      throw new UserNotFoundException();
    }

    Channel channel = new Channel(ChannelType.PRIVATE);
    channelRepository.save(channel);
    log.info("비공개 채널 생성 성공: channelId={}", channel.getId());
    createParticipantReadStatuses(participants, channel);
    log.debug("참가자 읽음 상태 생성 완료: channelId={}", channel.getId());

    return assembleChannelDto(channel);
  }

  @Override
  public ChannelResponse find(UUID channelId) {
    log.debug("채널 조회 시도: channelId={}", channelId);
    Channel channel = getChannel(channelId);
    log.debug("채널 조회 성공: channelId={}", channelId);
    return assembleChannelDto(channel);
  }

  @Override
  public List<ChannelResponse> findAllByUserId(UUID userId) {
    log.debug("유저의 모든 채널 조회 시도: userId={}", userId);
    validateUserExistence(userId);

    List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);
    List<Channel> privateChannels = channelRepository.findAllPrivateByUserId(userId,
        ChannelType.PRIVATE);

    List<ChannelResponse> channels = Stream.concat(publicChannels.stream(),
            privateChannels.stream())
        .map(this::assembleChannelDto)
        .toList();
    log.debug("유저의 모든 채널 조회 성공: userId={}, {}개 채널 발견", userId, channels.size());
    return channels;
  }

  @Override
  public ChannelResponse update(UUID channelId, PublicChannelUpdateRequest request) {
    log.info("채널 수정 시도: channelId={}", channelId);
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.warn("채널 수정 실패 - 채널을 찾을 수 없음: channelId={}", channelId);
          return new ChannelNotFoundException();
        });

    if (channel.getType() == ChannelType.PRIVATE) {
      log.warn("채널 수정 실패 - 비공개 채널은 수정 불가: channelId={}", channelId);
      throw new IllegalArgumentException("PRIVATE 채널 수정 불가");
    }

    channel.update(request.newName(), request.newDescription());
    channelRepository.save(channel);
    log.info("채널 수정 성공: channelId={}", channelId);

    return assembleChannelDto(channel);
  }

  @Override
  public void delete(UUID channelId) {
    log.info("채널 삭제 시도: channelId={}", channelId);
    if (!channelRepository.existsById(channelId)) {
      log.warn("채널 삭제 실패 - 채널을 찾을 수 없음: channelId={}", channelId);
      throw new ChannelNotFoundException();
    }

    messageRepository.deleteByChannel_Id(channelId);
    log.debug("채널 메시지 삭제 완료: channelId={}", channelId);
    readStatusRepository.deleteByChannel_Id(channelId);
    log.debug("채널 읽음 상태 삭제 완료: channelId={}", channelId);
    channelRepository.deleteById(channelId);
    log.info("채널 삭제 성공: channelId={}", channelId);
  }

  private ChannelResponse assembleChannelDto(Channel channel) {
    log.debug("ChannelResponse 조립 시도: channelId={}", channel.getId());
    List<UUID> participantIds = readStatusRepository.findUserIdsByChannel(channel);
    List<UserResponse> participants = userRepository.findAllById(participantIds).stream()
        .map(userMapper::toResponse)
        .toList();
    Instant lastMessageAt = messageRepository
        .findTop1ByChannelOrderByCreatedAtDesc(channel)
        .map(Message::getCreatedAt)
        .orElse(null);

    log.debug("ChannelResponse 조립 완료: channelId={}", channel.getId());
    return channelMapper.toResponse(channel, participants, lastMessageAt);
  }

  private void validateUserExistence(UUID userId) {
    log.debug("유저 존재 여부 검증: userId={}", userId);
    if (!userRepository.existsById(userId)) {
      log.warn("유저 존재 여부 검증 실패 - 유저를 찾을 수 없음: userId={}", userId);
      throw new UserNotFoundException();
    }
  }

  private Channel getChannel(UUID channelId) {
    log.debug("ID로 채널 조회: channelId={}", channelId);
    return channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.warn("ID로 채널 조회 실패 - 채널을 찾을 수 없음: channelId={}", channelId);
          return new ChannelNotFoundException();
        });
  }

  private void createParticipantReadStatuses(List<User> participants, Channel channel) {
    log.debug("참가자 읽음 상태 생성 시도: channelId={}, 참가자 수={}", channel.getId(), participants.size());
    participants.forEach(user -> {
      readStatusRepository.save(new ReadStatus(user, channel, Instant.EPOCH));
    });
    log.debug("참가자 읽음 상태 생성 완료: channelId={}", channel.getId());
  }
}