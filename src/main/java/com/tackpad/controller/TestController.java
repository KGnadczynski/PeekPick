package com.tackpad.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Kamil on 15.11.2016.
 */
@RestController
public class TestController {

    @RequestMapping("/")
    public String hello() {
        return "Hello world";
    }
}
