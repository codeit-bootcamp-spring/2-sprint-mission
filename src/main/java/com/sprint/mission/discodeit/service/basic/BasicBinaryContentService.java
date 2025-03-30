package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentFindResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentUploadResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.FileFindException;
import com.sprint.mission.discodeit.exception.binarycontent.FileUploadException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final FileStorageService fileStorageService;

    @Override
    public BinaryContentUploadResponse uploadSingle(MultipartFile file) {
        try {
            UUID fileId = UUID.randomUUID();
            BinaryContentCreateRequest request = fileStorageService.uploadFile(file, fileId);
            // 메타데이터 저장
            create(request);
            return new BinaryContentUploadResponse(request.fileId(), request.filePath(), request.fileName(), request.contentType(), request.fileSize());
        } catch (Exception e) {
            throw new FileUploadException("파일 업로드 중 입출력 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR ,e);
        }
    }

    private void create(BinaryContentCreateRequest binaryContentCreateRequest) {
        BinaryContent newBinaryContent = new BinaryContent(binaryContentCreateRequest.fileId(), binaryContentCreateRequest.filePath(), binaryContentCreateRequest.fileName(), binaryContentCreateRequest.contentType(), binaryContentCreateRequest.fileSize());
        this.binaryContentRepository.add(newBinaryContent);
    }

    @Override
    public boolean existsById(UUID id) {
        return binaryContentRepository.existsById(id);
    }

    @Override
    public BinaryContentFindResponse findById(UUID id) {
        try {
            BinaryContent findBinaryContent = this.binaryContentRepository.findById(id);
            byte[] fileBytes = fileStorageService.readFile(Paths.get(findBinaryContent.getFilePath()));
            String base64Bytes = Base64.getEncoder().encodeToString(fileBytes);
            return new BinaryContentFindResponse(
                    findBinaryContent.getId(),
                    findBinaryContent.getFilePath(),
                    findBinaryContent.getFileName(),
                    findBinaryContent.getFileType(),
                    findBinaryContent.getFileSize(),
                    base64Bytes
            );
        } catch (NoSuchElementException e) {
            throw new FileFindException("존재하지 않는 파일 id입니다.", HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            throw new FileFindException("파일 조회 중 예기치 못한 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public List<BinaryContentFindResponse> findAllByIdIn(List<UUID> ids) {
        return this.binaryContentRepository.findAllByIdIn(ids).stream()
                .map(binaryContent -> {
                    byte[] fileBytes = fileStorageService.readFile(Paths.get(binaryContent.getFilePath()));
                    String base64Bytes = Base64.getEncoder().encodeToString(fileBytes);
                    return new BinaryContentFindResponse(
                        binaryContent.getId(),
                        binaryContent.getFilePath(),
                        binaryContent.getFileName(),
                        binaryContent.getFileType(),
                        binaryContent.getFileSize(),
                            base64Bytes
                    );
                }).toList();
    }

    @Override
    public void deleteByID(UUID id) {
        this.binaryContentRepository.deleteById(id);
    }
}
