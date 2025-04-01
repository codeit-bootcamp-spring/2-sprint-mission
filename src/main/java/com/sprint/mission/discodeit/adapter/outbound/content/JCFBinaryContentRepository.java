package com.sprint.mission.discodeit.adapter.outbound.content;

import com.sprint.mission.discodeit.exception.content.EmptyBinaryContentListException;
import com.sprint.mission.discodeit.exception.content.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

  private final List<BinaryContent> binaryContentList = new LinkedList<>();

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    binaryContentList.add(binaryContent);
    return binaryContent;
  }

  @Override
  public BinaryContent findById(UUID binaryId) {
    return CommonUtils.findById(binaryContentList, binaryId, BinaryContent::getId)
        .orElseThrow(() -> new BinaryContentNotFoundException("바이너리 데이터를 찾을 수 없습니다."));
  }

  @Override
  public List<BinaryContent> findAllByIdIn() {
    if (binaryContentList.isEmpty()) {
      throw new EmptyBinaryContentListException("Repository 내 바이너리 정보 리스트가 비어있습니다.");
    }
    return binaryContentList;
  }

  @Override
  public void delete(UUID binaryId) {
    try {
      BinaryContent content = findById(binaryId);
      binaryContentList.remove(content);
    } catch (BinaryContentNotFoundException e) {
      System.out.println("해당 바이너리 데이터는 존재하지 않습니다.");
    }
  }
}
