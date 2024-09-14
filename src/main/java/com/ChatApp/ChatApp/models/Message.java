package com.ChatApp.ChatApp.models;

import java.time.LocalTime;

public class Message {
    private String sender;
    private String content;
    private LocalTime sentAt;

    public Message() {
    }

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public Message(String sender, String content, LocalTime sentAt) {
        this.sender = sender;
        this.content = content;
        this.sentAt = sentAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalTime sentAt) {
        this.sentAt = sentAt;
    }
}
