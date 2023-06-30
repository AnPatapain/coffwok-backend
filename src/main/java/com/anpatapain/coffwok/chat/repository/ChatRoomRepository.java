package com.anpatapain.coffwok.chat.repository;

import com.anpatapain.coffwok.chat.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findById(String id);
    @Query("{$or:  [{'profile1.userId':  ?0, 'profile2.userId':  ?1}, {'profile1.userId':  ?1, 'profile2.userId':  ?0}]}")
    Optional<ChatRoom> findChatRoomByUserIds(String userId1, String userId2);
}
