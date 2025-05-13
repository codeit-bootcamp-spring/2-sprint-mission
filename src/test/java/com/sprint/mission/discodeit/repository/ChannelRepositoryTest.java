package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@Sql(scripts = "classpath:schema.sql")
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    private Channel createChannel(ChannelType type, UUID id) {
        return Channel.builder()
                .type(type)
                .name("Test Channel")
                .build();
    }

    // 성공 케이스 1: 타입 조건으로 조회 (PUBLIC)
    @Test
    void findAllByTypeOrIdIn_ByPublicTypeSuccess() {
        // Given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        channelRepository.saveAll(List.of(
                createChannel(ChannelType.PUBLIC, id1),
                createChannel(ChannelType.PRIVATE, id2)
        ));

        // When
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PUBLIC,
                List.of()
        );

        // Then
        assertThat(result)
                .hasSize(1)
                .extracting(Channel::getType)
                .containsExactly(ChannelType.PUBLIC);
    }

    // 성공 케이스 2: ID 목록으로 조회 (PRIVATE 타입 채널을 ID로 검색)
    @Test
    void findAllByTypeOrIdIn_ByIdsSuccess() {
        // Given
        UUID targetId = UUID.randomUUID();
        channelRepository.saveAll(List.of(
                createChannel(ChannelType.PRIVATE, UUID.randomUUID()),
                createChannel(ChannelType.PRIVATE, targetId) // 타입은 PRIVATE
        ));

        // When
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PUBLIC, // 존재하지 않는 타입 조건
                List.of(targetId)   // ID로 검색
        );

        // Then
        assertThat(result)
                .hasSize(1)
                .extracting(Channel::getId)
                .containsExactly(targetId);
    }

    // 실패 케이스: 타입과 ID 모두 불일치
    @Test
    void findAllByTypeOrIdIn_NoResults() {
        // Given
        channelRepository.save(createChannel(ChannelType.PUBLIC, UUID.randomUUID()));

        // When
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PRIVATE, // 저장된 타입과 다름
                List.of(UUID.randomUUID()) // 존재하지 않는 ID
        );

        // Then
        assertThat(result).isEmpty();
    }

    // 추가 테스트: 타입과 ID 조합 검증
    @Test
    void findAllByTypeOrIdIn_Combination() {
        // Given
        UUID publicId = UUID.randomUUID();
        UUID privateId = UUID.randomUUID();
        channelRepository.saveAll(List.of(
                createChannel(ChannelType.PUBLIC, publicId),
                createChannel(ChannelType.PRIVATE, privateId)
        ));

        // When: PUBLIC 타입이거나 privateId인 경우
        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PUBLIC,
                List.of(privateId)
        );

        // Then: PUBLIC 1개 + PRIVATE 1개 = 총 2개
        assertThat(result)
                .hasSize(2)
                .extracting(Channel::getId)
                .containsExactlyInAnyOrder(publicId, privateId);
    }
}