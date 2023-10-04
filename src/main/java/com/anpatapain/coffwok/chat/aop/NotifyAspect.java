package com.anpatapain.coffwok.chat.aop;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.service.ChatService;
import com.anpatapain.coffwok.notification.service.NotificationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotifyAspect {
    private final Logger logger = LoggerFactory.getLogger(NotifyAspect.class);
    private final ChatService chatService;
    private final NotificationService notificationService;

    @Autowired
    public NotifyAspect(ChatService chatService, NotificationService notificationService) {
        this.chatService = chatService;
        this.notificationService = notificationService;
    }

    @Pointcut("@annotation(com.anpatapain.coffwok.chat.aop.Notify)")
    public void notifiableMethod(){}

    @AfterReturning("notifiableMethod()")
    public void notifyAdvice(JoinPoint joinPoint) throws Throwable {
        ChatRoom chatRoom = null;
        MessageDTO messageDTO = null;

        Object[] args = joinPoint.getArgs();
        for(Object arg : args) {
            if(arg instanceof String) {
                chatRoom = chatService.getOneByChatRoomId((String)arg);
            }
            if(arg instanceof MessageDTO) {
                messageDTO = (MessageDTO) arg;
            }
        }

        if(chatRoom != null && messageDTO != null) {
            String notifyUserId = chatRoom.getProfile1().getUserId().equals(messageDTO.getSenderId()) ?
                    chatRoom.getProfile2().getUserId() : chatRoom.getProfile1().getUserId();
            notificationService.notifyMessageToUser(notifyUserId, chatRoom.getId(), messageDTO);
        }
    }

}
