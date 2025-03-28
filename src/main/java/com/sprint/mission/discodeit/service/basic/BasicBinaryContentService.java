package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentFindResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentUploadResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final FileStorageService fileStorageService;

    @Override
    public BinaryContentUploadResponse uploadSingle(MultipartFile file) {
        UUID fileId = UUID.randomUUID();
        BinaryContentCreateRequest request = fileStorageService.uploadFile(file, fileId);
        // 메타데이터 저장
        create(request);
        return new BinaryContentUploadResponse(request.fileId(), request.filePath(), request.fileName(), request.contentType(), request.fileSize());
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
        BinaryContent findBinaryContent = this.binaryContentRepository.findById(id);
        return new BinaryContentFindResponse(
                findBinaryContent.getId(),
                findBinaryContent.getFilePath(),
                findBinaryContent.getFileName(),
                findBinaryContent.getFileType(),
                findBinaryContent.getFileSize()
        );
    }

    @Override
    public List<BinaryContentFindResponse> findAllByIdIn(List<UUID> ids) {
        return this.binaryContentRepository.findAllByIdIn(ids).stream()
                .map(binaryContent -> new BinaryContentFindResponse(
                        binaryContent.getId(),
                        binaryContent.getFilePath(),
                        binaryContent.getFileName(),
                        binaryContent.getFileType(),
                        binaryContent.getFileSize()
                )).toList();
    }

    @Override
    public void deleteByID(UUID id) {
        this.binaryContentRepository.deleteById(id);
    }
}
