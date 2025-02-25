package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Container;
import com.sprint.mission.discodeit.service.CategoryService;

import java.util.LinkedList;
import java.util.List;

public class JCFCategoryService implements CategoryService {
    private static JCFCategoryService instance;
    public List<Container> list;

    private JCFCategoryService() {
    }

    public static JCFCategoryService getInstance() {
        if (instance == null) {
            instance = new JCFCategoryService();
        }
        return instance;
    }

    public void setList(LinkedList<Container> list) {
        this.list = list;
    }

    @Override
    public void add( ) {

    }

    @Override
    public void remove( ) {

    }

    //업데이트 조건, id가 동일해야한다.
    @Override
    public void update(String replaceName) {

    }

    @Override
    public void printCurrent() {
        if (list.isEmpty()) {
            System.out.println("카테고리와 채널이 없습니다.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
    }
}
