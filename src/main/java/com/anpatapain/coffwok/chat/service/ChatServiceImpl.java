package com.anpatapain.coffwok.chat.service;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.exception.UnAuthorizedActionException;
import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.model.Message;
import com.anpatapain.coffwok.chat.repository.ChatRoomRepository;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService{
    private Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private ChatRoomRepository chatRoomRepository;
    private UserRepository userRepository;

    @Autowired
    public ChatServiceImpl(ChatRoomRepository chatRoomRepository,
                           UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ChatRoom getOneByChatRoomId(String id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("chatroom", "id", id));
        return chatRoom;
    }

    @Override
    public ChatRoom getChatRoomByUserIds(String userId1, String userId2) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findChatRoomByUserIds(userId1, userId2);
        if(chatRoomOptional.isPresent()) {
            return chatRoomOptional.get();
        }else {
            try {
                ChatRoom chatRoom = createChatRoom(userId1, userId2);
                return chatRoom;
            }catch (ResourceNotFoundException e) {
                throw e;
            }
        }
    }

    @Override
    public ChatRoom createChatRoom(String userId1, String userId2) {
        ChatRoom chatRoom = new ChatRoom(userId1, userId2);
        chatRoom = chatRoomRepository.save(chatRoom);

        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId1));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId2));

        user1.addChatRoomId(chatRoom.getId());
        user2.addChatRoomId(chatRoom.getId());

        userRepository.save(user1);
        userRepository.save(user2);

        return chatRoom;
    }

    @Override
    public List<ChatRoom> getAllChatRoomsByCurrentUser(User user) {
        List<ChatRoom> chatRooms = user.getChatRoomIds()
                .stream()
                .map(chatRoomRepository::findById)
                .filter(optionalChatRoom -> optionalChatRoom.isPresent())
                .map(Optional::get)
                .collect(Collectors.toList());
        return chatRooms;
    }

    public ChatRoom pushMessageIntoChatRoom(User user, MessageDTO messageDTO, String chat_room_id)
            throws UnAuthorizedActionException, ResourceNotFoundException{
        // Ensure that chatroom exists
        ChatRoom chatRoom = chatRoomRepository.findById(chat_room_id)
                .orElseThrow(() -> new ResourceNotFoundException("chatroom", "id", chat_room_id));

        // Ensure that current user is member of this chatroom
        if(!user.getId().equals(chatRoom.getUserId1()) && !user.getId().equals(chatRoom.getUserId2())) {
            throw new UnAuthorizedActionException("Unauthorized action. You must be in the chat room to do this action");
        }

        Message message = new Message(messageDTO.getMessageType(),
                                      messageDTO.getLocalDateTime(),
                                      messageDTO.getText(),
                                      messageDTO.getSenderId(),
                                      chat_room_id);
        chatRoom.addMessage(message);
        chatRoom = chatRoomRepository.save(chatRoom);

        return chatRoom;
    }

    @Override
    public void deleteOneById(String chatRoomId) {

    }
}