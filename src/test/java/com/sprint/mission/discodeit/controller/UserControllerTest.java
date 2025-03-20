package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;

import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {
    private UserStatusRepository userStatusRepository;
    private UserRepository userRepository;
    private BinaryContentRepository binaryContentRepository;
    private UserService userService;
    private BinaryContentService binaryContentService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userStatusRepository = new JCFUserStatusRepository();
        userRepository = new JCFUserRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        userService = new BasicUserService(userRepository, userStatusRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        userController = new UserController(userService, binaryContentService);
    }

    @DisplayName("프로필 사진 저장을 선택하지 않았을떄 profile 아이디를 null을 반환합니다")
    @Test
    void registerException() {
        UserRegisterDto userRegisterDto = new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(),
                LONGIN_USER.getPassword());
        UserDto user = userController.register(userRegisterDto, null);
        assertThat(user.profileId()).isNull();
    }

    @DisplayName("프로필 사진 저장을 선택했을떄 파일 저장 경로를 반환합니다")
    @Test
    void register() throws IOException {
        byte[] binaryProfileImage = loadImageFileFromResource("dog.jpg");
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                binaryProfileImage
        );

        UserRegisterDto userRegisterDto = new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword());

        UserDto user = userController.register(userRegisterDto, file);
        BinaryContent binaryContent = binaryContentRepository.findById(user.profileId());

        byte[] storedFileBytes = Files.readAllBytes(binaryContent.getPath());
        assertThat(binaryProfileImage).isEqualTo(storedFileBytes);
    }

    @DisplayName("사용자 이미지를 업데이트하고 유저 정보를 반환합니다")
    @Test
    void updateProfileImage() throws IOException {
        byte[] existingProfileImage = loadImageFileFromResource("dog.jpg");
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                existingProfileImage
        );
        UserRegisterDto userRegisterDto = new UserRegisterDto(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword());

        UserDto user = userController.register(userRegisterDto, file);


        byte[] updatedProfileImage = loadImageFileFromResource("Kirby.jpg");
        MockMultipartFile otherFile = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                updatedProfileImage
        );
        UserDto profileImageUpdatedUser = userController.updateProfile(user.id(), otherFile);

        BinaryContent binaryContent = binaryContentRepository.findById(profileImageUpdatedUser.profileId());
        byte[] storedFileBytes = Files.readAllBytes(binaryContent.getPath());

        assertThat(updatedProfileImage).isEqualTo(storedFileBytes);
    }

    private byte[] loadImageFileFromResource(String filePath) {
        byte[] binaryProfileImage;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            assert inputStream != null;
            binaryProfileImage = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("src/resources 파일에" + filePath + "파일이 없습니다.", e);
        }

        return binaryProfileImage;
    }
}