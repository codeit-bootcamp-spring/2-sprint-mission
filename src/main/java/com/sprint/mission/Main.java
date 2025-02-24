package com.sprint.mission;

import com.sprint.mission.controller.UserService;
import com.sprint.mission.controller.UserServiceImpl;
import com.sprint.mission.view.input.UserInput;
import com.sprint.mission.view.output.UserOutput;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        UserOutput userOutput = new UserOutput();

        UserService userService = new UserServiceImpl(userOutput);

        UserInput userInput = new UserInput(userService);

        userInput.creatInput();
    }
}