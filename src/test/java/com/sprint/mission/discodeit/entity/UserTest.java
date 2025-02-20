package com.sprint.mission.discodeit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void 업데이트시_수정시간_변경되는지_확인() {
        User user = new User("황지환");
        Long createdAt = user.getUpdatedAt();

        user.updateName("지환");
        Long updatedAt = user.getUpdatedAt();

        assertThat(createdAt).isEqualTo(updatedAt);
    }
}