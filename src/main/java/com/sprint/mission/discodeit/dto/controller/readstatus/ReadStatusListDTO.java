package com.sprint.mission.discodeit.dto.controller.readstatus;

import com.sprint.mission.discodeit.dto.service.readStatus.FindReadStatusResult;

import java.util.List;

public record ReadStatusListDTO(
    List<FindReadStatusResult> readStatusDTOList
) {

}
