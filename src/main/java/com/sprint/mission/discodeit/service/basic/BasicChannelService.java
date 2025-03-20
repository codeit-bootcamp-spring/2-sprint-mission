package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.channel.common.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.dto.channel.response.PublicChannelCreateResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public PrivateChannelCreateResponse createPrivate(PrivateChannelCreateRequest request) {
        // name과 description 속성은 생략합니다.
        Channel channel = new Channel(ChannelType.PRIVATE);
        // 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
        List<ReadStatus> readStatuses =
                request.users().stream()
                .map( user -> {
                            ReadStatus readStatus = new ReadStatus(user.getId(), channel.getId(), Instant.now());
                            readStatusRepository.save(readStatus);
                            return readStatus;
                        }).toList();
        channelRepository.save(channel);

        return new PrivateChannelCreateResponse(channel.getType(), channel.getId(), request.users(), readStatuses);
    }

    // PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다.
    @Override
    public PublicChannelCreateResponse createPublic(PublicChannelCreateRequest channelCreateDTO) {
        Channel channel = new Channel(ChannelType.PUBLIC, channelCreateDTO.name(), channelCreateDTO.description());
        channelRepository.save(channel);
        return new PublicChannelCreateResponse(channel.getType(), channel.getId(), channel.getName(), channel.getDescription());
    }

    @Override
    public ChannelFindResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                        .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        Optional<Message> lastMessage = messageRepository.findAll().stream()
                .filter(msg -> msg.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt));

        Instant lastMessageTime = lastMessage.map(Message::getCreatedAt).orElse(null);

        List<UUID> userIds = new ArrayList<>();
        if(channel.getType().equals(ChannelType.PRIVATE)){
            userIds = readStatusRepository.findAll().stream()
                    .filter(status -> status.getChannelId().equals(channelId))
                    .map(ReadStatus::getUserId)
                    .toList();
        }

        if(channel.getType() == ChannelType.PRIVATE){
            return new ChannelFindResponse(channel.getType(),lastMessageTime, userIds);
        }
        // Public Channel Response
        return new ChannelFindResponse(channel.getType(), lastMessageTime, channel.getName(), channel.getDescription());
    }

    @Override
    public List<ChannelFindResponse> findAllByUserId(UUID userId) {
        // PUBLIC 채널 목록은 전체 조회합니다.
        // Public 채널을 List에 저장
        List<ChannelFindResponse> channelResponses = channelRepository.findAll().stream()
                .filter(c -> c.getType().equals(ChannelType.PUBLIC))
                .map(c -> find(c.getId()))
                .collect(Collectors.toList());

        // PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
        // userId를 가진 Channel을 readStatusRepository를 통해 가져와서 List에 저장
        readStatusRepository.findByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .map(this::find)
                .forEach(channelResponses::add);

        return channelResponses;
    }

    @Override
    public ChannelUpdateDto update(ChannelUpdateDto request) {
        // PRIVATE 채널은 수정할 수 없습니다.
        if(channelRepository.findById(request.id()).get().getType() == ChannelType.PRIVATE){
            throw new IllegalArgumentException("Private channels cannot be updated.");
        }

        Channel channel = channelRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + request.id() + " not found"));
        channel.update(request.name(), request.description());
        channelRepository.save(channel);

        return new ChannelUpdateDto(channel.getId(), channel.getName(), channel.getDescription());
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        // 관련된 도메인도 같이 삭제합니다.
        messageRepository.findAll().stream()
                .filter(msg -> msg.getChannelId().equals(channelId))
                    .forEach(msg -> {
                        messageRepository.deleteById(msg.getId());
                    });
        readStatusRepository.findAll().stream()
                        .filter(status -> status.getChannelId().equals(channelId))
                                .forEach(status -> {
                                    readStatusRepository.delete(status.getId());
                                });

        channelRepository.deleteById(channelId);
    }
}
