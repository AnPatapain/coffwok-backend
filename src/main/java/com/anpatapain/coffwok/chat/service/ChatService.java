package com.anpatapain.coffwok.chat.service;

import com.anpatapain.coffwok.chat.model.ChatRoom;

import java.util.List;

public interface ChatService {
    public ChatRoom getChatRoomByUserIds(String userId1, String userId2);
    public List<ChatRoom> getAllByUserId(String userId);
    public void deleteOneById(String chatRoomId);
}
