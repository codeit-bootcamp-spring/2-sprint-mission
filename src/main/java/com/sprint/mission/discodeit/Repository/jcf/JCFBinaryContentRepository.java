package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Exception.NotFoundException;
import com.sprint.mission.discodeit.Exception.NotFoundExceptions;
import com.sprint.mission.discodeit.Exception.EmptyExceptions;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private List<BinaryContent> binaryContentList = new LinkedList<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentList.add(binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent find(UUID binaryId) {
        BinaryContent content = CommonUtils.findById(binaryContentList, binaryId, BinaryContent::getBinaryContentId)
                .orElseThrow(() -> NotFoundExceptions.BINARY_CONTENT_NOT_FOUND);
        return content;
    }

    @Override
    public List<BinaryContent> findAllByIdIn() {
        if (binaryContentList.isEmpty()) {
            throw EmptyExceptions.EMPTY_BINARY_CONTENT_LIST;
        }
        return binaryContentList;
    }

    @Override
    public boolean delete(UUID binaryId) {
        try {
            BinaryContent content = find(binaryId);
            binaryContentList.remove(content);
            return true;
        } catch (NotFoundException e) {
            System.out.println("해당 바이너리 데이터는 존재하지 않습니다.");
            return false;
        }
    }
}
