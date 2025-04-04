package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.JsonConvertor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private ChannelService channelService;
    @MockitoBean
    private UserService userService;

    @Test
    void createPublic() {
        Channel channel = new Channel(ChannelType.PUBLIC, CHANNEL_NAME);
        ChannelResult stubResult = ChannelResult.fromPublic(channel, null);
        when(channelService.createPublic(any())).thenReturn(stubResult);

        assertThat(mockMvc.post()
                .uri("/api/channels/public")
                .content(JsonConvertor.asString(new PublicChannelCreateRequest(CHANNEL_NAME, UUID.randomUUID())))
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(channel.getId().toString());
    }

    @Test
    void createPrivate() {
        Channel channel = new Channel(ChannelType.PRIVATE, CHANNEL_NAME);
        ChannelResult stubResult = ChannelResult.fromPrivate(channel, null, List.of());
        when(channelService.createPrivate(any())).thenReturn(stubResult);

        assertThat(mockMvc.post()
                .uri("/api/channels/private")
                .content(JsonConvertor.asString(new PrivateChannelCreateRequest(CHANNEL_NAME, UUID.randomUUID(), List.of())))
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(channel.getId().toString());
    }

    @Test
    void getById() {
        Channel channel = new Channel(ChannelType.PRIVATE, CHANNEL_NAME);
        ChannelResult stubPrivateResult = ChannelResult.fromPrivate(channel, null, List.of());

        when(channelService.getById(any())).thenReturn(stubPrivateResult, stubPrivateResult);

        assertThat(mockMvc.get()
                .uri("/api/channels/{channelId}", channel.getId()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(channel.getId().toString());
    }

    @Test
    void getAllByUserId() {
        Channel channel = new Channel(ChannelType.PRIVATE, CHANNEL_NAME);
        UUID memberId = UUID.randomUUID();
        ChannelResult stubResult = ChannelResult.fromPrivate(channel, null, List.of(memberId));
        when(channelService.getAllByUserId(any())).thenReturn(List.of(stubResult));

        assertThat(mockMvc.get()
                .uri("/api/channels", channel.getId())
                .queryParam("userId", memberId.toString()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].privateMemberIds.[0]")
                .isEqualTo(List.of(memberId.toString()));
    }

    @Test
    void updatePublicChannelName() {
        Channel channel = new Channel(ChannelType.PUBLIC, CHANNEL_NAME);
        channel.updateName(UPDATED_CHANNEL_NAME);
        ChannelResult stubResult = ChannelResult.fromPublic(channel, null);
        when(channelService.updatePublicChannelName(any(), any())).thenReturn(stubResult);

        assertThat(mockMvc.put()
                .uri("/api/channels/public/update")
                .content(JsonConvertor.asString(new PublicChannelUpdateRequest(channel.getId(), UPDATED_CHANNEL_NAME)))
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.name")
                .isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @Test
    void addPrivateChannelMember() {
        Channel channel = new Channel(ChannelType.PRIVATE, CHANNEL_NAME);
        UUID userId = UUID.randomUUID();
        UUID friendId = UUID.randomUUID();
        List<UUID> privateMembers = List.of(userId, friendId);
        ChannelResult stubResult = ChannelResult.fromPrivate(channel, null, privateMembers);

        when(channelService.addPrivateChannelMember(any(), any())).thenReturn(stubResult);
        when(userService.getByEmail(any())).thenReturn(new UserResult(friendId, null, null, null, false));

        assertThat(mockMvc.post()
                .uri("/api/channels/private/{channelId}/members", channel.getId())
                .content(JsonConvertor.asString(OTHER_USER.getEmail()))
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.privateMemberIds")
                .isEqualTo(List.of(privateMembers.get(0).toString(), privateMembers.get(1).toString()));
    }

    @Test
    void delete() {
        UUID channelId = UUID.randomUUID();
        assertThat(mockMvc.delete().uri("/api/channels/{channelId}", channelId))
                .hasStatus(HttpStatus.NO_CONTENT);
    }
}