package com.example.graduationproject.Data;

public class ChatResponse {
    String chatBotReply;

    public ChatResponse(String chatBotReply) {
        this.chatBotReply = chatBotReply;
    }

    public ChatResponse() {
    }

    public String getChatBotReply() {
        return chatBotReply;
    }

    public void setChatBotReply(String chatBotReply) {
        this.chatBotReply = chatBotReply;
    }
}
