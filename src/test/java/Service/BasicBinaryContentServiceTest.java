//package Service;
//
//import com.sprint.mission.discodeit.dto.service.binaryContent.BinaryContentDTO;
//import com.sprint.mission.discodeit.dto.service.binaryContent.CreateBinaryContentParam;
//import com.sprint.mission.discodeit.entity.BinaryContent;
//import com.sprint.mission.discodeit.exception.RestException;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
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
//public class BasicBinaryContentServiceTest {
//
//    @Mock
//    BinaryContentRepository binaryContentRepository;
//
//    @InjectMocks
//    BasicBinaryContentService binaryContentService;
//
//    BinaryContent mockBinaryContent = BinaryContent.builder()
//            .contentType("jpeg")
//            .size(500)
//            .filename("testImage")
//            .build();
//
//
//    @Test
//    void 파일생성_성공() {
//
//        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(mockBinaryContent);
//
//        BinaryContent binaryContent = binaryContentService.create(mockBinaryContent);
//
//        assertEquals(binaryContent.getSize(), mockBinaryContent.getSize());
//        assertEquals(binaryContent.getContentType(), mockBinaryContent.getContentType());
//        assertEquals(binaryContent.getFilename(), mockBinaryContent.getFilename());
//
//        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
//    }
//
//    @Test
//    void 파일조회_성공() {
//        when(binaryContentRepository.findById(mockBinaryContent.getId())).thenReturn(Optional.ofNullable(mockBinaryContent));
//
//        BinaryContentDTO binaryContentDTO = binaryContentService.find(mockBinaryContent.getId());
//
//        assertEquals(binaryContentDTO.filename(), mockBinaryContent.getFilename());
//        assertEquals(binaryContentDTO.ContentType(), mockBinaryContent.getContentType());
//        assertEquals(binaryContentDTO.size(), mockBinaryContent.getSize());
//
//        verify(binaryContentRepository, times(1)).findById(mockBinaryContent.getId());
//    }
//
//    @Test
//    void 파일조회_파일없음_실패() {
//        when(binaryContentRepository.findById(mockBinaryContent.getId())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> binaryContentService.find(mockBinaryContent.getId()))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("BinaryContent not found");
//
//        verify(binaryContentRepository, times(1)).findById(mockBinaryContent.getId());
//
//    }
//
//    @Test
//    void 파일전체조회_성공() {
//        byte[] bytes = {(byte)0xAB, (byte)0xCD};
//
//        List<BinaryContent> mockBinaryContentList = List.of(
//                new BinaryContent("testImage2", 400, "jpeg", bytes),
//                new BinaryContent("testImage3", 600, "jpeg", bytes),
//                new BinaryContent("testImage4", 800, "jpeg", bytes)
//        );
//
//        when(binaryContentRepository.findById(mockBinaryContentList.get(0).getId())).thenReturn(Optional.of(mockBinaryContentList.get(0)));
//        when(binaryContentRepository.findById(mockBinaryContentList.get(1).getId())).thenReturn(Optional.of(mockBinaryContentList.get(1)));
//
//        List<UUID> attachmentsId = List.of(mockBinaryContentList.get(0).getId(), mockBinaryContentList.get(1).getId());
//
//        List<BinaryContentDTO> binaryContentDTOList = binaryContentService.findAllByIdIn(attachmentsId);
//
//        assertEquals(attachmentsId.size(), binaryContentDTOList.size());
//        assertEquals(binaryContentDTOList.get(0).id(), attachmentsId.get(0));
//        assertEquals(binaryContentDTOList.get(1).id(), attachmentsId.get(1));
//
//        verify(binaryContentRepository, times(2)).findById(any(UUID.class));
//    }
//
//    @Test
//    void 파일전체조회_파일없음_실패() {
//        List<UUID> attachmentsId = List.of(UUID.randomUUID(), UUID.randomUUID());
//
//        when(binaryContentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> binaryContentService.findAllByIdIn(attachmentsId))
//                .isInstanceOf(RestException.class)
//                .hasMessageContaining("BinaryContent not found");
//
//        verify(binaryContentRepository, times(1)).findById(any(UUID.class));
//    }
//
//    @Test
//    void 파일삭제_성공() {
//        UUID id = mockBinaryContent.getId();
//
//        binaryContentService.delete(id);
//
//        verify(binaryContentRepository, times(1)).deleteById(id);
//    }
//}
