package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.FileBinaryContentRepository;
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


    private final String dataDir;
    private final String imageDir;

    private final String metadataFileName = "binary_content.dat";

    // 경로 Path 객체들
    private final Path storagePath;        // 메타데이터 기본 디렉토리 Path
    private final Path imageDirPath;       // 이미지 저장 디렉토리 Path
    private final Path metadataFilePath;   // 메타데이터 파일 Path

    private final Map<UUID, BinaryContent> binaryContentMetadataMap;

    // 생성자에서 두 개의 경로 설정을 주입받음
    public FileBinaryContentRepositoryImpl(
            @Value("${discodeit.storage.data-dir}") String configuredDataDir,
            @Value("${discodeit.storage.image-dir}") String configuredImageDir) {

        this.dataDir = configuredDataDir;
        this.imageDir = configuredImageDir;

        // 각 경로 설정값을 기반으로 Path 객체 초기화
        this.storagePath = Paths.get(this.dataDir);          // 메타데이터용
        this.imageDirPath = Paths.get(this.imageDir);        // 이미지 저장용
        this.metadataFilePath = this.storagePath.resolve(metadataFileName); // 메타데이터 파일은 storagePath 기준

        System.out.println("메타데이터 기본 디렉토리 사용: " + this.storagePath.toAbsolutePath());
        System.out.println("이미지 저장 디렉토리 사용: " + this.imageDirPath.toAbsolutePath()); // 수정된 이미지 경로 출력
        System.out.println("메타데이터 파일 경로: " + this.metadataFilePath.toAbsolutePath());

        this.binaryContentMetadataMap = loadMetadata();
    }

    @PostConstruct
    public void initializeDirectories() {
        try {
            // 메타데이터 저장용 디렉토리 생성 (기존 storagePath 사용)
            Files.createDirectories(storagePath);
            System.out.println("메타데이터 디렉토리 확인/생성: " + storagePath.toAbsolutePath());

            // 이미지 저장용 특정 디렉토리 생성 (새로운 imageDirPath 사용)
            Files.createDirectories(imageDirPath);
            System.out.println("이미지 디렉토리 확인/생성: " + imageDirPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("디렉토리 생성 실패: " + e.getMessage());
            throw new RuntimeException("스토리지 디렉토리 생성 실패", e);
        }
    }

    // loadMetadata, saveMetadata는 metadataFilePath를 사용하므로 변경 불필요
    @SuppressWarnings("unchecked")
    private synchronized Map<UUID, BinaryContent> loadMetadata() {
        System.out.println("메타데이터 로드 경로: " + metadataFilePath.toAbsolutePath());
        if (Files.exists(metadataFilePath)) {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(metadataFilePath))) {
                return new ConcurrentHashMap<>((Map<UUID, BinaryContent>) in.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("메타데이터 로드 실패: " + e.getMessage());
                return new ConcurrentHashMap<>();
            }
        }
        return new ConcurrentHashMap<>();
    }

    private synchronized void saveMetadata() {
        System.out.println("메타데이터 저장 경로: " + metadataFilePath.toAbsolutePath());
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(metadataFilePath))) {
            out.writeObject(new HashMap<>(binaryContentMetadataMap));
        } catch (IOException e) {
            System.err.println("메타데이터 저장 실패: " + e.getMessage());
        }
    }


    @Override
    public BinaryContent store(InputStream inputStream, String contentType, String originalFileName,
                               long size, UUID ownerId, String ownerType) throws IOException {

        UUID generatedId = UUID.randomUUID();
        String sanitizedFileName = sanitizeFileName(originalFileName);
        String storedFileName = generatedId.toString() + "_" + sanitizedFileName;

        // ★★★ 이미지 저장 경로를 imageDirPath 기준으로 변경 ★★★
        Path targetFilePath = imageDirPath.resolve(storedFileName);
        System.out.printf("이미지 파일 저장 시도: %s%n", targetFilePath.toAbsolutePath());

        try {
            Files.copy(inputStream, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.printf("이미지 파일 저장 성공: %s%n", targetFilePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.printf("이미지 파일 저장 실패: %s, Error: %s%n", targetFilePath.toAbsolutePath(), e.getMessage());
            throw e;
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
        saveMetadata(); // 메타데이터는 metadataFilePath에 저장됨

        System.out.printf("컨텐츠 메타데이터 저장 완료: ID=%s%n", metadata.getId());
        return metadata;
    }

    // findMetadataById 는 변경 불필요 (메모리 맵 사용)
    @Override
    public Optional<BinaryContent> findMetadataById(UUID id) {
        return Optional.ofNullable(binaryContentMetadataMap.get(id));
    }

    // getContentStream 은 변경 불필요 (메타데이터의 filePath 사용)
    @Override
    public Optional<InputStream> getContentStream(UUID id) throws IOException {
        Optional<BinaryContent> metadataOpt = findMetadataById(id);
        if (metadataOpt.isPresent()) {
            // 메타데이터에 저장된 파일 경로 사용
            Path filePath = Paths.get(metadataOpt.get().getFilePath());
            if (Files.exists(filePath)) {
                return Optional.of(Files.newInputStream(filePath));
            } else {
                System.err.printf("메타데이터는 있으나 파일 없음: Path=%s%n", filePath);
                return Optional.empty();
            }
        }
        System.out.printf("스트림 요청: 메타데이터 없음: ID=%s%n", id);
        return Optional.empty();
    }

    // deleteById 는 변경 불필요 (메타데이터의 filePath 사용)
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
                    System.err.printf("이미지 파일 삭제 실패 또는 파일 없음: Path=%s%n", filePath);
                } else {
                    System.out.printf("이미지 파일 삭제 성공: Path=%s%n", filePath);
                }
            } catch (IOException e) {
                System.err.printf("이미지 파일 삭제 중 오류 발생: Path=%s, Error=%s%n", filePath, e.getMessage());
            }
            saveMetadata(); // 변경된 메타데이터 저장
            return true;
        }
        System.out.printf("삭제 대상 메타데이터 없음: ID=%s%n", id);
        return false;
    }

    // findAllMetadataByOwnerId, findMetadataByOwnerId, findAllIds 는 변경 불필요 (메모리 맵 사용)
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
        System.out.println("애플리케이션 종료 감지 - 바이너리 메타데이터 저장 실행");
        saveMetadata();
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) return "unknown_file";
        return fileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
    }
}
