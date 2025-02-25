package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.composit.CategoryAndChannel;

public interface CategoryService {
    public abstract void add(CategoryAndChannel channel);
    public abstract void remove(CategoryAndChannel channel);
    public abstract void update(CategoryAndChannel channel, String replaceName);
    public abstract void printCurrent();

}
