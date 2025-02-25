package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.service.CategoryService;

public class JCFCategoryService implements CategoryService {
    private static JCFCategoryService instance;

    private JCFCategoryService() {
    }

    public static JCFCategoryService getInstance() {
        if (instance == null) {
            instance = new JCFCategoryService();
        }
        return instance;
    }

    @Override
    public void add( ) {

    }

    @Override
    public void remove( ) {

    }

    @Override
    public void update(String replaceName) {

    }

    @Override
    public void printCurrent() {
    }
}
