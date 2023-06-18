package com.anpatapain.coffwok.chat.controller;

import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.service.ChatService;
import com.anpatapain.coffwok.chat.service.ChatServiceImpl;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat_rooms")
public class ChatController {
    private Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOneByUserIds(@RequestParam("user_id1") String userId1,
                                             @RequestParam("user_id2") String userId2) {
        try{
            ChatRoom chatRoom = chatService.getChatRoomByUserIds(userId1, userId2);
            return ResponseEntity.ok(chatRoom);
        }catch(ResourceNotFoundException e) {
            logger.error("error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllChatRoomsForCurrentUser() {
        return null;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOne(@PathVariable String id) {
        return null;
    }
}
