package Service;

import com.sprint.mission.discodeit.dto.service.userStatus.CreatedUserStatusParam;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusParam;
import com.sprint.mission.discodeit.dto.service.userStatus.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasicUserStatusServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserStatusRepository userStatusRepository;

    @InjectMocks
    BasicUserStatusService basicUserStatusService;

    private UUID userId;
    private CreatedUserStatusParam createdUserStatusParam;
    private UserStatus mockUserStatus;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        createdUserStatusParam = new CreatedUserStatusParam(userId);

        mockUserStatus = UserStatus.builder()
                .userId(userId)
                .build();
    }

    @Test
    void 유저상태생성_성공() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock()));
        when(userStatusRepository.existsByUserId(userId)).thenReturn(false);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(mockUserStatus);

        UserStatusDTO result = basicUserStatusService.create(createdUserStatusParam);

        assertEquals(userId, result.userId());
        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    }

    @Test
    void 유저상태생성_유저없음_실패() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicUserStatusService.create(createdUserStatusParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("User not found");

        verify(userStatusRepository, never()).save(any());
    }

    @Test
    void 유저상태생성_중복_실패() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock()));
        when(userStatusRepository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> basicUserStatusService.create(createdUserStatusParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("UserStatus exists already");

        verify(userStatusRepository, never()).save(any());
    }

    @Test
    void 유저상태조회_성공() {
        UUID id = mockUserStatus.getId();
        when(userStatusRepository.findById(id)).thenReturn(Optional.of(mockUserStatus));

        UserStatusDTO result = basicUserStatusService.findById(id);

        assertEquals(mockUserStatus.getUserId(), result.userId());
        verify(userStatusRepository, times(1)).findById(id);
    }

    @Test
    void 유저상태조회_실패() {
        UUID id = UUID.randomUUID();
        when(userStatusRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicUserStatusService.findById(id))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("UserStatus not found");
    }

    @Test
    void 유저상태전체조회_성공() {
        List<UserStatus> statusList = List.of(mockUserStatus);
        when(userStatusRepository.findAll()).thenReturn(statusList);

        List<UserStatusDTO> result = basicUserStatusService.findAll();

        assertEquals(1, result.size());
        assertEquals(mockUserStatus.getUserId(), result.get(0).userId());
    }

    @Test
    void 유저상태수정_성공() {
        UUID id = mockUserStatus.getId();
        UpdateUserStatusParam updateParam = new UpdateUserStatusParam(id);

        when(userStatusRepository.findById(id)).thenReturn(Optional.of(mockUserStatus));
        when(userStatusRepository.save(any())).thenReturn(mockUserStatus);

        UUID result = basicUserStatusService.update(updateParam);

        assertEquals(id, result);
        verify(userStatusRepository, times(1)).save(mockUserStatus);
    }

    @Test
    void 유저상태수정_실패() {
        UUID id = UUID.randomUUID();
        UpdateUserStatusParam updateParam = new UpdateUserStatusParam(id);

        when(userStatusRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicUserStatusService.update(updateParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("UserStatus not found");

        verify(userStatusRepository, never()).save(any());
    }

    @Test
    void 유저상태수정_ByUserId_성공() {
        UUID id = mockUserStatus.getId();
        UpdateUserStatusParam updateParam = new UpdateUserStatusParam(id);

        when(userStatusRepository.findById(id)).thenReturn(Optional.of(mockUserStatus));

        UUID result = basicUserStatusService.updateByUserId(userId, updateParam);

        assertEquals(id, result);
    }

    @Test
    void 유저상태수정_ByUserId_실패() {
        UUID id = UUID.randomUUID();
        UpdateUserStatusParam updateParam = new UpdateUserStatusParam(id);

        when(userStatusRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicUserStatusService.updateByUserId(userId, updateParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("UserStatus not found");
    }

    @Test
    void 유저상태삭제_성공() {
        UUID id = UUID.randomUUID();

        basicUserStatusService.delete(id);

        verify(userStatusRepository, times(1)).deleteById(id);
    }
}