package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestJpaConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
public class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @BeforeEach
    void setUp() {
        channelRepository.saveAll(List.of(
            new Channel("general", "일반 채널", ChannelType.PUBLIC),
            new Channel("random", "랜덤 채널", ChannelType.PUBLIC),
            new Channel("private", "비공개 채널", ChannelType.PRIVATE)
        ));
    }

    @Test
    @DisplayName("findAll(): 페이징 요청 시 지정한 사이즈 만큼 채널 반환")
    void findAll_withPaging_shouldReturnLimitedResults() {
        // when
        Page<Channel> page = channelRepository.findAll(PageRequest.of(0, 2));

        // then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("findAll(): 너무 큰 페이지를 요청하면 빈 결과를 반환")
    void findAll_withInvalidPage_shouldReturnEmpty() {
        // when
        Page<Channel> page = channelRepository.findAll(PageRequest.of(10, 5));

        // then
        assertThat(page.getContent()).isEmpty();
    }

    @Test
    @DisplayName("findAll(): name 기준 내림 차순 정렬 적용")
    void findAll_withSorting_shouldReturnInDescendingOrder() {
        // when
        Page<Channel> page = channelRepository.findAll(
            PageRequest.of(0, 3, Sort.by("name").descending()));

        // then
        List<String> names = page.getContent().stream().map(Channel::getName).toList();
        assertThat(names).containsExactly("random", "private", "general");
    }


}
