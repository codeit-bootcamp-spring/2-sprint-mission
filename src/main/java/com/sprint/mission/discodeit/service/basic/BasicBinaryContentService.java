package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.BinaryContentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent createBinaryContent(BinaryContentCreateDto createDto) {
        // 중복 여부 확인
//        if (binaryContentRepository.existsByFileHashAndFileSize(createDto.fileHash(), createDto.fileSize())) {
//            throw new IllegalArgumentException("이미 존재하는 파일입니다.");
//        }
        return binaryContentRepository.save(createDto.convertCreateDtoToBinaryContent());
    }

    @Override
    public BinaryContentResponseDto findById(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 BinaryContent를 찾을 수 없습니다: " + id));
        return BinaryContentResponseDto.convertToResponseDto(binaryContent);
    }

    @Override
    public byte[] findBinaryById(UUID id) {
        return binaryContentRepository.findBinaryById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 BinaryContent를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> idList) {
        List<BinaryContent> binaryContentList = binaryContentRepository.findAllByIdIn(idList);
        return binaryContentList.stream()
                .map(BinaryContentResponseDto::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContent(UUID id) {
        checkBinaryContentExists(id);
        binaryContentRepository.deleteById(id);
    }

    @Override
    public List<byte[]> findAll() {
        return binaryContentRepository.findAll();
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkBinaryContentExists(UUID id) {
        if(binaryContentRepository.findById(id).isEmpty()){
            throw new NoSuchElementException("해당 BinaryContent가 존재하지 않습니다. : " + id);
        }
    }

}
