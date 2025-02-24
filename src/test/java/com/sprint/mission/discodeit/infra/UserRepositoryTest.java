package com.sprint.mission.discodeit.infra;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.application.UserDto;
import com.sprint.mission.application.UserRegisterDto;
import com.sprint.mission.discodeit.jcf.JCFUserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRepositoryTest {
    private static final String PASSWORD = "password";
    private static final String NAME = "황지환";
    private UserRepository userRepository;
    private UserDto setUpUser;

    @BeforeEach
    void init() {
        userRepository = new JCFUserRepository();

        this.setUpUser = userRepository.register(new UserRegisterDto(NAME, PASSWORD));
    }

    @Test
    void 유저_UUID_단건_조회_예외테스트() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> userRepository.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 유저_UUID_단건_조회() {
        UUID id = setUpUser.id();
        UserDto userDto = userRepository.findById(id);

        assertThat(userDto.id()).isEqualTo(id);
    }

    @Test
    void 유저_이름_단건_조회() {
        List<UserDto> users = userRepository.findByName(NAME);
        UserDto userDto = users.get(0);

        assertThat(userDto.name()).isEqualTo(NAME);
    }

    @Test
    void 유저_이름_다수_조회() {
        UserRegisterDto userOtherHwang = new UserRegisterDto(NAME, PASSWORD + "123");
        UserRegisterDto userKim = new UserRegisterDto("KIM", PASSWORD);

        userRepository.register(userOtherHwang);
        userRepository.register(userKim);

        assertThat(userRepository.findByName(NAME)).hasSize(2);
    }

    @Test
    void 유저_이름_수정() {
        String userName = "김철수";
        userRepository.updateName(setUpUser.id(), userName);

        UserDto updatedUserInfo = userRepository.findById(setUpUser.id());

        assertThat(setUpUser.name()).isNotEqualTo(updatedUserInfo.name());
    }

    @Test
    void 유저_삭제() {
        UUID id = setUpUser.id();
        userRepository.delete(id);

        assertThatThrownBy(() -> userRepository.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}