package com.anpatapain.coffwok.chat.repository;

import com.anpatapain.coffwok.chat.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    @Query("{$or:  [{'userId1':  ?0, 'userId2':  ?1}, {'userId1':  ?1, 'userId2':  ?0}]}")
    Optional<ChatRoom> findChatRoomByUserIds(String userId1, String userId2);
}
