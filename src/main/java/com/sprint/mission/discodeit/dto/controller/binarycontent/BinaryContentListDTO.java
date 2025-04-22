package com.sprint.mission.discodeit.dto.controller.binarycontent;


import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import java.util.List;

public record BinaryContentListDTO(
    List<FindBinaryContentResult> findBinaryContentResultList
) {

}
