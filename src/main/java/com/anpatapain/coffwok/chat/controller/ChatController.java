package com.anpatapain.coffwok.chat.controller;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.exception.UnAuthorizedActionException;
import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.service.ChatService;
import com.anpatapain.coffwok.chat.service.ChatServiceImpl;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/chat_rooms")
public class ChatController {
    private Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private ChatService chatService;
    private UserService userService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    /**
     * Return chat room between userId1 and userId2 if existed, create a new one and return if not
     * @param userId1
     * @param userId2
     * @return ChatRoom
     */
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOneByUserIds(@RequestParam("user_id1") String userId1,
                                             @RequestParam("user_id2") String userId2) {
        User user;
        try {
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("user not found");
        }

        if( !user.getId().equals(userId1) && !user.getId().equals(userId2) ) {
            return ResponseEntity.badRequest().body("Unauthorized request");
        }

        try{
            ChatRoom chatRoom = chatService.getChatRoomByUserIds(userId1, userId2);
            return ResponseEntity.ok(chatRoom);
        }catch(ResourceNotFoundException e) {
            logger.error("error inside chatController " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    /**
     * Return chat room identified by id
     * @param id
     * @return ChatRoom
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        User user;
        try {
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("user not found");
        }

        try {
            ChatRoom chatRoom = chatService.getOneByChatRoomId(id);
            if(!user.getId().equals(chatRoom.getUserId1()) && !user.getId().equals(chatRoom.getUserId2())) {
                return ResponseEntity.badRequest().body("unauthorized request");
            }
            return ResponseEntity.ok(chatRoom);
        }catch (ResourceNotFoundException e) {
            logger.error("error inside chatController " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }


    /**
     * return all chat rooms that current user of application has
     * @return List<ChatRoom>
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getChatRoomsForCurrentUser() {
        User user;
        try {
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("user not found");
        }
        List<ChatRoom> chatRooms = chatService.getAllChatRoomsByCurrentUser(user);
        return ResponseEntity.ok(chatRooms);
    }

    @PostMapping("/{chat_room_id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> sendMessageToChatRoom(@Valid @RequestBody MessageDTO messageDTO,
                                                   @PathVariable String chat_room_id) {
        User user;
        try {
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("user not found");
        }

        try{
            ChatRoom chatRoom = chatService.pushMessageIntoChatRoom(user, messageDTO, chat_room_id);
            return ResponseEntity.ok(chatRoom);
        }catch (ResourceNotFoundException | UnAuthorizedActionException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    /**
     * Delete chat room identified by id.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOne(@PathVariable String id) {
        return null;
    }
}
