package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public BinaryContentDto create(BinaryContentCreateRequest request) {
    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    log.info("파일 생성 요청: name={}, size={} bytes, contentType={}", fileName, bytes.length, contentType);

    BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(fileName, (long) bytes.length, contentType));
    UUID uuid = binaryContentStorage.put(binaryContent.getId(), bytes);
    log.info("파일 저장 완료: id={}, uuid={}", binaryContent.getId(), uuid);

    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    log.info("파일 조회 요청: id={}", binaryContentId);
    return binaryContentRepository.findById(binaryContentId)
            .map(dto -> {
              log.info("파일 조회 성공: id={}", binaryContentId);
              return binaryContentMapper.toDto(dto);
            })
            .orElseThrow(() -> {
              log.warn("파일 조회 실패: id={} not found", binaryContentId);
              return new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
            });
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    log.info("여러 파일 조회 요청: ids={}", binaryContentIds);
    List<BinaryContentDto> dtos = binaryContentRepository
            .findAllByIdIn(binaryContentIds)
            .stream()
            .map(binaryContentMapper::toDto)
            .toList();
    log.info("여러 파일 조회 완료: count={}", dtos.size());
    return dtos;
  }

  @Override
  public void delete(UUID binaryContentId) {
    log.info("파일 삭제 요청: id={}", binaryContentId);
    if (!binaryContentRepository.existsById(binaryContentId)) {
      log.warn("파일 삭제 실패: id={} not found", binaryContentId);
      throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
    }
    binaryContentRepository.deleteById(binaryContentId);
    log.info("파일 삭제 완료: id={}", binaryContentId);
  }
}
