package com.sprint.mission.discodeit.entity.Container;

import java.util.List;

public class Category extends Container {
    public List<Container> list;

    public Category(String name) {
        super(name);
    }

    public void setList(List<Container> list) {
        this.list = list;
    }

    public List<Container> getList() {
        return list;
    }


}
