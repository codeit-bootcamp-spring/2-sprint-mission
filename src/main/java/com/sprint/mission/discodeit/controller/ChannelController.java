package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.channeldto.ChannelDto;
import com.sprint.mission.discodeit.application.channeldto.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.application.userdto.UsersDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final UserService userService;
    private final ReadStatusService readStatusService;

    public ChannelResponseDto create(ChannelRegisterDto channelRegisterDto) {
        ChannelDto channel = channelService.create(channelRegisterDto);

        if (channel.type().equals(ChannelType.PRIVATE)) {
            return createPrivateChannelResponse(channel);
        }

        return ChannelResponseDto.fromEntity(channel, null);
    }

    public List<ChannelResponseDto> findAll(UUID userId) {
        return channelService.findAllByUserId(userId)
                .stream()
                .map(this::createChannelResponse)
                .toList();
    }

    public ChannelResponseDto updateNameToPublic(UUID channelId, String channelName) {
        ChannelDto channel = channelService.updateName(channelId, channelName);

        return ChannelResponseDto.fromEntity(channel, null);
    }

    public ChannelResponseDto addMemberToPrivate(UUID channelId, String friendEmail) {
        UserDto friend = userService.findByEmail(friendEmail);
        ChannelDto channel = channelService.addMemberToPrivate(channelId, friend.id());

        return createPrivateChannelResponse(channel);
    }

    public ChannelResponseDto findById(UUID channelId) {
        ChannelDto channel = channelService.findById(channelId);

        if (channel.type().equals(ChannelType.PRIVATE)) {
            return createPrivateChannelResponse(channel);
        }

        return ChannelResponseDto.fromEntity(channel, null);
    }

    private ChannelResponseDto createChannelResponse(ChannelDto channel) {
        if (channel.type().equals(ChannelType.PRIVATE)) {
            return createPrivateChannelResponse(channel);
        }

        return ChannelResponseDto.fromEntity(channel, null);
    }

    private ChannelResponseDto createPrivateChannelResponse(ChannelDto channel) {
        List<UserDto> users = readStatusService.findByChannelId(channel.id())
                .readStatuses()
                .stream()
                .map(readStatus -> userService.findById(readStatus.userId()))
                .toList();

        return ChannelResponseDto.fromEntity(channel, new UsersDto(users));
    }
}
