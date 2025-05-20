package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class BinaryContentRepositoryTest {

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private BinaryContent binaryContent1;
    private BinaryContent binaryContent2;
    private BinaryContent binaryContent3;

    @BeforeEach
    void setUp() {
        binaryContentRepository.deleteAllInBatch();

        binaryContent1 = new BinaryContent("테스트1", 11L, "image/jpeg");
        binaryContent2 = new BinaryContent("테스트2", 12L, "image/jpeg");
        binaryContent3 = new BinaryContent("테스트3", 13L, "image/jpeg");

        binaryContentRepository.save(binaryContent1);
        binaryContentRepository.save(binaryContent2);
        binaryContentRepository.save(binaryContent3);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void 이미지_목록_조회_테스트() {
        List<UUID> binaryContentIds = List.of(binaryContent1.getId(), binaryContent2.getId(),
            binaryContent3.getId());

        List<BinaryContent> binaryContents = binaryContentRepository.findByIdIn(binaryContentIds);

        assertThat(binaryContents).hasSize(3);
    }

}