package com.sprint.mission.service;

import com.sprint.sprint1.mission.service.usersServiceImpl;
import com.sprint.sprint1.mission.view.output.usersOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class usersServiceTest {

    private usersServiceImpl usersService;
    private usersOutput usersOutput;

    @BeforeEach
    void setUp() {
       // usersOutput = mock(usersOutput.class);
       // usersService = new usersServiceImpl(usersOutput);

        usersService.createusers("John", "codit@naver.com", "codit1234");
        //usersService.createusers("John Doe", "john@example.com", "password123");
    }


    @Test
    void createusers() {

        //given
//        String usersname = "test";
//        String password = "password123";
//        String email = "john@example.com";

        String usersname = "John";
        String password = "codit1234";
        String email = "codit@naver.com";



        //when
        usersService.createusers(usersname, email, password);

        //then
       // users createdusers = usersService.(email).orElse(null);
        //assertNotNull(createdusers);
//        assertEquals(usersname, createdusers.getusersname());
//        assertEquals(email, createdusers.getEmail());
//        assertEquals(password, createdusers.getPassword());


    }

    @Test
    void updateusers() {

        //given
        usersService.createusers("John", "codit@naver.com", "codit1234");

        //when
        usersService.updateusers("John Doe", "john@example.com", "password123");

        //then
//        users users = usersService.findByEmail("john@example.com").orElse(null);
        //assertNotNull(users);
//        assertEquals("john@example.com", users.getEmail());
//        assertEquals("password123", users.getPassword());

    }

    @Test
    void getEmail() {
    }

    @Test
    void getAlluserss() {
    }
}