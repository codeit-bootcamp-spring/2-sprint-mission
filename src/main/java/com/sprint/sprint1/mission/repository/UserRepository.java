package com.sprint.sprint1.mission.repository;

import com.sprint.sprint1.mission.model.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<User> {

    List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }


    @Override
    public void save(User entity) {
        users.add(entity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(
                users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null));
    }


    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }
}
