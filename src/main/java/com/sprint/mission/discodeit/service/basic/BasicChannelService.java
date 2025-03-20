package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.channeldto.*;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusService readStatusService;


    @Override
    public Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdPirvateChannel = channelRepository.save(channel);
        System.out.println(createdPirvateChannel);

        // Read Status를 생성
        ReadStatusCreateDto readStatusCreateDto = new ReadStatusCreateDto(channelCreatePrivateDto.userId(), createdPirvateChannel.getId());
        readStatusService.create(readStatusCreateDto);

        return createdPirvateChannel;
    }


    @Override
    public Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        Channel channel = new Channel(ChannelType.PUBLIC, channelCreatePublicDto.channelName(), channelCreatePublicDto.description());
        Optional<Channel> ChannelList = channelRepository.load().stream()
                .filter(c -> c.getChannelName().equals(channelCreatePublicDto.channelName()))
                .findAny();
        if (ChannelList.isPresent()) {
            throw new IllegalArgumentException("A channel already exists.");
        }
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
        List<Channel> publicChannelList = channelList.stream()
                .filter(c -> c.getType().equals(ChannelType.PUBLIC))
                .toList();

        List<ReadStatus> readStatusList = readStatusRepository.load().stream()
                .filter(c -> c.getUserId().equals(channelFindAllByUserIdRequestDto.userId()))
                .toList();

        List<Channel> privateChannelList = channelList.stream().filter(c -> c.getType().equals(ChannelType.PRIVATE))
                .filter(f -> readStatusList.stream().anyMatch(r -> r.getChannelId().equals(f.getId())))
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
                .orElseThrow(() -> new NoSuchElementException("A channel does not exist"));
        if (matchingChannel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalStateException("Private channels cannot be changed.");
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
                .orElseThrow(() -> new NoSuchElementException("A channel does not exist"));

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
