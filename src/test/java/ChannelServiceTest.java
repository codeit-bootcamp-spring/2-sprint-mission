import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {
//
//  @Mock
//  private ChannelRepository channelRepository;
//
//  @Mock
//  private MessageRepository messageRepository;
//
//  @Mock
//  private ReadStatusRepository readStatusRepository;
//
//  @InjectMocks
//  private BasicChannelService channelService;
//
//  private UUID id;
//  private UUID authorId;
//
//  @BeforeEach
//  void setup() {
//    id = UUID.randomUUID();
//    authorId = UUID.randomUUID();
//  }
//
//  @Test
//  void find_shouldReturnChannelFindDTO_whenPublicChannel() {
//    Channel channel = new Channel(UUID.randomUUID(), authorId, "general");
//    ReflectionTestUtils.setField(channel, "id", id);
//    when(channelRepository.find(id)).thenReturn(channel);
//    when(messageRepository.findAllByChannelId(id)).thenReturn(List.of(
//        new Message(authorId, "general", id, "hi"),
//        new Message(authorId, "general", id, "hello")
//    ));
//
//    ChannelFindDTO result = channelService.find(id);
//
//    assertNotNull(result);
//    assertEquals(id, result.id());
//    assertEquals("general", result.username());
//    assertTrue(result.userIdList().isEmpty()); // Public channel일 경우
//  }
//
//  @Test
//  void find_shouldIncludeUserList_whenPrivateChannel() {
//    Channel channel = new Channel(UUID.randomUUID(), authorId, null);
//    ReflectionTestUtils.setField(channel, "id", id);
//    channel.setType(ChannelType.PRIVATE);
//    when(channelRepository.find(id)).thenReturn(channel);
//    when(messageRepository.findAllByChannelId(id)).thenReturn(Collections.emptyList());
//    when(readStatusRepository.findAllByChannelId(id)).thenReturn(List.of(
//        new ReadStatus(authorId, id, Instant.now())
//    ));
//
//    ChannelFindDTO result = channelService.find(id);
//
//    assertNotNull(result);
//    assertEquals(1, result.userIdList().size());
//    assertTrue(result.userIdList().contains(authorId));
//  }
//
//  @Test
//  void findAllByUserId_shouldReturnSubscribedAndPublicChannels() {
//    UUID privateChannelId = UUID.randomUUID();
//    UUID publicChannelId = UUID.randomUUID();
//
//    Channel privateChannel = new Channel(UUID.randomUUID(), authorId, null);
//    ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);
//    privateChannel.setType(ChannelType.PRIVATE);
//
//    Channel publicChannel = new Channel(UUID.randomUUID(), authorId, "public");
//    ReflectionTestUtils.setField(publicChannel, "id", publicChannelId);
//
//    when(readStatusRepository.findAllByUserId(authorId)).thenReturn(List.of(
//        new ReadStatus(authorId, privateChannelId, Instant.now())
//    ));
//
//    when(channelRepository.findAll()).thenReturn(List.of(privateChannel, publicChannel));
//    when(channelRepository.find(privateChannelId)).thenReturn(privateChannel);
//    when(channelRepository.find(publicChannelId)).thenReturn(publicChannel);
//    when(messageRepository.findAllByChannelId(any())).thenReturn(Collections.emptyList());
//    when(readStatusRepository.findAllByChannelId(any())).thenReturn(Collections.emptyList());
//
//    List<ChannelFindDTO> results = channelService.findAllByUserId(authorId);
//
//    assertEquals(2, results.size());
//  }
}