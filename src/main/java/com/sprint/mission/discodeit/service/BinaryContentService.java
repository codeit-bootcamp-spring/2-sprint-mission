package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.*;


import java.util.List;

public interface BinaryContentService {

    BinaryContent create(BinaryContentCreateDto binaryContentCreateDto);
    BinaryContent getUser(BinaryContentFindDto binaryContentFindDto);
    List<BinaryContent> getAllUser();
    BinaryContent updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto);
    void delete(BinaryContentDeleteDto binaryContentDeleteDto);

}
