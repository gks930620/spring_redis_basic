package com.prac.prac.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prac.prac.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        //객체를 새로 만들어서 주는게 맞나??? 빈에는 필드 안하는게 좋아도 list객체를 그때그떄 만들어서 하는것보다 현재 상태로 갖고있는거 제공하는게 낫지않나?
        //왜 굳이 Map 형태일까? List형태였으면 안됐나??
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }




}