package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.adapter.outbound.content.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentMetaRepositoryPort;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentMetaRepositoryPort binaryContentMetaRepository;
  private final LocalBinaryContentStorage binaryContentStorage;

  /**
   * <h2>이미지 생성 메서드</h2>
   * 유저 생성, 유저 업데이트 메서드가 사용될 때 작동함 <br> 이미지 DTO 내 값이 존재하면 작동하는 메서드 <br> DTO 내 변수를 풀어해친 뒤, 메타 데이터는
   * DB에다가, bytes는 로컬에다가 저장 <br>
   *
   * @param command 파일 이름, 파일 타입, 바이트
   * @return 메타 데이터 : 파일명, 파일 사이즈, 파일 타입, 확장자명
   */
  @Override
  @Transactional
  public BinaryContent create(CreateBinaryContentCommand command) {
    if (command != null) {
      //바이너키 컨텐트 메타 데이터를 생성
      //파일명, 파일 사이즈, 파일 타입
      BinaryContent binaryContent = BinaryContent.create(command.fileName(),
          (long) command.bytes().length, command.contentType());
      //메타 데이터를 DB에 저장시킴
      binaryContentMetaRepository.save(binaryContent);
      //byte를 로컬 storage에 저장시킴
      binaryContentStorage.put(binaryContent.getId(), command.bytes());
      log.info("[BinaryContentService] Binary Content Created: {}", binaryContent.getId());
      return binaryContent;
    }
    log.warn("[BinaryContentService] Parameter is empty");
    return null;
  }

  /**
   * <h2>바이너리 메타 데이터 검색(id)</h2>
   * 아이디를 통해 이미지 메타 데이터를 검색한다.
   *
   * @param binaryId 찾고자 할 이미지의 아이디
   * @return 메타 데이터 : 파일명, 파일 사이즈, 파일 타입, 확장자명
   */
  @Override
  @Transactional(readOnly = true)
  public BinaryContent findById(UUID binaryId) {
    return binaryContentMetaRepository.findById(binaryId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.FILE_NOT_FOUND, binaryId)
    );
  }

  /**
   * <h2>바이너리 메타 데이터 전체 검색(id)</h2>
   * id 리스트를 통해 전체 메타 데이터를 검색한다.
   *
   * @param binaryContentIds 찾고자 할 이미지의 아이디 목록
   * @return List [ 메타 데이터 : 파일명, 파일 사이즈, 파일 타입, 확장자명 ]
   */
  @Override
  @Transactional(readOnly = true)
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentMetaRepository.findAllByIdIn(binaryContentIds);
  }

  /**
   * <h2>바이너리 메타 데이터 삭제 메서드</h2>
   * 아이디를 통해 메타 데이터를 삭제한다.
   *
   * @param binaryId 삭제하고자 할 메타 데이터 아이디
   */
  @Override
  @Transactional
  public void delete(UUID binaryId) {
    if (!binaryContentMetaRepository.existsId(binaryId)) {
      log.warn("[BinaryContentService] To Delete Binary Content is failed");
      throw new UserNotFoundException(ErrorCode.FILE_NOT_FOUND, binaryId);
    }
    binaryContentMetaRepository.delete(binaryId);
    log.info("[BinaryContentService] Binary Content deleted successfully");
  }
}
