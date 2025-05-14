package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChannelController.class)
public class ChannelControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChannelService channelService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("[ChannelController][createPublicChannel] Channel Controller 테스트")
    public void createPublicChannel() throws Exception {
        List<User> users = List.of(
                createUserWithId("메시", "messi@test.com", "12345"),
                createUserWithId("호날두", "ronaldo@test.com", "54321")
        );
        List<UserResponseDto> responseUser = users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        null,
                        true))
                .toList();

        Channel publicChannel = createChannelWithId(ChannelType.PUBLIC, "테스트방", "테스트입니다.");
        ChannelResponseDto responseChannel = new ChannelResponseDto(
                publicChannel.getId(),
                publicChannel.getType(),
                publicChannel.getName(),
                publicChannel.getDescription(),
                responseUser,
                Instant.MIN
        );

        ChannelCreatePublicDto request = new ChannelCreatePublicDto("테스트방", "테스트입니다.");

        given(channelService.createPublic(any(ChannelCreatePublicDto.class))).willReturn(responseChannel);

        mockMvc.perform(post("/api/channels/public").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(publicChannel.getId().toString()))
                .andExpect(jsonPath("$.type").value(publicChannel.getType().toString()))
                .andExpect(jsonPath("$.name").value("테스트방"))
                .andExpect(jsonPath("$.description").value("테스트입니다."));


    }


    @Test
    @DisplayName("[ChannelController][createPrivateChannel] Channel Controller 테스트")
    public void createPrivateChannel() throws Exception {
        List<User> users = List.of(
                createUserWithId("메시", "messi@test.com", "12345"),
                createUserWithId("호날두", "ronaldo@test.com", "54321")
        );
        List<UserResponseDto> responseUser = users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        null,
                        true))
                .toList();

        Channel privateChannel = createChannelWithId(ChannelType.PRIVATE, null, null);
        ChannelResponseDto responseChannel = new ChannelResponseDto(
                privateChannel.getId(),
                privateChannel.getType(),
                privateChannel.getName(),
                privateChannel.getDescription(),
                responseUser,
                Instant.MIN
        );
        List<UUID> userIds = users.stream().map(User::getId).toList();
        ChannelCreatePrivateDto request = new ChannelCreatePrivateDto(userIds);

        given(channelService.createPrivate(any(ChannelCreatePrivateDto.class))).willReturn(responseChannel);

        mockMvc.perform(post("/api/channels/private").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(privateChannel.getId().toString()))
                .andExpect(jsonPath("$.type").value("PRIVATE"))
                .andExpect(jsonPath("$.participants", hasSize(2)))
                .andExpect(jsonPath("$.participants[0].username").value("메시"))
                .andExpect(jsonPath("$.participants[1].username").value("호날두"));
    }


    @Test
    @DisplayName("[ChannelController][findChannelByUserId] Channel Controller 테스트")
    public void findChannelByUserId() throws Exception {
        List<User> users = List.of(
                createUserWithId("메시", "messi@test.com", "12345"),
                createUserWithId("호날두", "ronaldo@test.com", "54321")
        );
        List<UserResponseDto> responseUser = users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        null,
                        true))
                .toList();

        List<Channel> channels = List.of(
                createChannelWithId(ChannelType.PUBLIC, "테스트방", "테스트입니다."),
                createChannelWithId(ChannelType.PRIVATE, null, null)
        );
        List<ChannelResponseDto> responseChannel = channels.stream()
                .map(channel -> new ChannelResponseDto(
                        channel.getId(),
                        channel.getType(),
                        channel.getName(),
                        channel.getDescription(),
                        responseUser,
                        Instant.MIN))
                .toList();


        given(channelService.findAllByUserId(users.get(0).getId())).willReturn(responseChannel);

        mockMvc.perform(get("/api/channels").param("userId", users.get(0).getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("테스트방"))
                .andExpect(jsonPath("$[1].type").value("PRIVATE"));
    }


    // User 생성
    private User createUserWithId(String name, String email, String password) {
        User user = new User(name, email, password, null);
        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
        return user;
    }

    // Channel 생성
    private Channel createChannelWithId(ChannelType channelType, String name, String description) {
        Channel channel = new Channel(channelType, name, description);
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());
        return channel;
    }
}
