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
//  private UUID userId;
//
//  @BeforeEach
//  void setup() {
//    id = UUID.randomUUID();
//    userId = UUID.randomUUID();
//  }
//
//  @Test
//  void find_shouldReturnChannelFindDTO_whenPublicChannel() {
//    Channel channel = new Channel(UUID.randomUUID(), userId, "general");
//    ReflectionTestUtils.setField(channel, "id", id);
//    when(channelRepository.find(id)).thenReturn(channel);
//    when(messageRepository.findAllByChannelId(id)).thenReturn(List.of(
//        new Message(userId, "general", id, "hi"),
//        new Message(userId, "general", id, "hello")
//    ));
//
//    ChannelFindDTO result = channelService.find(id);
//
//    assertNotNull(result);
//    assertEquals(id, result.id());
//    assertEquals("general", result.name());
//    assertTrue(result.userIdList().isEmpty()); // Public channel일 경우
//  }
//
//  @Test
//  void find_shouldIncludeUserList_whenPrivateChannel() {
//    Channel channel = new Channel(UUID.randomUUID(), userId, null);
//    ReflectionTestUtils.setField(channel, "id", id);
//    channel.setType(ChannelType.PRIVATE);
//    when(channelRepository.find(id)).thenReturn(channel);
//    when(messageRepository.findAllByChannelId(id)).thenReturn(Collections.emptyList());
//    when(readStatusRepository.findAllByChannelId(id)).thenReturn(List.of(
//        new ReadStatus(userId, id, Instant.now())
//    ));
//
//    ChannelFindDTO result = channelService.find(id);
//
//    assertNotNull(result);
//    assertEquals(1, result.userIdList().size());
//    assertTrue(result.userIdList().contains(userId));
//  }
//
//  @Test
//  void findAllByUserId_shouldReturnSubscribedAndPublicChannels() {
//    UUID privateChannelId = UUID.randomUUID();
//    UUID publicChannelId = UUID.randomUUID();
//
//    Channel privateChannel = new Channel(UUID.randomUUID(), userId, null);
//    ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);
//    privateChannel.setType(ChannelType.PRIVATE);
//
//    Channel publicChannel = new Channel(UUID.randomUUID(), userId, "public");
//    ReflectionTestUtils.setField(publicChannel, "id", publicChannelId);
//
//    when(readStatusRepository.findAllByUserId(userId)).thenReturn(List.of(
//        new ReadStatus(userId, privateChannelId, Instant.now())
//    ));
//
//    when(channelRepository.findAll()).thenReturn(List.of(privateChannel, publicChannel));
//    when(channelRepository.find(privateChannelId)).thenReturn(privateChannel);
//    when(channelRepository.find(publicChannelId)).thenReturn(publicChannel);
//    when(messageRepository.findAllByChannelId(any())).thenReturn(Collections.emptyList());
//    when(readStatusRepository.findAllByChannelId(any())).thenReturn(Collections.emptyList());
//
//    List<ChannelFindDTO> results = channelService.findAllByUserId(userId);
//
//    assertEquals(2, results.size());
//  }
}