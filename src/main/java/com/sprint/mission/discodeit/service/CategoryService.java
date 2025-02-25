package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.composit.CategoryAndChannel;

public interface CategoryService {
    public abstract void add( );
    public abstract void remove( );
    public abstract void update(String replaceName);
    public abstract void printCurrent();

}
