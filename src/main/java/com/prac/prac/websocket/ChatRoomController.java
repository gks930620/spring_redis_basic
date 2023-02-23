package com.prac.prac.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;

@Controller
public class ChatRoomController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("rooms", Arrays.asList("room1", "room2", "room3"));
        return "index";
    }

    @GetMapping("/room/{roomId}")
    public String room(@PathVariable String roomId, Model model) {
        model.addAttribute("roomName", roomId);
        return "room";
    }
}