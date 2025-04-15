package com.sprint.discodeit.sprint.service.basic.util;

import com.sprint.discodeit.sprint.domain.dto.binaryContentDto.BinaryContentDto;
import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint.domain.storage.BinaryContentStorage;
import com.sprint.discodeit.sprint.repository.BinaryContentRepository;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentService implements BinaryService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public BinaryContent createProfileImage(String imgUrl) {
        try {
            // 1. 외부 URL에서 이미지 다운로드
            byte[] fileBytes = downloadImageAsBytes(imgUrl);

            // 2. 메타 정보 생성
            BinaryContent binaryContent = BinaryContent.builder()
                    .fileName(extractFileName(imgUrl))
                    .fileSize((long) fileBytes.length)
                    .fileType("image/png") // 실제 contentType 구할 수도 있음
                    .build();

            // 3. 메타 정보 저장
            binaryContentRepository.save(binaryContent);

            // 4. 실제 파일 저장
            binaryContentStorage.put(binaryContent.getId(), fileBytes);

            return binaryContent;

        } catch (Exception e) {
            throw new RuntimeException("프로필 이미지 저장 실패", e);
        }
    }

    private byte[] downloadImageAsBytes(String imgUrl) throws IOException {
        try (InputStream in = new URL(imgUrl).openStream()) {
            return in.readAllBytes();
        }
    }

    private String extractFileName(String imgUrl) {
        return imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
    }

    public BinaryContentDto find(Long binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new IllegalArgumentException("없는 파일 입니다"));
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getFileSize(),
                binaryContent.getFileType()
        );
    }
}


