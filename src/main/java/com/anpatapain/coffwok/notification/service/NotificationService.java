package com.anpatapain.coffwok.notification.service;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.notification.model.Notification;
import com.anpatapain.coffwok.user.model.User;

import java.util.List;

public interface NotificationService {
    public void notifyMessageToUser(String userId, String chat_room_id, MessageDTO messageDTO);
    public List<Notification> deleteChatRoomInNotificationList(User user, String chat_room_id);

    public List<Notification> getNotificationsForUser(User user);
}
