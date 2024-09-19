package com.pilot.project.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AuthController {

    @GetMapping("/login")
    public ModelAndView login(){
        return new ModelAndView( "login");
    }


}
