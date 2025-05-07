package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local") // 설정값에 따라 조건부 빈 등록
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root; // 로컬 스토리지 루트 경로

  // 생성자를 통해 설정값을 주입받습니다.
  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @PostConstruct // 빈 생성 및 의존성 주입 완료 후 자동 호출
  public void init() {
    try {
      Files.createDirectories(root);
      System.out.println("Local storage root directory initialized at: " + root.toAbsolutePath());
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize local storage root directory!", e);
    }
  }

  /**
   * 주어진 UUID에 해당하는 파일의 로컬 경로를 계산합니다. 파일 저장 규칙: {root}/{UUID}
   *
   * @param id BinaryContent의 UUID.
   * @return 파일의 절대 경로 Path 객체.
   */
  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  /**
   * 주어진 UUID와 연결된 바이너리 데이터를 로컬 디스크에 저장합니다. BinaryContentStorage 인터페이스의 put 메소드 구현입니다.
   *
   * @param id   BinaryContent의 ID로 사용될 UUID.
   * @param data 저장할 바이너리 데이터 (byte 배열).
   * @return 데이터가 저장된 UUID (입력된 id와 동일).
   * @throws IOException 저장 중 I/O 오류 발생 시.
   */
  @Override
  public UUID put(UUID id, byte[] data) throws IOException {
    Path filePath = resolvePath(id);
    try {
      // 부모 디렉토리가 필요하다면 여기에 생성 로직 추가
      // Files.createDirectories(filePath.getParent());
      Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      return id;
    } catch (IOException e) {
      // 로깅 고려
      throw new IOException("Failed to save data to local storage for id: " + id, e);
    }
  }

  /**
   * 주어진 UUID에 해당하는 바이너리 데이터를 InputStream 형태로 로컬 디스크에서 조회합니다. BinaryContentStorage 인터페이스의 get 메소드
   * 구현입니다.
   *
   * @param id 조회할 데이터의 UUID.
   * @return 바이너리 데이터의 InputStream. 해당 ID의 파일이 없을 경우 IOException 발생.
   * @throws IOException 조회 중 I/O 오류 또는 파일이 없을 경우 발생 시.
   */
  @Override
  public InputStream get(UUID id) throws IOException {
    Path filePath = resolvePath(id);
    try {
      if (!Files.exists(filePath)) {
        // 파일이 없을 경우 IOException 발생
        throw new IOException("File not found in local storage for id: " + id);
      }
      return Files.newInputStream(filePath, StandardOpenOption.READ);
    } catch (IOException e) {
      // 로깅 고려
      throw new IOException("Failed to get data from local storage for id: " + id, e);
    }
  }

  /**
   * 주어진 UUID에 해당하는 바이너리 데이터를 로컬 디스크에서 삭제합니다. BinaryContentStorage 인터페이스의 delete 메소드 구현입니다.
   *
   * @param id 삭제할 데이터의 UUID.
   * @throws IOException 삭제 중 I/O 오류 발생 시.
   */
  @Override
  public void delete(UUID id) throws IOException {
    Path filePath = resolvePath(id);
    try {
      // 파일이 없어도 예외를 발생시키지 않음
      Files.deleteIfExists(filePath);
      // 로깅 고려: deleted 변수를 받아 파일 삭제 여부 확인 가능
    } catch (IOException e) {
      // 로깅 고려
      throw new IOException("Failed to delete data from local storage for id: " + id, e);
    }
  }

  /**
   * BinaryContentDto와 바이너리 데이터를 활용해 ResponseEntity<Resource> 응답을 생성 후 반환합니다. 이 메소드 내에서 get 메소드를 통해
   * 파일의 바이너리 데이터를 조회합니다.
   * <p>
   * (참고: 이 메소드는 BinaryContentStorage 인터페이스에 정의되지 않았으며, 스토리지 컴포넌트의 일반적인 책임 범위를 벗어납니다. HTTP 응답 생성 로직은
   * 컨트롤러 레이어에 위치하는 것이 아키텍처적으로 바람직합니다.)
   *
   * @param dto 다운로드에 필요한 메타 정보(ID, 파일명, 컨텐츠 타입 등)를 담은 DTO.
   * @return 파일 데이터를 담은 ResponseEntity<Resource> 객체.
   */
  // 이 메소드는 BinaryContentStorage 인터페이스에 정의되지 않았습니다.
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    // 요구사항: get 메소드를 통해 바이너리 데이터를 조회합니다.
    // 요구사항: BinaryContentDto와 바이너리 데이터를 활용해 ResponseEntity<Resource> 응답을 생성 후 반환합니다.

    if (dto == null || dto.id() == null) {
      // DTO나 ID가 유효하지 않으면 Bad Request 반환
      return ResponseEntity.badRequest().body(null);
    }

    UUID id = dto.id();
    // BinaryContentDto에서 파일명과 컨텐츠 타입을 가져옵니다.
    // 서비스 레이어에서 DTO에 이 정보를 채워줬다고 가정합니다.
    String filename = dto.fileName() != null ? dto.fileName() : id.toString(); // DTO의 파일명 사용
    String contentType = dto.contentType() != null ? dto.contentType()
        : "application/octet-stream"; // DTO의 컨텐츠 타입 사용

    try {
      // 이 스토리지 구현체의 get(id) 메소드를 호출하여 InputStream을 가져옵니다.
      InputStream inputStream = this.get(id); // <<< get 메소드 호출

      // InputStream을 Spring Resource로 래핑합니다.
      Resource resource = new InputStreamResource(inputStream);

      // HTTP 헤더를 설정합니다. (Content-Disposition, Content-Type)
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
      // headers.add(HttpHeaders.CONTENT_TYPE, contentType); // 아래 contentType()으로 설정 가능

      // ResponseEntity를 구성하고 반환합니다.
      return ResponseEntity.ok()
          .headers(headers) // 설정한 헤더 추가
          .contentType(MediaType.parseMediaType(contentType)) // 컨텐츠 타입 설정
          .body(resource); // Resource (파일 데이터)를 응답 본문에 설정

    } catch (IOException e) {
      // get 메소드에서 파일이 없거나 읽기 오류 발생 시 처리
      // logger.error("Failed to generate download response for id: " + id, e);
      // 파일이 없는 경우는 404, 읽기 오류는 500으로 구분할 수도 있습니다.
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(null); // 파일 없음 또는 기타 I/O 오류 시 404/500 응답
    } catch (Exception e) {
      // 그 외 예상치 못한 오류 처리
      // logger.error("Unexpected error during download response generation for id: " + id, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error 응답
    }
  }
}
