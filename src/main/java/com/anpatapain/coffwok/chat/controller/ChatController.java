package com.anpatapain.coffwok.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat_rooms")
public class ChatController {
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOneByUserIds(@RequestParam("user_id1") String userId1,
                                             @RequestParam("user_id2") String userId2) {
        return null;
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
