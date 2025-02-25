package com.sprint.mission.service;

import com.sprint.mission.model.entity.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.view.output.UserOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserOutput userOutput;
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, UserOutput userOutput) {
        this.userRepository = userRepository;
        this.userOutput = userOutput;
    }

    @Override
    public void createUser(String username, String email, String password) {
        User newUser = new User(username, password, email);
        userRepository.save(newUser);
        userOutput.creatOutput(newUser);
    }


    @Override
    public void updateUser(String email, String username, String password) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() || byEmail != null) {
            User user = byEmail.get();
            user.update(username, password);
            userOutput.updatedOutput();
        }else{
            System.out.println("정보를 찾을 수 없습니다ㅠㅠ");
        }
    }


    @Override
    public void getAllUser() {
        List<User> all = userRepository.findAll();
        userOutput.allOutput(all);
    }


    @Override
    public void getEmailUser(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent()) {
            userOutput.getEmailOutput(byEmail);
        }else{
            userOutput.getEmailOutput(null);
        }
    }

}


