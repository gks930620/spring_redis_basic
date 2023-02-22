package com.prac.prac;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


//git에 올리는것까지만 하자
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(){
        return "home";
    }
}
