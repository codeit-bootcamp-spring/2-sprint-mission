package com.sprint.mission.service;

public interface UserService {


    void createUser(String username, String email, String password);

    void updateUser(String email, String username, String password);

    void getAllUser();

    void getEmailUser(String email);
}
