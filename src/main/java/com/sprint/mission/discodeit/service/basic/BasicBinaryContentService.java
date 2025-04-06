package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent save(MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new NoSuchElementException("File is empty");
    }

    String fileName = file.getOriginalFilename();
    String contentType = file.getContentType();
    byte[] bytes = file.getBytes();

    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(fileName)
        .contentType(contentType)
        .bytes(bytes)
        .size((long) bytes.length)
        .build();
    binaryContentRepository.save(binaryContent);
    return binaryContent;
  }

  @Override
  public BinaryContent findById(UUID binaryContentUUID) {
    return binaryContentRepository.findById(binaryContentUUID)
        .orElseThrow(
            () -> new NoSuchElementException(binaryContentUUID + "에 해당하는 파일을 찾는데 실패하였습니다."));
  }

  @Override
  public List<BinaryContent> findByIdIn(List<UUID> binaryContentUUIDList) {
    return binaryContentRepository.findAll().stream()
        .filter(binaryContent -> binaryContentUUIDList.contains(binaryContent.getId()))
        .map(binaryContent -> findById(binaryContent.getId()))
        .toList();
  }

  @Override
  public void delete(UUID userStatusUUID) {
    binaryContentRepository.delete(userStatusUUID);
  }
}
