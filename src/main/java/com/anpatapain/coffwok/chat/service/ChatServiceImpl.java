package com.anpatapain.coffwok.chat.service;

import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.repository.ChatRoomRepository;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<ChatRoom> getAllByUserId(String userId) {
        return null;
    }

    @Override
    public void deleteOneById(String chatRoomId) {

    }
}
