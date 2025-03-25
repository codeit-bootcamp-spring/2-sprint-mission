package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public UUID createBinaryContent(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        return findById(binaryContent.getId()).getId();
    }

    @Override
    public BinaryContent findById(UUID id) {
        BinaryContent binaryContent = data.get(id);
        if (binaryContent == null) {
            throw new NoSuchElementException("해당 ID의 BinaryContent를 찾을 수 없습니다: " + id);
        }
        return binaryContent;
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return data.values().stream()
                .filter(binaryContent -> idList.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void deleteBinaryContent(UUID id) {
        checkBinaryContentExists(id);

        data.remove(id);
    }

    @Override
    public List<byte[]> findAll() {
        List<byte[]> fileData = data.values().stream()
                .map(BinaryContent::getFileData)
                .toList();

        return new ArrayList<>(fileData);
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkBinaryContentExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 BinaryContent를 찾을 수 없습니다: " + id);
        }
    }

}
