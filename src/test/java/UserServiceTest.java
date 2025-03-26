import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @InjectMocks
    private BasicUserService userService;

    @BeforeEach
    void setUp() {
        userService = new BasicUserService(userRepository, binaryContentRepository, userStatusRepository);
    }

    @Test
    void reset_success() {
        // given
        boolean adminAuth = true;

        // when
        userService.reset(adminAuth);

        // then
        verify(userRepository, times(1)).reset();
    }

    @Test
    void reset_fail() {
        // given
        boolean adminAuth = false;

        // when
        userService.reset(adminAuth);

        // then
        verify(userRepository, times(0)).reset();

    }
}
