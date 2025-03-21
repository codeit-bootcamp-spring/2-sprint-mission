import com.sprint.mission.discodeit.DTO.RequestToService.UserCreateDTO;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ServerRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.BasicServerService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ServerServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private BasicUserService userService;

    @InjectMocks
    private BasicServerService serverService;

    UserCreateDTO userCreateDTO;

    @BeforeEach
    void setUp() {
        userService = new BasicUserService(userRepository, binaryContentRepository, userStatusRepository);
        serverService = new BasicServerService(userRepository, serverRepository);

    }


    @Test
    void delete_Successful() {
//        // given
//        UUID randomUserId = UUID.randomUUID();
//        User mockUser = createMockUser(randomUserId);
//        ServerCreateRequestDTO serverCreateRequestDTO = new ServerCreateRequestDTO(randomUserId, "deleteServer");
//
//        when(userRepository.find(randomUserId)).thenReturn(mockUser);
//        when(serverRepository.find(server.getServerId())).thenReturn(server);
//
//        Server server = serverService.create(serverCreateRequestDTO);

//
//
//        // when
//        serverService.delete(server.getOwnerId().toString());
//
//        // then
//        verify(serverRepository).remove(server.getOwnerId());
//

    }

    static User createMockUser(UUID userId) {
        return new User(userId, null, Instant.now(), null, null, null);
    }

}
