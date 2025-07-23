package com.sprint.mission.discodeit.config.async;

import org.springframework.core.task.TaskDecorator;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChainedTaskDecorator implements TaskDecorator {

    private final List<TaskDecorator> decorators;

    public ChainedTaskDecorator(TaskDecorator... decorators) {
        this.decorators = Arrays.asList(decorators);
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        for (int i = decorators.size() - 1; i >= 0; i--) {
            runnable = decorators.get(i).decorate(runnable);
        }
        return runnable;
    }
}
