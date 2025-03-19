package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.channeldto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;


    @Override
    public Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdPirvateChannel = channelRepository.save(channel);
        System.out.println(createdPirvateChannel);

        // 로그인 후 로그인한 user의 ID를 이용하여 readStatusRepository에 ReadStatus를 생성하는 로직 구현필요

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
    public ChannelFindResponseDto getChannel(ChannelFindRequestDto channelFindRequestDto) {
        Channel matchingChannel = channelRepository.load().stream()
                .filter(c -> c.getId().equals(channelFindRequestDto.channelId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("A channel does not exist"));
        ReadStatus matchingReadStatus = readStatusRepository.load().stream()
                .filter(r->r.getChannelId().equals(channelFindRequestDto.channelId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("A read status does not exist"));
        return ChannelFindResponseDto.fromChannel(matchingChannel, matchingReadStatus);
    }


    @Override
    public List<ChannelFindAllByUserIdResponseDto> getAllChannel(ChannelFindAllByUserIdRequestDto channelFindAllByUserIdRequestDto) {
        List<Channel> channelList = channelRepository.load();
        List<Channel> publicChannelList = channelList.stream()
                .filter(c -> c.getType().equals(ChannelType.PUBLIC))
                .toList();

        List<ReadStatus> readStatusList = readStatusRepository.load().stream()
                .filter(c -> c.getUserId().equals(channelFindAllByUserIdRequestDto.userId()))
                .toList();

        List<Channel> privateChannelList = channelList.stream().filter(c -> c.getType().equals(ChannelType.PRIVATE))
                .filter(f->readStatusList.stream().anyMatch(r-> r.getChannelId().equals(f.getId())))
                .toList();

        List<Channel> AllChannelByUserId = new ArrayList<>();
        AllChannelByUserId.addAll(publicChannelList);
        AllChannelByUserId.addAll(privateChannelList);

        return ChannelFindAllByUserIdResponseDto.fromChannel(AllChannelByUserId);
    }


    @Override
    public Channel update(ChannelUpdateDto channelUpdateDto) {
        Channel matchingChannel = channelRepository.load().stream()
                .filter(c->c.getId().equals(channelUpdateDto.channelId()))
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
                .filter(c->c.getId().equals(channelDeleteDto.channelId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("A channel does not exist"));
        channelRepository.remove(matchingChannel);
        // ChannelDeleteDto에 messageId와 readstatusId를 포함하여 message와 readstatus도 삭제하는 로직 구현 필요
    }
}
