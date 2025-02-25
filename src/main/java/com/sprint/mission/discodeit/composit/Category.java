package com.sprint.mission.discodeit.composit;

import java.util.LinkedList;
import java.util.List;

public class Category extends CategoryAndChannel {
    public List<CategoryAndChannel> list;

    public Category(String name) {
        super(name);
    }

    public void setList(List<CategoryAndChannel> list) {
        this.list = list;
    }

    public List<CategoryAndChannel> getList() {
        return list;
    }


}
