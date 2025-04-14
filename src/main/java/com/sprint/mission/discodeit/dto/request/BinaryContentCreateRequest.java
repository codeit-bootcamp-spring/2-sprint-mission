package com.sprint.mission.discodeit.dto.request;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public static List<BinaryContentCreateRequest> of(List<MultipartFile> attachmentFiles) {
    return Optional.ofNullable(attachmentFiles)
        .orElse(List.of())
        .stream()
        .map(file -> {
          try {
            return new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
          } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 처리 중 오류 발생", e);
          }
        })
        .collect(Collectors.toList());
  }
}
