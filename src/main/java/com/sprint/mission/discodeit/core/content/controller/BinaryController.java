package com.sprint.mission.discodeit.core.content.controller;

import static com.sprint.mission.discodeit.core.content.controller.BinaryContentDtoMapper.toCreateResponse;

import com.sprint.mission.discodeit.core.content.controller.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Binary Content", description = "바이너리 데이터 관련 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryController {

  private final BinaryContentStoragePort binaryContentStorage;
  private final BinaryContentService binaryContentService;

  /**
   * <h2>바이너리 데이터 전체 검색 </h2>
   * 바이너리 데이터를 전체적으로 검색한다.
   *
   * @param binaryContentIds 찾고자 할 바이너리 데이터 아이디 목록
   * @return List [ 아이디, 파일명, 파일 사이즈, 파일 타입 ]
   */
  @GetMapping
  public ResponseEntity<List<BinaryContentResponse>> findAllBinaryContent(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> allByIdIn = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(allByIdIn.stream()
        .map(BinaryContentDtoMapper::toCreateResponse).toList());
  }

  /**
   * <h2>바이너리 데이터 검색</h2>
   * 바이너리 데이터를 검색한다.
   *
   * @param binaryContentId 찾고자 하는 바이너리 데이터 아이디
   * @return 메타 데이터 : 파일명, 파일 사이즈, 파일 타입, 확장자명
   */
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentResponse> findBinaryContent(
      @PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(toCreateResponse(binaryContent));
  }

  /**
   * <h2>바이너리 데이터 다운로드 메서드</h2>
   * storage에 저장된 바이너리 데이터를 아이디를 통해 접근해서 다운한다.
   *
   * @param binaryContentId 다운로드하고자 할 바이너리 데이터 아이디
   * @return
   */
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    BinaryContentResponse response = toCreateResponse(binaryContent);
    return binaryContentStorage.download(response);
  }
}
