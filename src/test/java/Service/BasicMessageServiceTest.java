//package Service;
//
//import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
//import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;
//import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.exception.RestException;
//import com.sprint.mission.discodeit.exception.RestExceptions;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BasicMessageServiceTest {
//
//    @Mock
//    MessageRepository messageRepository;
//
//    @Mock
//    ChannelRepository channelRepository;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    BinaryContentRepository binaryContentRepository;
//
//    @InjectMocks
//    BasicMessageService basicMessageService;
//
//    private CreateMessageParam createMessageParam;
//    private Message mockMessage;
//    private UUID authorId;
//    private UUID channelId;
//
//    @BeforeEach
//    void setUp() {
//        authorId = UUID.randomUUID();
//        channelId = UUID.randomUUID();
//
//        createMessageParam = new CreateMessageParam("test", null, channelId,authorId);
//
//        mockMessage = Message.builder()
//                .authorId(authorId)
//                .channelId(channelId)
//                .content(createMessageParam.content())
//                .attachmentIds(createMessageParam.attachmentsId())
//                .build();
//    }
//
//    @Test
//    void 메시지생성_성공() {
//        when(userRepository.findById(authorId)).thenReturn(Optional.of(mock()));
//        when(channelRepository.findById(channelId)).thenReturn(Optional.of(mock()));
//        when(messageRepository.save(any(Message.class))).thenReturn(mockMessage);
//
//        MessageDTO messageDTO = basicMessageService.create(createMessageParam);
//
//        assertEquals(createMessageParam.content(), messageDTO.content());
//        assertEquals(createMessageParam.authorId(), messageDTO.authorId());
//        assertEquals(createMessageParam.channelId(), messageDTO.channelId());
//        assertEquals(createMessageParam.attachmentsId(), messageDTO.attachmentIds());
//
//        verify(messageRepository, times(1)).save(any(Message.class));
//    }
//
//    @Test
//    void 메시지생성_빈내용_실패() {
//        CreateMessageParam badParam = new CreateMessageParam(null, null,channelId,authorId);
//
//        assertThatThrownBy(() -> basicMessageService.create(badParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("BAD REQUEST");
//    }
//
//    @Test
//    void 메시지생성_유저없음_실패() {
//        when(userRepository.findById(authorId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicMessageService.create(createMessageParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("User not found");
//    }
//
//    @Test
//    void 메시지생성_채널없음_실패() {
//        when(userRepository.findById(authorId)).thenReturn(Optional.of(mock()));
//        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicMessageService.create(createMessageParam))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("Channel not found");
//    }
//
//    @Test
//    void 메시지조회_성공() {
//        when(messageRepository.findById(mockMessage.getId())).thenReturn(Optional.of(mockMessage));
//
//        MessageDTO result = basicMessageService.find(mockMessage.getId());
//
//        assertEquals(mockMessage.getId(), result.id());
//        assertEquals(mockMessage.getContent(), result.content());
//        assertEquals(mockMessage.getAuthorId(), result.authorId());
//        assertEquals(mockMessage.getChannelId(), result.channelId());
//
//        verify(messageRepository, times(1)).findById(mockMessage.getId());
//    }
//
//    @Test
//    void 메시지조회_실패() {
//        UUID messageId = UUID.randomUUID();
//        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicMessageService.find(messageId))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("Message not found");
//    }
//
//    @Test
//    void 채널별_메시지전체조회_성공() {
//        List<Message> messages = List.of(
//                Message.builder().channelId(channelId).authorId(authorId).content("msg1").attachmentIds(List.of()).build(),
//                Message.builder().channelId(channelId).authorId(authorId).content("msg2").attachmentIds(List.of()).build()
//        );
//
//        when(messageRepository.findAllByChannelId(channelId)).thenReturn(messages);
//
//        List<MessageDTO> result = basicMessageService.findAllByChannelId(channelId);
//
//        assertEquals(2, result.size());
//        assertEquals("msg1", result.get(0).content());
//        assertEquals("msg2", result.get(1).content());
//    }
//
//    @Test
//    void 메시지수정_성공() {
//        UpdateMessageParam updateParam = new UpdateMessageParam(mockMessage.getId(), "updated content");
//
//        when(messageRepository.findById(updateParam.id())).thenReturn(Optional.of(mockMessage));
//        when(messageRepository.save(any(Message.class))).thenReturn(mockMessage);
//
//        UUID result = basicMessageService.update(updateParam);
//
//        assertEquals(mockMessage.getId(), result);
//        assertEquals("updated content", mockMessage.getContent());
//
//        verify(messageRepository, times(1)).save(mockMessage);
//    }
//
//    @Test
//    void 메시지삭제_성공_첨부파일O() {
//        Message messageWithAttachments = Message.builder()
//                .authorId(authorId)
//                .channelId(channelId)
//                .content("with attachments")
//                .attachmentIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
//                .build();
//
//        when(messageRepository.findById(messageWithAttachments.getId())).thenReturn(Optional.of(messageWithAttachments));
//
//        basicMessageService.delete(messageWithAttachments.getId());
//
//        verify(binaryContentRepository, times(2)).deleteById(any(UUID.class));
//        verify(messageRepository, times(1)).deleteById(messageWithAttachments.getId());
//    }
//
//    @Test
//    void 메시지삭제_성공_첨부파일X() {
//        Message messageNoAttachments = Message.builder()
//                .authorId(authorId)
//                .channelId(channelId)
//                .content("no attachments")
//                .attachmentIds(Collections.emptyList())
//                .build();
//
//        when(messageRepository.findById(messageNoAttachments.getId())).thenReturn(Optional.of(messageNoAttachments));
//
//        basicMessageService.delete(messageNoAttachments.getId());
//
//        verify(binaryContentRepository, never()).deleteById(any());
//        verify(messageRepository, times(1)).deleteById(messageNoAttachments.getId());
//    }
//
//    @Test
//    void 메시지삭제_실패_메시지없음() {
//        UUID messageId = UUID.randomUUID();
//
//        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> basicMessageService.delete(messageId))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("Message not found");
//
//        verify(messageRepository, never()).deleteById(messageId);
//    }
//}