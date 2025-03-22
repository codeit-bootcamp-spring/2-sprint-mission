package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.channel.ChannelDto;
import com.sprint.mission.discodeit.application.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.user.UserDto;
import com.sprint.mission.discodeit.application.user.UsersDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final MessageService messageService;
    private final UserService userService;
    private final ReadStatusService readStatusService;

    public ChannelResponseDto create(ChannelRegisterDto channelRegisterDto) {
        ChannelDto channel = channelService.create(channelRegisterDto);

        if (channel.type().equals(ChannelType.PRIVATE)) {
            return createPrivateChannelResponse(Instant.ofEpochSecond(0), channel);
        }

        return ChannelResponseDto.fromEntity(channel, Instant.ofEpochSecond(0), null);
    }

    public List<ChannelResponseDto> findAll() {
        return channelService.findAll()
                .stream()
                .map(channel -> {
                    Instant lastMessageCreatedAt = messageService.findLastMessageCreatedAtByChannelId(channel.id());
                    return createPrivateChannelResponse(lastMessageCreatedAt, channel);
                })
                .toList();
    }

    public ChannelResponseDto updateNameToPublic(UUID channelId, String channelName) {
        channelService.updateName(channelId, channelName);

        Instant lastMessageCreatedAt = messageService.findLastMessageCreatedAtByChannelId(channelId);
        ChannelDto channel = channelService.findById(channelId);

        return ChannelResponseDto.fromEntity(channel, lastMessageCreatedAt, null);
    }

    public ChannelResponseDto addMemberToPrivate(UUID channelId, String friendEmail) {
        UserDto friend = userService.findByEmail(friendEmail);
        ChannelDto channel = channelService.addMemberToPrivate(channelId, friend.id());
        Instant lastMessageCreatedAt = messageService.findLastMessageCreatedAtByChannelId(channel.id());

        return createPrivateChannelResponse(lastMessageCreatedAt, channel);
    }

    public ChannelResponseDto findById(UUID channelId) {
        Instant lastMessageCreatedAt = messageService.findLastMessageCreatedAtByChannelId(channelId);
        ChannelDto channel = channelService.findById(channelId);

        if (channel.type().equals(ChannelType.PRIVATE)) {
            return createPrivateChannelResponse(lastMessageCreatedAt, channel);
        }

        return ChannelResponseDto.fromEntity(channel, lastMessageCreatedAt, null);
    }

    private ChannelResponseDto createPrivateChannelResponse(Instant lastMessageCreatedAt, ChannelDto channel) {
        List<UserDto> users = readStatusService.findByChannelId(channel.id())
                .readStatuses()
                .stream()
                .map(readStatus -> userService.findById(readStatus.userId()))
                .toList();

        return ChannelResponseDto.fromEntity(channel, lastMessageCreatedAt, new UsersDto(users));
    }
}
