package com.sprint.mission.discodeit.controller;

import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class UserControllerTest {
    private UserRepository userRepository;
    private BinaryContentRepository binaryContentRepository;
    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        userService = new BasicUserService(userRepository, binaryContentRepository);
        userController = new UserController(userService);
    }

    @DisplayName("프로필 사진 저장을 선택하지 않았을떄 profile 아이디를 null을 반환합니다")
    @Test
    void registerException() {
        UserRegisterDto userRegisterDto = new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(),
                LONGIN_USER.getPassword(), null);
        UserDto user = userController.register(userRegisterDto);
        assertThat(user.profileId()).isNull();
    }

    @DisplayName("프로필 사진 저장을 선택했을떄 파일 저장 경로를 반환합니다")
    @Test
    void register() throws IOException {
        byte[] binaryProfileImage = loadImageFileFromResource();
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                binaryProfileImage
        );

        UserRegisterDto userRegisterDto = new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword(), file);
        UserDto user = userController.register(userRegisterDto);
        BinaryContent binaryContent = binaryContentRepository.findById(user.profileId());

        byte[] storedFileBytes = Files.readAllBytes(binaryContent.getPath());
        assertThat(binaryProfileImage).isEqualTo(storedFileBytes);
    }

    private byte[] loadImageFileFromResource() {
        byte[] binaryProfileImage;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dog.jpg")) {
            assert inputStream != null;
            binaryProfileImage = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("src/resources 파일에 dog파일이 없습니다.", e);
        }

        return binaryProfileImage;
    }
}