package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;

public record AsyncFailureEvent(
    AsyncTaskFailure failure
) {

}
