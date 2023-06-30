package com.anpatapain.coffwok.chat.service;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ChatService {
    public ChatRoom getChatRoomByUserIds(String userId1, String userId2);
    public ChatRoom getOneByChatRoomId(String id);
    public ChatRoom createChatRoom(String userId1, String userId2);
    public List<ChatRoom> getAllChatRoomsByCurrentUser(User user);
    public List<Profile> getAllProfiles(User user);
    public ChatRoom pushMessageIntoChatRoom(User user, MessageDTO messageDTO, String chat_room_id);
    public ChatRoom pushMessageIntoChatRoomWithoutUser(MessageDTO messageDTO, String chat_room_id);
//    public ChatRoom deleteAllMessageForUser(User user, String chat_room_id);
    public void deleteOneById(String chatRoomId);
}
