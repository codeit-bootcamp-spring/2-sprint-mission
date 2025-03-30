package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResponse;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
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
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        userController = new UserController(userService, binaryContentService, new BasicUserStatusService(userStatusRepository, userRepository));
    }

    @DisplayName("프로필 사진 저장을 선택하지 않았을 때 프로필 아이디가 null을 반환한다.")
    @Test
    void registerException() {
        UserRequest userRequest = new UserRequest(LOGIN_USER.getName(), LOGIN_USER.getEmail(),
                LOGIN_USER.getPassword());
        UserResult user = userController.register(userRequest, null).getBody();
        assertThat(user.profileId()).isNull();
    }

    @DisplayName("프로필 사진을 저장하면 파일 저장 경로를 반환한다.")
    @Test
    void register() {
        byte[] binaryProfileImage = loadImageFileFromResource("dog.jpg");
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                binaryProfileImage
        );

        UserRequest userRequest = new UserRequest(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword());

        UserResult user = userController.register(userRequest, file).getBody();
        BinaryContentResult binaryContentResult = binaryContentService.findById(user.profileId());

        assertThat(binaryProfileImage).isEqualTo(binaryContentResult.bytes());
    }

    @DisplayName("처음 등록한 유저의 로그인 상태는 false를 반환한다.")
    @Test
    void registerValidateUserStatus() {
        UserRequest userRequest = new UserRequest(OTHER_USER.getName(), OTHER_USER.getEmail(), LOGIN_USER.getPassword());
        UserResult user = userController.register(userRequest, null).getBody();
        UserResponse userResponse = userController.findById(user.id()).getBody();

        assertThat(userResponse.isLogin()).isFalse();
    }

    @DisplayName("사용자 프로필 이미지를 업데이트하면 변경된 이미지 정보가 반영된다.")
    @Test
    void updateProfileImage() {
        byte[] existingProfileImage = loadImageFileFromResource("dog.jpg");
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                existingProfileImage
        );
        UserRequest userRequest = new UserRequest(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword());

        UserResult user = userController.register(userRequest, file).getBody();


        byte[] updatedProfileImage = loadImageFileFromResource("Kirby.jpg");
        MockMultipartFile otherFile = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                updatedProfileImage
        );
        UserResult profileImageUpdatedUser = userController.updateProfileImage(user.id(), otherFile).getBody();

        BinaryContentResult binaryContentResult = binaryContentService.findById(profileImageUpdatedUser.profileId());

        assertThat(updatedProfileImage).isEqualTo(binaryContentResult.bytes());
    }

    @DisplayName("유저 삭제 시 프로필 이미지와 UserStatus도 삭제된다.")
    @Test
    void delete() {
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                MESSAGE_CONTENT.getBytes()
        );
        UserRequest userRequest = new UserRequest(OTHER_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword());


        UserResult user = userController.register(userRequest, file).getBody();
        userController.delete(user.id());

        assertAll(
                () -> assertThat(userRepository.findById(user.id())).isEmpty(),
                () -> assertThat(userStatusRepository.findByUserId(user.id())).isEmpty(),
                () -> assertThat(binaryContentRepository.findById(user.profileId())).isEmpty()
        );
    }


    public byte[] loadImageFileFromResource(String filePath) {
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