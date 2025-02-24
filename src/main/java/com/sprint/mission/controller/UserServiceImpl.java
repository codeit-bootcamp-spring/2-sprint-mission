package com.sprint.mission.controller;

import com.sprint.mission.model.entity.User;
import com.sprint.mission.view.output.UserOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserOutput userOutput;
    private final List<User> users;

    public UserServiceImpl(UserOutput userOutput) {
        this.users = new ArrayList<>();
        this.userOutput = userOutput;
    }

    @Override
    public void createUser(String username, String email, String password) {

        User newUser = new User(username, password, email);
        userOutput.creatResponse(newUser);
    }


    @Override
    public void updateUser(String email, String username, String password) {
        for(User u : users) {
            if(u.getEmail().equals(email)) {
                u.update(username, password);
                userOutput.UpdatedResponse();
            }
        }
    }

    @Override
    public void getAllUser() {
        userOutput.AllResponse(users);
    }

    @Override
    public void getEmailUser(String email) {
        Optional<User> user = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();

        if(user.isPresent()) {
            userOutput.gatEmailResponse(user.toString());
        }else{
            userOutput.gatEmailResponse(null);
        }
    }


}


