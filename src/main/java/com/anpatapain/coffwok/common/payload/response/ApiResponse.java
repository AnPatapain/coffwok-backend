package com.anpatapain.coffwok.common.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse {
    private boolean isSuccess;
    private String message;

    public ApiResponse(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
