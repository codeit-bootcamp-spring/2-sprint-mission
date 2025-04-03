//package Service;
//
//import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
//import com.sprint.mission.discodeit.dto.service.user.UserDTO;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.exception.RestException;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.basic.BasicAuthService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BasicAuthServiceTest {
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    UserStatusRepository userStatusRepository;
//
//    @InjectMocks
//    BasicAuthService basicAuthService;
//
//    User mockUser = User.builder()
//            .username("test")
//            .password("1234")
//            .email("test@test.com")
//            .build();
//
//    UserStatus mockUserStatus = UserStatus.builder()
//            .userId(mockUser.getId())
//            .build();
//
//    LoginParam loginParam = new LoginParam("test", "1234");
//
//    @Test
//    void 로그인_성공() {
//        when(userRepository.findByUsername(loginParam.username())).thenReturn(Optional.ofNullable(mockUser));
//        when(userStatusRepository.findByUserId(mockUser.getId())).thenReturn(Optional.ofNullable(mockUserStatus));
//
//        UserDTO userDTO = basicAuthService.login(loginParam);
//
//        assertEquals(userDTO.username(), loginParam.username());
//        assertEquals(userDTO.email(), mockUser.getEmail());
//
//        verify(userRepository,times(1)).findByUsername(loginParam.username());
//        verify(userStatusRepository, times(1)).findByUserId(mockUser.getId());
//    }
//
//    @Test
//    void 로그인_비밀번호틀림_실패() {
//        when(userRepository.findByUsername(loginParam.username())).thenReturn(Optional.ofNullable(mockUser));
//
//        LoginParam wrongLoginParam = new LoginParam("test", "wrong");
//
//        assertThatThrownBy(() -> basicAuthService.login(wrongLoginParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("Invalid password");
//
//        verify(userRepository, times(1)).findByUsername(loginParam.username());
//        verify(userStatusRepository, never()).findByUserId(mockUser.getId());
//    }
//
//    @Test
//    void 로그인_아이디틀림_실패() {
//        LoginParam wrongLoginParam = new LoginParam("wrongUsername", "1234");
//
//        when(userRepository.findByUsername(wrongLoginParam.username())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicAuthService.login(wrongLoginParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("User not found");
//
//        verify(userRepository, times(1)).findByUsername(wrongLoginParam.username());
//        verify(userStatusRepository, never()).findByUserId(mockUser.getId());
//    }
//
//    @Test
//    void 로그인_유저상태없음_실패() {
//        when(userRepository.findByUsername(loginParam.username())).thenReturn(Optional.ofNullable(mockUser));
//        when(userStatusRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicAuthService.login(loginParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("UserStatus not found");
//
//        verify(userRepository, times(1)).findByUsername(loginParam.username());
//        verify(userStatusRepository, times(1)).findByUserId(mockUser.getId());
//    }
//
//}
