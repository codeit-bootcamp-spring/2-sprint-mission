package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.application.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.FileUtils.getBytesFromMultiPartFile;
import static com.sprint.mission.discodeit.util.mock.file.MockFile.createMockImageFile;
import static com.sprint.mission.discodeit.util.mock.user.MockUser.createMockUserRequest;
import static com.sprint.mission.discodeit.util.mock.user.UserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.util.mock.user.UserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserServiceTest {

    private UserService userService;
    private UserResult setUpUser;
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        userService = new BasicUserService(userRepository, userStatusRepository,
                binaryContentRepository);

        UserCreateRequest mockUserRequest = createMockUserRequest(LOGIN_USER.getName(),
                LOGIN_USER.getEmail());
        setUpUser = userService.register(mockUserRequest, null);
    }

    @DisplayName("이미 등록된 유저 이메일로 등록한다면 예외를 반환합니다")
    @Test
    void registerDuplicateEmail() {
        UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
                LOGIN_USER.getEmail());

        assertThatThrownBy(() -> userService.register(mockUserRequest, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 등록된 유저 이름으로 등록한다면 예외를 반환합니다.")
    @Test
    void registerDuplicateUserName() {
        UserCreateRequest mockUserRequest = createMockUserRequest(LOGIN_USER.getName(),
                OTHER_USER.getEmail());

        assertThatThrownBy(() -> userService.register(mockUserRequest, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("프로필 이미지를 null로 설정하면 null을 반환합니다.")
    @Test
    void register_ProfileNull() {
        assertThat(setUpUser.profileId()).isNull();
    }

    @DisplayName("프로필 사진을 저장할 경우, 유저 객체에 ID로 저장한 결과를 반환합니다..")
    @Test
    void register() {
        UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
                OTHER_USER.getEmail());
        MultipartFile imageFile = createMockImageFile("dog.jpg");
        BinaryContentRequest binaryContentRequest = new BinaryContentRequest(imageFile.getName(), imageFile.getContentType(), getBytesFromMultiPartFile(imageFile));

        UserResult userWithImage = userService.register(mockUserRequest, binaryContentRequest);
        Optional<BinaryContent> binaryContent = binaryContentRepository.findByBinaryContentId(
                userWithImage.profileId());

        assertThat(binaryContent).map(BinaryContent::getId)
                .hasValue(userWithImage.profileId());
    }

    @DisplayName("유저 등롟시 프로필 사진 저장을 하지 않은 경우, 프로필 아이디를 null을 반환합니다.")
    @Test
    void registerException() {
        UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
                OTHER_USER.getEmail());

        UserResult user = userService.register(mockUserRequest, null);
        assertThat(user.profileId()).isNull();
    }

    @DisplayName("처음 등록된 유저의 경우, 로그인 상태는 false를 반환합니다.")
    @Test
    void registerValidateUserStatus() {
        UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
                OTHER_USER.getEmail());
        UserResult user = userService.register(mockUserRequest, null);

        assertThat(user.online()).isTrue();
    }

    @DisplayName("유저 조회 시 등록되지않은 ID로 조회한다면 예외를 반환합니다.")
    @Test
    void getByIdThrowsExceptionWhenUserNotFound() {
        UUID randomUUID = UUID.randomUUID();

        assertThatThrownBy(() -> userService.getById(randomUUID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유저 ID로 조회할 경우, 해당 Id를 가진 유저를 반환합니다.")
    @Test
    void getByIdReturnsUser() {
        UserResult userResult = userService.getById(setUpUser.id());

        assertThat(userResult.id()).isEqualTo(setUpUser.id());
    }

    @DisplayName("유저 이름으로 조회할 경우, 같은 이름을 가진 유저를 반환합니다.")
    @Test
    void getByNameReturnsUser() {
        UserResult user = userService.getByName(LOGIN_USER.getName());

        assertThat(user.username()).isEqualTo(LOGIN_USER.getName());
    }

    @DisplayName("등록된 전체 유저를 가져옵니다.")
    @Test
    void getALL() {
        List<UserResult> user = userService.getAll();
        assertThat(user).extracting(UserResult::online)
                .containsExactlyInAnyOrder(true);
    }

    @DisplayName("다른 이름으로 업데이트할 경우, 변경된 이름을 반환합니다.")
    @Test
    void updateUserName() {
        UserResult user = userService.update(setUpUser.id(),
                new UserUpdateRequest(OTHER_USER.getName(), null, null), null);

        assertThat(user.username()).isEqualTo(OTHER_USER.getName());
    }

    @DisplayName("유저 프로필 이미지를 업데이트할 경우, 변경된 이미지를 반환하고 이전 이미지는 삭제합니다.")
    @Test
    void updateProfileImage() {
        UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
                OTHER_USER.getEmail());
        MultipartFile imageFile = createMockImageFile("dog.jpg");
        BinaryContentRequest binaryContentRequest = new BinaryContentRequest(imageFile.getName(), imageFile.getContentType(), getBytesFromMultiPartFile(imageFile));

        UserResult user = userService.register(mockUserRequest, binaryContentRequest);

        MultipartFile otherImageFile = createMockImageFile("Kirby.jpg");
        BinaryContentRequest otherBinaryContentRequest = new BinaryContentRequest(imageFile.getName(), imageFile.getContentType(), getBytesFromMultiPartFile(otherImageFile));

        UserResult updateProfileUser = userService.update(user.id(),
                new UserUpdateRequest(null, null, null), otherBinaryContentRequest);
        Optional<BinaryContent> binaryContent = binaryContentRepository.findByBinaryContentId(
                updateProfileUser.profileId());

        assertAll(
                () -> assertThat(binaryContent).map(BinaryContent::getBytes)
                        .hasValue(otherImageFile.getBytes()),
                () -> assertThat(binaryContentRepository.findByBinaryContentId(user.profileId())).isEmpty()
        );
    }

    @DisplayName("유저가 삭제할 경우, 연관된 유저 상태와 유저 프로필 이미지도 삭제됩니다.")
    @Test
    void deleteWithUserStatus() {
        userService.delete(setUpUser.id());

        assertAll(
                () -> assertThat(userRepository.findByUserId(setUpUser.id())).isEmpty(),
                () -> assertThat(userStatusRepository.findByUserId(setUpUser.id())).isEmpty(),
                () -> assertThat(
                        binaryContentRepository.findByBinaryContentId(setUpUser.profileId())).isEmpty()
        );
    }

    @DisplayName("유저를 삭제 할 경우, 반환값은 없습니다.")
    @Test
    void deleteUserAndVerifyNotFound() {
        UUID setUpUserId = setUpUser.id();
        userService.delete(setUpUserId);

        assertThatThrownBy(() -> userService.getById(setUpUserId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}