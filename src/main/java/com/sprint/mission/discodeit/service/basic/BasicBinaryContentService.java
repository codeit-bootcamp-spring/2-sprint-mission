package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDownload;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public BinaryContentDto create(BinaryContentCreateRequest request) {

    BinaryContent binaryContent = binaryContentMapper.toEntity(request);

    BinaryContent savedBinaryContent = binaryContentRepository.save(
        binaryContent);

    MultipartFile file = request.file();

    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File data is required for BinaryContent creation.");
    }

    try {

      byte[] fileBytes = file.getBytes();
      UUID storedId = binaryContentStorage.put(savedBinaryContent.getId(),
          fileBytes);
    } catch (IOException e) {
      throw new RuntimeException("Failed to store binary content data", e);
    }
    return binaryContentMapper.toDto(savedBinaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public BinaryContentDto findById(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new IllegalArgumentException(
            "[Error] binaryContent metadata not found with id: " + binaryContentId));

    return binaryContentMapper.toDto(binaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public List<BinaryContentDto> findAllByIds(List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentRepository.findAllById(binaryContentIds);

    if (binaryContents.isEmpty() && !binaryContentIds.isEmpty()) {
      throw new IllegalArgumentException(
          "[Error] No binaryContent metadata found for the given IDs.");
    } else if (binaryContents.isEmpty()) {
      return List.of();
    }

    return binaryContents.stream()
        .map(binaryContentMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void delete(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new IllegalArgumentException(
            "[Error] binaryContent metadata not found with id: " + binaryContentId));

    try {
      binaryContentStorage.delete(binaryContentId);
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete binary content data from storage", e);
    }

    binaryContentRepository.delete(binaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public BinaryContentDownload download(UUID binaryContentId) throws IOException {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new IllegalArgumentException(
            "[Error] binaryContent metadata not found with id: " + binaryContentId));

    InputStream dataStream = binaryContentStorage.get(binaryContentId);

    if (dataStream == null) {
      throw new IOException("Binary data not found in storage for id: " + binaryContentId);
    }

    String filename = binaryContent.getFileName();
    String contentType = binaryContent.getContentType();

    if (filename == null || filename.trim().isEmpty()) {
      filename = binaryContentId.toString();
    }
    if (contentType == null || contentType.trim().isEmpty()) {
      contentType = "application/octet-stream";
    }

    return new BinaryContentDownload(dataStream, filename, contentType);
  }
  
}
