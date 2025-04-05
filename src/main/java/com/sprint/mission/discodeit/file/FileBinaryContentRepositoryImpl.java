package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DataStorageException;
import com.sprint.mission.discodeit.service.FileBinaryContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;


@Repository
public class FileBinaryContentRepositoryImpl implements FileBinaryContentRepository { // 인터페이스 구현 가정

  private final Path storagePath;        // 메타데이터 기본 디렉토리 Path
  private final Path imageDirPath;       // 이미지 저장 디렉토리 Path
  private final Path metaDataFilePath;   // 메타데이터 파일 Path

  private final ConcurrentHashMap<UUID, BinaryContent> binaryContentMetadataMap;
  Logger logger = LoggerFactory.getLogger(FileBinaryContentRepositoryImpl.class);

  // 생성자에서 두 개의 경로 설정을 주입받음
  public FileBinaryContentRepositoryImpl(
      @Value("${discodeit.storage.data-dir}") String configuredDataDir,
      @Value("${discodeit.storage.image-dir}") String configuredImageDir) {

    // 각 경로 설정값을 기반으로 Path 객체 초기화
    this.storagePath = Paths.get(configuredDataDir);          // 메타데이터용
    this.imageDirPath = Paths.get(configuredImageDir);        // 이미지 저장용
    String metadataFileName = "binary_content.dat";
    this.metaDataFilePath = this.storagePath.resolve(metadataFileName); // 메타데이터 파일은 storagePath 기준

    System.out.println("메타데이터 기본 디렉토리 사용: " + this.storagePath.toAbsolutePath());
    System.out.println("이미지 저장 디렉토리 사용: " + this.imageDirPath.toAbsolutePath()); // 수정된 이미지 경로 출력
    System.out.println("메타데이터 파일 경로: " + this.metaDataFilePath.toAbsolutePath());

    this.binaryContentMetadataMap = loadMetadata();
  }

  @PostConstruct
  public void initializeDirectories() {
    try {
      Files.createDirectories(storagePath);
      System.out.println("메타데이터 디렉토리 확인/생성: " + storagePath.toAbsolutePath());

      Files.createDirectories(imageDirPath);
      System.out.println("이미지 디렉토리 확인/생성: " + imageDirPath.toAbsolutePath());
    } catch (IOException e) {
      throw new RuntimeException("스토리지 디렉토리 생성 실패", e);
    }
  }

  // loadMetadata, saveMetadata는 metadataFilePath를 사용하므로 변경 불필요
  @SuppressWarnings("unchecked")
  private synchronized ConcurrentHashMap<UUID, BinaryContent> loadMetadata() {
    System.out.println("메타데이터 로드 경로: " + metaDataFilePath.toAbsolutePath());
    if (Files.exists(metaDataFilePath)) {
      try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(metaDataFilePath))) {
        return new ConcurrentHashMap<>((Map<UUID, BinaryContent>) in.readObject());
      } catch (IOException | ClassNotFoundException e) {
        System.err.println("메타데이터 로드 실패: " + e.getMessage());
        return new ConcurrentHashMap<>();
      }
    }
    return new ConcurrentHashMap<>();
  }

  private synchronized void saveMetadata() {
    System.out.println("메타데이터 저장 경로: " + metaDataFilePath.toAbsolutePath());
    try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(metaDataFilePath))) {
      out.writeObject(new HashMap<>(binaryContentMetadataMap));
    } catch (IOException e) {
      throw new DataStorageException("메타 데이터 저장 실패");
    }
  }


  @Override
  public BinaryContent store(InputStream inputStream, String contentType, String originalFileName,
      long size, UUID ownerId, String ownerType) {

    UUID generatedId = UUID.randomUUID();
    String sanitizedFileName = sanitizeFileName(originalFileName);
    String storedFileName = generatedId + "_" + sanitizedFileName;

    Path targetFilePath = imageDirPath.resolve(storedFileName);
    System.out.printf("이미지 파일 저장 시도: %s%n", targetFilePath.toAbsolutePath());

    try {
      Files.copy(inputStream, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
      logger.info("이미지 파일 저장 성공: %s%n");
    } catch (IOException e) {
      throw new DataStorageException("이미지 저장 실패");
    }

    BinaryContent metadata = new BinaryContent(
        contentType,
        originalFileName,
        size,
        ownerId,
        ownerType,
        targetFilePath.toString()
    );

    binaryContentMetadataMap.put(metadata.getId(), metadata);
    saveMetadata();

    logger.info("컨텐츠 메타데이터 저장 완료: ID=%s%n");
    return metadata;
  }

  @Override
  public Optional<BinaryContent> findMetadataById(UUID id) {
    return Optional.ofNullable(binaryContentMetadataMap.get(id));
  }

  @Override
  public Optional<InputStream> getContentStream(UUID id) throws IOException {
    Optional<BinaryContent> metadataOpt = findMetadataById(id);
    if (metadataOpt.isPresent()) {
      // 메타데이터에 저장된 파일 경로 사용
      Path filePath = Paths.get(metadataOpt.get().getFilePath());
      if (Files.exists(filePath)) {
        return Optional.of(Files.newInputStream(filePath));
      } else {
        logger.debug("메타데이터는 있으나 파일 없음: Path=%s%n");
        return Optional.empty();
      }
    }
    logger.debug("스트림 요청: 메타데이터 없음: ID=%s%n");
    return Optional.empty();
  }

  @Override
  public boolean deleteById(UUID id) {
    BinaryContent removedMetadata = binaryContentMetadataMap.remove(id);
    if (removedMetadata != null) {
      System.out.printf("메타데이터 삭제됨: ID=%s%n", id);
      // 메타데이터에 저장된 파일 경로 사용
      Path filePath = Paths.get(removedMetadata.getFilePath());
      try {
        boolean fileDeleted = Files.deleteIfExists(filePath);
        if (!fileDeleted) {
          logger.debug("이미지 파일 삭제 실패 또는 파일 없음: Path=%s%n");
        } else {
          logger.debug("이미지 파일 삭제 성공: Path=%s%n");
        }
      } catch (IOException e) {
        logger.warn("이미지 파일 삭제 중 오류 발생: Path=%s, Error=%s%n");
      }
      saveMetadata(); // 변경된 메타데이터 저장
      return true;
    }
    System.out.printf("삭제 대상 메타데이터 없음: ID=%s%n", id);
    return false;
  }


  @Override
  public List<BinaryContent> findAllMetadataByOwnerId(UUID ownerId) {
    return binaryContentMetadataMap.values().stream()
        .filter(content -> content.getOwnerId() != null && content.getOwnerId().equals(ownerId))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<BinaryContent> findMetadataByOwnerId(UUID ownerId) {
    return binaryContentMetadataMap.values().stream()
        .filter(content -> content.getOwnerId() != null && content.getOwnerId().equals(ownerId))
        .findFirst();
  }

  @Override
  public List<UUID> findAllIds() {
    return new ArrayList<>(binaryContentMetadataMap.keySet());
  }


  @PreDestroy
  public void saveDataOnShutdown() {
    logger.info("애플리케이션 종료 감지 - 바이너리 메타데이터 저장 실행");
    saveMetadata();
  }

  private String sanitizeFileName(String fileName) {
    if (fileName == null) {
      return "unknown_file";
    }
    return fileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
  }
}
