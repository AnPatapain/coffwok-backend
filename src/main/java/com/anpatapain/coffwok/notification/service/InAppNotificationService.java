package com.anpatapain.coffwok.notification.service;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.notification.model.Notification;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.repository.UserRepository;
import com.anpatapain.coffwok.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InAppNotificationService implements NotificationService {
    private SimpMessagingTemplate messagingTemplate;

    private UserService userService;

    private UserRepository userRepository;

    @Autowired
    public InAppNotificationService(SimpMessagingTemplate messagingTemplate,
                                    UserService userService,
                                    UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void notifyMessageToUser(String userId, String chat_room_id, MessageDTO messageDTO) {
        String topic = "/notification/" + userId;
        User user = userService.getUserById(userId);
        Notification notification = new Notification(chat_room_id, messageDTO.getText());
        user.addNotification(notification);
        userRepository.save(user);
        messagingTemplate.convertAndSend(topic, user.getNotificationList());
    }

    @Override
    public List<Notification> deleteChatRoomInNotificationList(User user, String chat_room_id) {
        user.removeNotification(chat_room_id);
        userRepository.save(user);
        return user.getNotificationList();
    }

    @Override
    public List<Notification> getNotificationsForUser(User user) {
        return user.getNotificationList();
    }
}
