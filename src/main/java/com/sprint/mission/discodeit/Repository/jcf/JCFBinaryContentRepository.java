package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.Exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private List<BinaryContent> binaryContentList = new LinkedList<>();

    @Override
    public void save(BinaryContentCreateDTO binaryContentCreateDTO) {
        binaryContentList.add(binaryContentCreateDTO.binaryContent());
    }

    @Override
    public BinaryContent find(UUID binaryId) {
        BinaryContent content = binaryContentList.stream().filter(binaryContent -> binaryContent.getBinaryContentId().equals(binaryId))
                .findFirst().orElseThrow(() -> new BinaryContentNotFoundException("해당 바이너리 데이터는 존재하지 않습니다."));
        return content;
    }

    @Override
    public List<BinaryContent> findAllByIdIn() {
        return binaryContentList;
    }

    @Override
    public boolean delete(UUID binaryId) {
        try {
            BinaryContent content = binaryContentList.stream().filter(binaryContent -> binaryContent.getBinaryContentId().equals(binaryId))
                    .findFirst().orElseThrow(() -> new BinaryContentNotFoundException("해당 바이너리 데이터는 존재하지 않습니다."));
            binaryContentList.remove(content);
            return true;
        } catch (BinaryContentNotFoundException e) {
            System.out.println("해당 바이너리 데이터는 존재하지 않습니다.");
            return false;
        }
    }
}
