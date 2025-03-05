package com.sprint.mission.discodeit.Factory;

public interface Factory<T> {
    T create();
    T create(String s);
}
