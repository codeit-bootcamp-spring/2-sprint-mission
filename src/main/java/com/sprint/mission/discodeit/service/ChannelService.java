package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.request.NotificationUpdateRequest;

public interface ChannelService {

    ChannelDto create(PublicChannelCreateRequest request, User user);

    ChannelDto create(PrivateChannelCreateRequest request, User user);

    ChannelDto find(UUID channelId);

    ChannelDto update(UUID channelId, PublicChannelUpdateRequest request);

    void delete(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);

    List<ChannelDto> findAllPublic();

    void updateNotificationSetting(UUID channelId, User user, NotificationUpdateRequest request);
}
