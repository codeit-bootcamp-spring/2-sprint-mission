package Service;

import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
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
public class BasicReadStatusServiceTest {

    @Mock
    ReadStatusRepository readStatusRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ChannelRepository channelRepository;

    @InjectMocks
    BasicReadStatusService basicReadStatusService;

    private UUID userId;
    private UUID channelId;
    private ReadStatus mockReadStatus;
    private CreateReadStatusParam createReadStatusParam;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        channelId = UUID.randomUUID();

        createReadStatusParam = new CreateReadStatusParam(userId, channelId);

        mockReadStatus = ReadStatus.builder()
                .userId(userId)
                .channelId(channelId)
                .build();
    }

    @Test
    void 읽음상태생성_성공() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock()));
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(mock()));
        when(readStatusRepository.existsByUserIdAndChannelId(userId, channelId)).thenReturn(false);
        when(readStatusRepository.save(any(ReadStatus.class))).thenReturn(mockReadStatus);

        ReadStatusDTO result = basicReadStatusService.create(createReadStatusParam);

        assertEquals(userId, result.userId());
        assertEquals(channelId, result.channelId());

        verify(readStatusRepository, times(1)).save(any(ReadStatus.class));
    }

    @Test
    void 읽음상태생성_유저없음_실패() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicReadStatusService.create(createReadStatusParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("User not found");

        verify(readStatusRepository, never()).save(any());
    }

    @Test
    void 읽음상태생성_채널없음_실패() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock()));
        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicReadStatusService.create(createReadStatusParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("Channel not found");

        verify(readStatusRepository, never()).save(any());
    }

    @Test
    void 읽음상태생성_중복_실패() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock()));
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(mock()));
        when(readStatusRepository.existsByUserIdAndChannelId(userId, channelId)).thenReturn(true);

        assertThatThrownBy(() -> basicReadStatusService.create(createReadStatusParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("ReadStatus exists already");

        verify(readStatusRepository, never()).save(any());
    }

    @Test
    void 읽음상태조회_성공() {
        UUID id = mockReadStatus.getId();

        when(readStatusRepository.findById(id)).thenReturn(Optional.of(mockReadStatus));

        ReadStatusDTO result = basicReadStatusService.find(id);

        assertEquals(userId, result.userId());
        assertEquals(channelId, result.channelId());

        verify(readStatusRepository, times(1)).findById(id);
    }

    @Test
    void 읽음상태조회_실패() {
        UUID id = UUID.randomUUID();

        when(readStatusRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicReadStatusService.find(id))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("ReadStatus not found");
    }

    @Test
    void 유저기준_읽음상태전체조회_성공() {
        List<ReadStatus> list = List.of(mockReadStatus);

        when(readStatusRepository.findAllByUserId(userId)).thenReturn(list);

        List<ReadStatusDTO> result = basicReadStatusService.findAllByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).userId());
    }

    @Test
    void 채널기준_읽음상태전체조회_성공() {
        List<ReadStatus> list = List.of(mockReadStatus);

        when(readStatusRepository.findAllByChannelId(channelId)).thenReturn(list);

        List<ReadStatusDTO> result = basicReadStatusService.findAllByChannelId(channelId);

        assertEquals(1, result.size());
        assertEquals(channelId, result.get(0).channelId());
    }

    @Test
    void 읽음상태수정_성공() {
        UUID id = mockReadStatus.getId();
        UpdateReadStatusParam updateParam = new UpdateReadStatusParam(id);

        when(readStatusRepository.findById(id)).thenReturn(Optional.of(mockReadStatus));
        when(readStatusRepository.save(any())).thenReturn(mockReadStatus);

        UUID result = basicReadStatusService.update(updateParam);

        assertEquals(id, result);
        verify(readStatusRepository, times(1)).save(mockReadStatus);
    }

    @Test
    void 읽음상태수정_실패() {
        UUID id = UUID.randomUUID();
        UpdateReadStatusParam updateParam = new UpdateReadStatusParam(id);

        when(readStatusRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicReadStatusService.update(updateParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("ReadStatus not found");

        verify(readStatusRepository, never()).save(any());
    }

    @Test
    void 읽음상태삭제_성공() {
        UUID id = UUID.randomUUID();

        basicReadStatusService.delete(id);

        verify(readStatusRepository, times(1)).deleteById(id);
    }

    @Test
    void 채널기준_읽음상태삭제_성공() {
        basicReadStatusService.deleteByChannelId(channelId);

        verify(readStatusRepository, times(1)).deleteByChannelId(channelId);
    }
}