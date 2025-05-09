package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Mapper.ChannelMapper;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFound;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdate;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;

  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();
    log.info("채널 생성 요청: type=PUBLIC, name={}, description={}", name, description);
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, name, description));
    log.info("채널 생성 완료: id={}, type=PUBLIC", channel.getId());
    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    List<UUID> participants = request.participantIds();
    log.info("프라이빗 채널 생성 요청: participants={}", participants);
    Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));
    participants.stream()
            .map(userId -> new com.sprint.mission.discodeit.entity.ReadStatus(
                    userRepository.findById(userId).orElse(null), channel, channel.getCreatedAt()))
            .forEach(readStatusRepository::save);
    log.info("프라이빗 채널 생성 완료: id={}, participants={}", channel.getId(), participants);
    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    log.info("채널 조회 요청: id={}", channelId);
    ChannelDto dto = channelRepository.findById(channelId)
            .map(channelMapper::toDto)
            .orElseThrow(() -> {
              log.warn("채널 조회 실패: id={} not found", channelId);
              return new ChannelNotFound(Map.of("id", channelId));
            });
    log.info("채널 조회 완료: id={}", channelId);
    return dto;
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    log.info("사용자 채널 조회 요청: userId={}", userId);
    List<UUID> subscribedIds = readStatusRepository.findAllByUserId(userId)
            .stream().map(rs -> rs.getChannel().getId()).toList();
    List<ChannelDto> result = channelRepository.findAll().stream()
            .filter(c -> c.getType().equals(ChannelType.PUBLIC) || subscribedIds.contains(c.getId()))
            .map(channelMapper::toDto)
            .toList();
    log.info("사용자 채널 조회 완료: userId={}, count={}", userId, result.size());
    return result;
  }

  @Override
  @Transactional
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    log.info("채널 수정 요청: id={}, newName={}, newDescription={}", channelId, newName, newDescription);
    Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> {
              log.warn("채널 수정 실패 (not found): id={}", channelId);
              return new ChannelNotFound(Map.of("id", channelId));
            });
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.warn("채널 수정 실패 (private): id={}", channelId);
      throw new PrivateChannelUpdate(Map.of("id", channelId));
    }
    channel.update(newName, newDescription);
    Channel updated = channelRepository.save(channel);
    log.info("채널 수정 완료: id={}", updated.getId());
    return channelMapper.toDto(updated);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    log.info("채널 삭제 요청: id={}", channelId);
    Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> {
              log.warn("채널 삭제 실패 (not found): id={}", channelId);
              return new ChannelNotFound(Map.of("id", channelId));
            });
    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    channelRepository.deleteById(channelId);
    log.info("채널 삭제 완료: id={}", channelId);
  }
}
