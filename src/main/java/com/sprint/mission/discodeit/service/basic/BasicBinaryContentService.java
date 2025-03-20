package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.IMAGE_STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constant.FilePath.JPG_EXTENSION;
import static com.sprint.mission.util.FileUtils.init;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public UUID createProfileImage(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return null;
        }

        Path imageFile = IMAGE_STORAGE_DIRECTORY.resolve(UUID.randomUUID() + JPG_EXTENSION);
        saveImageFileToPath(multipartFile, IMAGE_STORAGE_DIRECTORY, imageFile);

        BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(imageFile));

        return binaryContent.getProfileId();
    }

    @Override
    public BinaryContentDto findById(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 컨텐츠가 없습니다."));

        return BinaryContentDto.fromEntity(binaryContent);
    }

    @Override
    public void delete(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 컨텐츠가 없습니다."));

        deleteImageFileFromPath(binaryContent.getPath());
        binaryContentRepository.delete(id);
    }

    private void deleteImageFileFromPath(Path imageFilePath) {
        try {
            Files.deleteIfExists(imageFilePath);
        } catch (IOException e) {
            throw new UncheckedIOException("프로필이미지 파일을 삭제할 수 없습니다.", e);
        }
    }

    private void saveImageFileToPath(MultipartFile multipartFile, Path directoryPath, Path imageFilePath) {
        try {
            init(directoryPath);
            Files.write(imageFilePath, multipartFile.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("프로필이미지 파일을 저장할 수 없습니다.", e);
        }
    }
}
