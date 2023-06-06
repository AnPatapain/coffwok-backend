package com.anpatapain.coffwok.image_upload.exception;

public class ImageUploadException extends RuntimeException{
    public ImageUploadException() {
        super("could not upload image to cloudinary");
    }
}
