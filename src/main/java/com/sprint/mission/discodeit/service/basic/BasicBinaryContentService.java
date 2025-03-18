package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentFindResponse;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UUID create(BinaryContentCreateRequest binaryContentCreateRequest) {
        return null;
    }

    @Override
    public BinaryContentFindResponse findById(UUID id) {
        return null;
    }

    @Override
    public List<BinaryContentFindResponse> findAllByIdIn(List<UUID> ids) {
        return List.of();
    }

    @Override
    public void deleteByID(UUID id) {

    }
}
