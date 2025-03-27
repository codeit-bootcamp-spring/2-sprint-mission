package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.channelService.ChannelCreateByPrivateRequest;
import com.sprint.mission.discodeit.dto.channelService.ChannelDto;
import com.sprint.mission.discodeit.dto.channelService.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.dto.channelService.ChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData;

    public JCFChannelService() {
        this.channelData = new HashMap<>();
    }

    @Override
    public Channel create(ChannelCreateRequest channelCreateRequest) {
        Channel channel = channelCreateRequest.toEntity();
        channelData.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel createByPrivate(ChannelCreateByPrivateRequest channelCreateByPrivateRequest) {
        return null;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channelNullable = channelData.get(channelId);
        
//        return Optional.ofNullable(channelNullable).orElseThrow(() -> new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다."));
        return null;
    }

    @Override
    public ChannelDto findByStatus(UUID channelId) {
        return null;
    }

    @Override
    public List<Channel> findAll() {
//        return channelData.values().stream().toList();
        return null;
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return null;
    }

    @Override
    public Channel update(UUID channelId, ChannelUpdateRequest request) {       //채널명 수정
        Channel channelNullable = channelData.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable).orElseThrow(() -> new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다."));
//        channel.updateChannel(newName);
//        channel.updateChannelType(newType);
        
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if(!channelData.containsKey(channelId)){
            throw new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다.");
        }
        
        channelData.remove(channelId);
    }


}
