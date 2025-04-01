package com.sprint.mission.service;

import com.sprint.sprint1.mission.service.UserServiceImpl;
import com.sprint.sprint1.mission.view.output.UserOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserServiceImpl userService;
    private UserOutput userOutput;

    @BeforeEach
    void setUp() {
       // userOutput = mock(UserOutput.class);
       // userService = new UserServiceImpl(userOutput);

        userService.createUser("John", "codit@naver.com", "codit1234");
        //userService.createUser("John Doe", "john@example.com", "password123");
    }


    @Test
    void createUser() {

        //given
//        String username = "test";
//        String password = "password123";
//        String email = "john@example.com";

        String username = "John";
        String password = "codit1234";
        String email = "codit@naver.com";



        //when
        userService.createUser(username, email, password);

        //then
       // User createdUser = userService.(email).orElse(null);
        //assertNotNull(createdUser);
//        assertEquals(username, createdUser.getUsername());
//        assertEquals(email, createdUser.getEmail());
//        assertEquals(password, createdUser.getPassword());


    }

    @Test
    void updateUser() {

        //given
        userService.createUser("John", "codit@naver.com", "codit1234");

        //when
        userService.updateUser("John Doe", "john@example.com", "password123");

        //then
//        User user = userService.findByEmail("john@example.com").orElse(null);
        //assertNotNull(user);
//        assertEquals("john@example.com", user.getEmail());
//        assertEquals("password123", user.getPassword());

    }

    @Test
    void getEmail() {
    }

    @Test
    void getAllUsers() {
    }
}