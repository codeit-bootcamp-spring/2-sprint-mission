package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusResult;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ReadStatusController.class)
class ReadStatusControllerTest {

    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private ReadStatusService readStatusService;

    @Test
    void createReadStatus() {
        ReadStatus readStatus = new ReadStatus(UUID.randomUUID(), UUID.randomUUID());
        ReadStatusResult stubResult = ReadStatusResult.fromEntity(readStatus);
        when(readStatusService.create(any())).thenReturn(stubResult);

        assertThat(mockMvc.post()
                .uri("/api/readStatuses")
                .content(JsonConvertor.asString(new ReadStatusCreateRequest(UUID.randomUUID(), UUID.randomUUID())))
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(readStatus.getId().toString());
    }

    @Test
    void updateReadStatus() {
        ReadStatus readStatus = new ReadStatus(UUID.randomUUID(), UUID.randomUUID());
        ReadStatusResult stubResult = ReadStatusResult.fromEntity(readStatus);

        when(readStatusService.updateLastReadTime(any())).thenReturn(stubResult);

        assertThat(mockMvc.patch()
                .uri("/api/readStatuses/{id}", readStatus.getId()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.lastReadAt")
                .isEqualTo(readStatus.getLastReadTime().toString());
    }

    @Test
    void getUserReadStatus() {
        UUID userId = UUID.randomUUID();
        ReadStatus readStatus = new ReadStatus(userId, UUID.randomUUID());
        ReadStatusResult stubResult = ReadStatusResult.fromEntity(readStatus);

        when(readStatusService.getAllByUserId(any())).thenReturn(List.of(stubResult));

        assertThat(mockMvc.get()
                .uri("/api/readStatuses")
                .queryParam("userId", userId.toString()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].id")
                .isEqualTo(List.of(readStatus.getId().toString()));
    }

    @Test
    void delete() {
        UUID readStatusId = UUID.randomUUID();
        assertThat(mockMvc.delete().uri("/api/readStatuses/{id}", readStatusId))
                .hasStatus(HttpStatus.NO_CONTENT);
    }
}