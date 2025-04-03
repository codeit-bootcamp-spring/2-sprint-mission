//package Service;
//
//import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
//import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
//import com.sprint.mission.discodeit.dto.service.user.UserDTO;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.exception.RestException;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.Assertions.*;
//
//
//@ExtendWith(MockitoExtension.class)
//public class BasicUserServiceTest {
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    UserStatusRepository userStatusRepository;
//
//    @Mock
//    BinaryContentRepository binaryContentRepository;
//
//    @Mock
//    MultipartFile multipartFile;
//
//    @InjectMocks
//    BasicUserService basicUserService;
//
//    private static final UUID DEFAULT_PROFILE_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
//
//    private CreateUserParam createUserParam;
//    private User mockUser;
//    private UserStatus mockUserStatus;
//
//    @BeforeEach
//    void setUp() {
//        createUserParam = new CreateUserParam("test", "test@test.com", "1234");
//
//        mockUser = User.builder()
//                .username(createUserParam.username())
//                .password(createUserParam.password())
//                .email(createUserParam.email())
//                .profileId(DEFAULT_PROFILE_ID)
//                .build();
//
//        mockUserStatus = UserStatus.builder()
//                .userId(mockUser.getId())
//                .build();
//    }
//
//
//    @Test
//    void 유저생성_성공() {
//        // 가짜 객체기 때문에 어떤 기능이 실행됐을 때, 어떤걸 반환해야할지 모름 -> Mock 설정 필요
//        // Repository의 메서드가 실행되면 DB대신 미리 만들어둔 객체나 결과값 반환
//        when(userRepository.save(any(User.class))).thenReturn(mockUser);
//        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(mockUserStatus);
//        when(userRepository.existsByUsername(createUserParam.username())).thenReturn(false);
//        when(userRepository.existsByEmail(createUserParam.email())).thenReturn(false);
//
//        UserDTO userDTO = basicUserService.create(createUserParam, );
//
//        assertEquals(createUserParam.username(), userDTO.username());
//        assertEquals(createUserParam.email(), userDTO.email());
//        assertEquals(createUserParam.profileId(), userDTO.profileId());
//        assertEquals(true, userDTO.isLogin());
//
//        // create 1번에 1번만 실행됐는지 확인
//        verify(userRepository, times(1)).save(any(User.class));
//        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
//    }
//
//    @Test
//    void 유저생성_중복이름_실패() {
//        when(userRepository.existsByUsername(createUserParam.username())).thenReturn(true);
//        // 중복이름을 먼저 검사하고 email을 검사하기 아래의 코드가 있다면 UnnecessaryStubbingException 발생
//        //when(userRepository.existsByEmail(createUserParam.email())).thenReturn(false);
//
//        assertThatThrownBy(() -> basicUserService.create(createUserParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("Username exists already");
//
//        verify(userRepository, times(1)).existsByUsername(createUserParam.username());
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void 유저생성_중복이메일_실패() {
//        when(userRepository.existsByUsername(createUserParam.username())).thenReturn(false);
//        when(userRepository.existsByEmail(createUserParam.email())).thenReturn(true);
//
//        assertThatThrownBy(() -> basicUserService.create(createUserParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("Email exists already");
//
//        verify(userRepository, times(1)).existsByEmail(createUserParam.email());
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//
//    @Test
//    void 유저하나찾기_성공() {
//        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.ofNullable(mockUser));
//        when(userStatusRepository.findByUserId(mockUser.getId())).thenReturn(Optional.ofNullable(mockUserStatus));
//
//        UserDTO userDTO = basicUserService.find(mockUser.getId());
//
//        assertEquals(userDTO.username(), mockUser.getUsername());
//        assertEquals(userDTO.email(), mockUser.getEmail());
//        assertEquals(userDTO.profileId(), mockUser.getProfileId());
//        assertEquals(true, userDTO.isLogin());
//
//        verify(userRepository, times(1)).findById(mockUser.getId());
//        verify(userStatusRepository, times(1)).findByUserId(mockUser.getId());
//    }
//
//    @Test
//    void 유저하나찾기_유저_못찾음() {
//        // 검색 결과가 Optional.empty()를 반환한다고 가정
//        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicUserService.find(mockUser.getId()))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("User not found");
//
//        verify(userRepository, times(1)).findById(mockUser.getId());
//    }
//
//    @Test
//    void 유저하나찾기_유저상태_못찾음() {
//        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.ofNullable(mockUser));
//        when(userStatusRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicUserService.find(mockUser.getId()))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("UserStatus not found");
//
//        verify(userStatusRepository, times(1)).findByUserId(mockUser.getId());
//    }
//
//
//    List<User> mockUserList;
//    List<UserStatus> mockUserStatusList;
//
//    @Test
//    void 유저전체찾기_성공() {
//        mockUserList = List.of(
//                new User("test2", "test2@test.com", "1234", DEFAULT_PROFILE_ID),
//                new User("test3", "test3@test.com", "1234", DEFAULT_PROFILE_ID)
//        );
//
//        mockUserStatusList = List.of(
//                new UserStatus(mockUserList.get(0).getId()),
//                new UserStatus(mockUserList.get(1).getId())
//        );
//
//        when(userRepository.findAll()).thenReturn(mockUserList);
//        when(userStatusRepository.findAll()).thenReturn(mockUserStatusList);
//
//        List<UserDTO> userDTOList = basicUserService.findAll();
//
//        assertEquals(2, userDTOList.size());
//        assertEquals(mockUserList.get(0).getUsername(), userDTOList.get(0).username());
//        assertEquals(mockUserList.get(1).getUsername(), userDTOList.get(1).username());
//
//        verify(userRepository, times(1)).findAll();
//        verify(userStatusRepository, times(1)).findAll();
//    }
//
//    @Test
//    void 유저전체찾기_빈리스트_성공() {
//        when(userRepository.findAll()).thenReturn(List.of());
//        when(userStatusRepository.findAll()).thenReturn(List.of());
//
//        List<UserDTO> userDTOList = basicUserService.findAll();
//
//        assertTrue(userDTOList.isEmpty());
//
//        verify(userRepository, times(1)).findAll();
//        verify(userStatusRepository, times(1)).findAll();
//   }
//    UpdateUserParam updateUserParam = new UpdateUserParam("updateTest", "updateTest@test.com", "update1234", DEFAULT_PROFILE_ID);
//
//    @Test
//    void 유저수정_성공() {
//        UUID userId = mockUser.getId();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(mockUser));
//        when(userRepository.save(any(User.class))).thenReturn(mockUser);
//
//        UUID id = basicUserService.update(userId, updateUserParam);
//
//        assertEquals(id, userId);
//
//        assertEquals(mockUser.getUsername(), updateUserParam.username());
//        assertEquals(mockUser.getEmail(), updateUserParam.email());
//        assertEquals(mockUser.getPassword(), updateUserParam.password());
//        assertEquals(mockUser.getProfileId(), updateUserParam.profileId());
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).save(mockUser);
//    }
//
//    @Test
//    void 유저수정_유저없음_실패() {
//        UUID userId = mockUser.getId();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicUserService.update(userId, updateUserParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("User not found");
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, never()).save(mockUser);
//    }
//
//    @Test
//    void 유저삭제_성공() {
//        UUID userId = mockUser.getId();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(mockUser));
//
//        basicUserService.delete(userId);
//
//        verify(userRepository, times(1)).deleteById(userId);
//        verify(binaryContentRepository, times(1)).deleteById(mockUser.getProfileId());
//        verify(userStatusRepository, times(1)).deleteByUserId(userId);
//    }
//
//    @Test
//    void 유저삭제_유저없음_실패() {
//        UUID userId = mockUser.getId();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicUserService.delete(userId))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("User not found");
//
//        verify(userRepository, never()).deleteById(userId);
//        verify(binaryContentRepository, never()).deleteById(mockUser.getProfileId());
//        verify(userStatusRepository, never()).deleteByUserId(userId);
//    }
//}
//
//
//
