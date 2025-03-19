package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JCFBinaryContentRepository extends AbstractRepository<BinaryContent> implements BinaryContentRepository {
    public JCFBinaryContentRepository() {
        super(BinaryContent.class, new ConcurrentHashMap<>());      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> responses = new ArrayList<>();
        for(UUID id : ids) {
            BinaryContent binaryContent = findById(id);
            responses.add(binaryContent);
        }
        return responses;
    }
}