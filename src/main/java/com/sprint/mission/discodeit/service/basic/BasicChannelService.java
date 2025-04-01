package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.channeldto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;


    @Override
    public Channel createPrivate(ChannelCreateDto channelCreatePrivateDto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdPirvateChannel = channelRepository.save(channel);

        // Read Status 생성
        channelCreatePrivateDto.userId().stream()
                .map(userId -> new ReadStatus(userId, createdPirvateChannel.getId(), Instant.MIN))
                .forEach(readStatusRepository::save);

        return createdPirvateChannel;
    }


    @Override
    public Channel createPublic(ChannelCreateDto channelCreatePublicDto) {
        List<Channel> channelList = channelRepository.load();
        Optional<Channel> matchingChannel = channelList.stream()
                .filter(c -> c.getChannelName().equals(channelCreatePublicDto.channelName()))
                .findAny();
        if (matchingChannel.isPresent()) {
            throw new InvalidInputException("A channel already exists.");
        }
        Channel channel = new Channel(ChannelType.PUBLIC, channelCreatePublicDto.channelName(), channelCreatePublicDto.description());
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
    public List<ChannelFindAllByUserIdResponseDto> findAllByUserId(ChannelFindAllByUserIdRequestDto channelFindAllByUserIdRequestDto) {
        List<Channel> channelList = channelRepository.load();
        List<ReadStatus> readStatusList = readStatusRepository.load();
        // public channel list 조회
        List<Channel> publicChannelList = channelList.stream()
                .filter(c -> c.getType().equals(ChannelType.PUBLIC))
                .toList();

        // readStatus list 를 userId로 조회
        List<ReadStatus> readStatusListByUser = readStatusList.stream()
                .filter(c -> c.getUserId().equals(channelFindAllByUserIdRequestDto.userId()))
                .toList();

        // channel list 에서 readStatus list 의 channelId로 조회
        List<Channel> privateChannelList = channelList.stream().filter(c -> c.getType().equals(ChannelType.PRIVATE))
                .filter(f -> readStatusListByUser.stream().anyMatch(r -> r.getChannelId().equals(f.getId())))
                .toList();

        List<Channel> AllChannelByUserId = new ArrayList<>();
        AllChannelByUserId.addAll(publicChannelList);
        AllChannelByUserId.addAll(privateChannelList);

        return ChannelFindAllByUserIdResponseDto.fromChannel(AllChannelByUserId);
    }


    @Override
    public Channel update(ChannelUpdateDto channelUpdateDto) {
        Channel matchingChannel = channelRepository.load().stream()
                .filter(c -> c.getId().equals(channelUpdateDto.channelId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("A channel does not exist"));
        if (matchingChannel.getType().equals(ChannelType.PRIVATE)) {
            throw new InvalidInputException("Private channels cannot be changed.");
        }
        matchingChannel.updateChannel(channelUpdateDto.changeChannel(), channelUpdateDto.changeDescription());
        channelRepository.save(matchingChannel);
        return matchingChannel;

    }


    @Override
    public void delete(ChannelDeleteDto channelDeleteDto) {
        Channel matchingChannel = channelRepository.load().stream()
                .filter(c -> c.getId().equals(channelDeleteDto.channelId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("A channel does not exist"));

        List<Message> messageList = messageRepository.load().stream()
                .filter(m -> m.getChannelId().equals(channelDeleteDto.channelId()))
                .toList();

        List<ReadStatus> readStatusList = readStatusRepository.load().stream()
                .filter(r -> r.getChannelId().equals(channelDeleteDto.channelId()))
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
