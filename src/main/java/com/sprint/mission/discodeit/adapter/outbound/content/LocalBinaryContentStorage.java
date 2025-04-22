package com.sprint.mission.discodeit.adapter.outbound.content;

import com.sprint.mission.discodeit.adapter.inbound.content.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentMetaRepositoryPort;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.exception.CriticalException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStoragePort {

  private final BinaryContentMetaRepositoryPort binaryContentRepository;
  private final Path storagePath;

  /**
   * <h2>로컬 바이너리 데이터 생성자</h2>
   * 프로그램이 시작되자마자 설정파일에서 정해진 위치를 받아온다. <br> 해당 설정파일의 위치를 얻고 또한 메타 데이터의 위치 또한 받아온다. <br> 더불어 저장할 위치의
   * 롤더가 없다면 폴더를 생성한다.
   *
   * @param storagePath
   * @param jpaBinaryContentRepository
   */
  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") String storagePath,
      JpaBinaryContentRepository jpaBinaryContentRepository) {
    this.storagePath = Paths.get(storagePath);
    binaryContentRepository = new BinaryContentMetaRepositoryAdapter(jpaBinaryContentRepository);
    init();
  }

  private void init() {
    if (Files.notExists(storagePath)) {
      try {
        Files.createDirectories(storagePath);
      } catch (IOException e) {
        throw new CriticalException("파일 폴더를 생성할 수 없습니다.");
      }
    }
  }

  /**
   * <h2>파일 위치 생성 메서드</h2>
   * 바이너리 메타 데이터에서 확장자명을 추출한 뒤, id 뒤에 확장자명을 붙여서 반환한다.
   *
   * @param id
   * @return 저장위치/id.확장자명
   */
  private Path resolvePath(UUID id) {
    //확장자명을 추출하기 위해, 바이너리 메타 데이터를 가져온다.
    BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(
        () -> new NotFoundException(ErrorCode.FILE_NOT_FOUND, id)
    );
    //가져온 바이너리 메타 데이터에서 확장자명을 추출한다.
    String extension = binaryContent.getExtension();

    return storagePath.resolve(id.toString() + extension);
  }

  /**
   * <h2>바이너리 데이터 저장</h2>
   * id와 bytes를 통해 파일을 저장한다.
   *
   * @param id
   * @param bytes
   * @return
   */
  @Override
  public UUID put(UUID id, byte[] bytes) {
    //파일 위치를 생성하는 메서드를 통해 id + 확장자의 path를 받는다.
    Path destination = resolvePath(id);
    //해당 위치를 대상으로 Files의 output stream을 연다.
    try (OutputStream outputStream = Files.newOutputStream(destination)) {
      //bytes를 작성한다.
      outputStream.write(bytes);
    } catch (IOException e) {
      throw new RuntimeException();
    }
    return id;
  }

  /**
   * <h2>파일 input stream 열기 메서드</h2>
   * 파일 위치를 바탕으로 inputstream을 연다.
   *
   * @param id
   * @return
   */
  @Override
  public InputStream get(UUID id) {
    //파일 위치를 생성하는 메서드를 통해 id + 확장자의 path를 받는다.
    Path destination = resolvePath(id);
    try {
      return new FileInputStream(destination.toFile());
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <h2>바이너리 데이터 다운로드 메서드</h2>
   * 바이너리 데이터 응답 DTO를 바탕으로 inputstream을 연다. <br> 그 뒤 해당 inputstream을 통해 inputstream resource을 작성하고,
   * header에 파일명, 파일 타입, 파일 사이즈를 입력한 뒤, 응답으로 보낸다.
   *
   * @param binaryContentResponse
   * @return
   */
  @Override
  public ResponseEntity<Resource> download(BinaryContentResponse binaryContentResponse) {
    InputStream inputStream = get(binaryContentResponse.id());

    InputStreamResource resource = new InputStreamResource(inputStream);

    HttpHeaders headers = new HttpHeaders();

    headers.add(HttpHeaders.CONTENT_DISPOSITION,
        "attachments; filename=\"" + binaryContentResponse.fileName() + "\"");
    headers.setContentType(MediaType.parseMediaType(binaryContentResponse.contentType()));
    headers.setContentLength(binaryContentResponse.size());

    return ResponseEntity.ok().headers(headers).body(resource);
  }
}
