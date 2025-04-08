package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.channeldto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    @Override
    public Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {
        List<UUID> userIds = channelCreatePrivateDto.participantIds();
        List<User> users = userRepository.load().stream()
                .filter(m ->channelCreatePrivateDto.participantIds().contains(m.getId()))
                .toList();
        if (users.size() != userIds.size()) {
            throw new InvalidInputException("User does not exist.");
        }

        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdPirvateChannel = channelRepository.save(channel);

        // Read Status 생성
        channelCreatePrivateDto.participantIds().stream()
                .map(userId -> new ReadStatus(userId, createdPirvateChannel.getId(), Instant.MIN))
                .forEach(readStatusRepository::save);

        return createdPirvateChannel;
    }


    @Override
    public Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        List<Channel> channelList = channelRepository.load();
        Optional<Channel> matchingChannel = channelList.stream()
                .filter(c -> c.getType().equals(ChannelType.PUBLIC) && c.getName().equals(channelCreatePublicDto.name()))
                .findAny();
        if (matchingChannel.isPresent()) {
            throw new InvalidInputException("A channel already exists.");
        }
        Channel channel = new Channel(ChannelType.PUBLIC, channelCreatePublicDto.name(), channelCreatePublicDto.description());
        Channel createdPublicChannel = channelRepository.save(channel);
        System.out.println(createdPublicChannel);
        return createdPublicChannel;

    }


    @Override
    public ChannelFindResponseDto find(ChannelFindRequestDto channelFindRequestDto) {
        Channel matchingChannel = channelRepository.load().stream()
                .filter(c -> c.getId().equals(channelFindRequestDto.channelId()))
                .findAny().orElse(null);

        ReadStatus matchingReadStatus = readStatusRepository.load().stream()
                .filter(r -> r.getChannelId().equals(channelFindRequestDto.channelId()))
                .findAny().orElse(null);

        return ChannelFindResponseDto.fromChannel(matchingChannel, matchingReadStatus);
    }


    @Override
    public List<ChannelFindAllByUserIdResponseDto> findAllByUserId(UUID userId) {
        List<UUID> matchingChannelIdList = readStatusRepository.load().stream()
                .filter(m -> m.getUserId().equals(userId))
                .map(ReadStatus::getChannelId)
                .toList();

        List<ChannelFindAllByUserIdResponseDto> channelByUserID = channelRepository.load().stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC)
                                || matchingChannelIdList.contains(channel.getId())
                )
                .map(channel -> {
                    List<UUID> participantIds = new ArrayList<>();
                    if (channel.getType().equals(ChannelType.PRIVATE)) {
                        readStatusRepository.load().stream()
                                .filter(r -> r.getChannelId().equals(channel.getId()))
                                .map(ReadStatus::getUserId)
                                .forEach(participantIds::add);
                    }
                    Instant lastMessageAt = messageRepository.load().stream()
                            .filter(f -> f.getChannelId().equals(channel.getId()))
                            .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                            .map(Message::getCreatedAt)
                            .limit(1)
                            .findFirst()
                            .orElse(Instant.MIN);
                    return ChannelFindAllByUserIdResponseDto.fromChannel(channel, participantIds, lastMessageAt);
                })
                .toList();

        return channelByUserID;
    }


    @Override
    public Channel update(UUID channelId, ChannelUpdateDto channelUpdateDto) {
        Channel matchingChannel = channelRepository.loadToId(channelId)
                .orElseThrow(() -> new NotFoundException("A channel does not exist"));

        if (matchingChannel.getType().equals(ChannelType.PRIVATE)) {
            throw new InvalidInputException("Private channels cannot be changed.");
        }
        matchingChannel.updateChannel(channelUpdateDto.newName(), channelUpdateDto.newDescription());
        channelRepository.save(matchingChannel);
        return matchingChannel;

    }


    @Override
    public void delete(UUID channelId) {
        Channel matchingChannel = channelRepository.load().stream()
                .filter(c -> c.getId().equals(channelId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("A channel does not exist"));

        List<Message> messageList = messageRepository.load().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .toList();

        List<ReadStatus> readStatusList = readStatusRepository.load().stream()
                .filter(r -> r.getChannelId().equals(channelId))
                .toList();

        for (Message message : messageList) {
            messageRepository.remove(message);
        }

        for (ReadStatus readStatus : readStatusList) {
            readStatusRepository.remove(readStatus);
        }

        channelRepository.remove(matchingChannel);
    }
}
