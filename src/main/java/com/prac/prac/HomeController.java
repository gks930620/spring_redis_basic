package com.prac.prac;

import com.prac.prac.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;


@Controller
public class HomeController {

    @Autowired
    MemberReplository memberReplository;

    @PostConstruct
    public void initData(){
        Member member=new Member("pracId1","password1","한창희");   //id,password,username
        memberReplository.save(member);
    }

    @RequestMapping("/")
    public String home(Model model){
        Member member= memberReplository.findById("pracId1").get();
        model.addAttribute("member", member);
        System.out.println(member);
        return "home";
    }


}
