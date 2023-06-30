package com.anpatapain.coffwok.notification.controller;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.notification.model.Notification;
import com.anpatapain.coffwok.notification.service.NotificationService;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private NotificationService notificationService;
    private UserService userService;
    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/all/{user_id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getNotificationsForUser(@PathVariable String user_id) {
        User user;
        try {
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }
        List<Notification> notificationList = notificationService.getNotificationsForUser(user);
        return ResponseEntity.ok(notificationList);
    }

    @DeleteMapping("/remove/{chat_room_id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> remove_chat_room(@PathVariable String chat_room_id) {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }
        List<Notification> notificationList = notificationService.deleteChatRoomInNotificationList(user, chat_room_id);
        return ResponseEntity.ok(notificationList);
    }
}
