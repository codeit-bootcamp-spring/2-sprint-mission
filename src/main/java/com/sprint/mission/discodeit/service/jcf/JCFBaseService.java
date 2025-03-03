package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.service.BaseService;
import java.util.*;

public abstract class JCFBaseService<T> implements BaseService<T> {
    protected final List<T> data;

    public JCFBaseService() {
        this.data = new ArrayList<>();
    }

    @Override
    public T create(T entity) {
        data.add(entity);
        return entity;
    }

    @Override
    public Optional<T> findById(UUID id){
        return data.stream()
                .filter(entity -> ((BaseEntity)entity).getId().equals(id))
                .findFirst();
        }

    @Override
    public List<T> findAll(){
        return new ArrayList<>(data);
        }

    @Override
    public T update(T entity){
        deleteById(((BaseEntity) entity).getId());
        data.add(entity);
        return entity;
        }

    @Override
    public void delete (T entity){
        data.remove(entity);
    }

    @Override
    public void deleteById (UUID id){
        data.removeIf(entity -> ((BaseEntity) entity).getId().equals(id));
        }

    }


