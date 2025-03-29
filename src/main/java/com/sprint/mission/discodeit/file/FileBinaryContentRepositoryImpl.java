package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.FileBinaryContentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Repository("fileBinaryContentRepositoryImpl")
public class FileBinaryContentRepositoryImpl implements FileBinaryContentRepository {

    private final String dataDir;
    private final String metadataFileName = "binary_content.dat"; // 메타데이터 파일 이름
    private final Path storagePath;
    private final Path imageDirPath;
    private final Path metadataFilePath;

    private final Map<UUID, BinaryContent> binaryContentMetadataMap;


    public FileBinaryContentRepositoryImpl() {
        this("./data");
    }

    // 데이터 디렉토리 지정 생성자
    public FileBinaryContentRepositoryImpl(String dataDir) {
        this.dataDir = dataDir;
        this.storagePath = Paths.get(this.dataDir);
        this.imageDirPath = this.storagePath.resolve("images"); // dataDir 아래 images
        this.metadataFilePath = this.storagePath.resolve(metadataFileName);
        System.out.println("데이터 디렉토리 사용: " + this.storagePath.toAbsolutePath());
        System.out.println("이미지 저장 디렉토리: " + this.imageDirPath.toAbsolutePath());
        this.binaryContentMetadataMap = loadMetadata();
    }

    @PostConstruct
    public void initializeDirectories() {
        try {
            Files.createDirectories(storagePath); // 데이터 디렉토리 생성
            Files.createDirectories(imageDirPath); // 이미지 하위 디렉토리 생성
            System.out.println("데이터 디렉토리 확인/생성: " + storagePath.toAbsolutePath());
            System.out.println("이미지 디렉토리 확인/생성: " + imageDirPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("디렉토리 생성 실패: " + e.getMessage());
            throw new RuntimeException("스토리지 디렉토리 생성 실패", e);
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized Map<UUID, BinaryContent> loadMetadata() {
        System.out.println("메타데이터 로드 경로: " + metadataFilePath.toAbsolutePath());
        if (Files.exists(metadataFilePath)) {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(metadataFilePath))) {
                return new ConcurrentHashMap<>((Map<UUID, BinaryContent>) in.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("메타데이터 로드 실패", e);
            }
        }
        return new ConcurrentHashMap<>();
    }


    private synchronized void saveMetadata() {
        System.out.println("메타데이터 저장 경로: " + metadataFilePath.toAbsolutePath());
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(metadataFilePath))) {
            out.writeObject(new HashMap<>(binaryContentMetadataMap));
        } catch (IOException e) {
            throw new RuntimeException("메타데이터 저장 실패", e);
        }
    }


    @Override
    public BinaryContent store(InputStream inputStream, String contentType, String originalFileName,
                               long size, UUID ownerId, String ownerType) throws IOException {

        UUID generatedId = UUID.randomUUID();
        String sanitizedFileName = sanitizeFileName(originalFileName);
        String storedFileName = generatedId.toString() + "_" + sanitizedFileName;

        Path targetFilePath = imageDirPath.resolve(storedFileName);
        System.out.printf("이미지 파일 저장 시도: %s%n", targetFilePath.toAbsolutePath());

        try {

            Files.copy(inputStream, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.printf("이미지 파일 저장 성공: %s%n", targetFilePath.toAbsolutePath());
        } catch (IOException e) {
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
        saveMetadata();

        System.out.printf("컨텐츠 메타데이터 저장 완료: ID=%s%n", metadata.getId());
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

            Path filePath = Paths.get(metadataOpt.get().getFilePath());
            if (Files.exists(filePath)) {

                return Optional.of(Files.newInputStream(filePath));
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(UUID id) {
        BinaryContent removedMetadata = binaryContentMetadataMap.remove(id);

        if (removedMetadata != null) {
            System.out.printf("메타데이터 삭제됨: ID=%s%n", id);
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
            saveMetadata();
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
        System.out.println("애플리케이션 종료 감지 - 바이너리 메타데이터 저장 실행");
        saveMetadata();
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) return "unknown_file";
        return fileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
    }
}