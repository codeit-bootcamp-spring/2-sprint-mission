package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class BasicFileStorageService implements FileStorageService {
    private final Path fileStorageLocation;
    private static final Logger logger = LoggerFactory.getLogger(BasicFileStorageService.class);

    public BasicFileStorageService(RepositoryProperties repositoryProperties) {
        this.fileStorageLocation = Paths.get(repositoryProperties.getFileDirectory()).resolve("uploadFiles");
    }

    @Override
    public BinaryContentCreateRequest uploadFile(MultipartFile file, UUID fileId) {
        try {
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            String extension = getExtension(file.getOriginalFilename());
            String fileName = fileId.toString() + extension;
            Path filePath = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            return new BinaryContentCreateRequest(fileId, filePath.toAbsolutePath().toString(), fileName, file.getContentType(), file.getSize());
        } catch (IOException e) {
            logger.error("파일 저장 중 오류 발생: {}", fileStorageLocation.toAbsolutePath(), e);
            throw new RuntimeException();
        }
    }

    @Override
    public String getExtension(String originalFileName) {
        return originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";
    }

    @Override
    public void deleteFile(UUID fileId) {
        try (Stream<Path> paths = Files.list(fileStorageLocation)) {
            paths.filter(path -> path.getFileName().toString().startsWith(fileId.toString()))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            logger.error("해당 파일을 삭제할 수 없습니다 :{}", fileId, e);
                        }
                    });
        } catch (IOException e) {
            logger.error("해당 디렉토리에 접근할 수 없습니다 : {}", fileStorageLocation.toAbsolutePath(), e);
        }
    }
}
