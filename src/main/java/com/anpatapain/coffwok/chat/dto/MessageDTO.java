package com.anpatapain.coffwok.chat.dto;

import com.anpatapain.coffwok.chat.model.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter @Setter @NoArgsConstructor
public class MessageDTO {
    @NotNull
    private MessageType messageType;
    @NotNull
    private LocalDateTime localDateTime;
    @NotBlank
    private String text;
    @NotNull
    private String senderId;

    public MessageDTO(MessageType messageType, LocalDateTime localDateTime, String text, String senderId) {
        this.messageType = messageType;
        this.localDateTime = localDateTime;
        this.text = text;
        this.senderId = senderId;
    }

}
