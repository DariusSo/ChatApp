package com.ChatApp.ChatApp.controllers;

import com.ChatApp.ChatApp.models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public List<String> chatUsersList = new ArrayList<>();
    public List<Message> messageList = new ArrayList<>();

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/chatroom")
    public Message chatMessage(Message message) throws Exception {
        String s = message.getSender();
        String ss = message.getContent();

        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime now = LocalTime.parse(time.format(formatter));
        message.setSentAt(now);

        messageList.add(message);
        addUserToList(message.getSender());

        return new Message(HtmlUtils.htmlEscape(message.getSender()), HtmlUtils.htmlEscape(message.getContent()), LocalTime.parse(HtmlUtils.htmlEscape(String.valueOf(message.getSentAt()))));
    }

    @MessageMapping("/userList")
    @SendTo("/topic/usersList")
    public List<String> getChatUsersList(){
        //String json = new Gson().toJson(chatUsersList);
        return chatUsersList;
    }

    public void addUserToList(String name){
        if(!chatUsersList.contains(name)){
            chatUsersList.add(name);
        }
    }
    @MessageMapping("/leave")
    public void removeUser(String name) throws JsonProcessingException {
        String remove = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonName = mapper.readTree(name);
        String user = jsonName.get("name").asText();
        for(String listName : chatUsersList){
            if(listName.equals(user)){
                remove = listName;
            }
        }
        if(remove != null){
            chatUsersList.remove(remove);
        }
    }
    @MessageMapping("/messageList")
    @SendTo("/topic/messageList")
    public List<Message> getMessageList(){
        return messageList;
    }

    @Scheduled(fixedRate = 3000)
    public void sendGreeting() {
        messagingTemplate.convertAndSend("/topic/heyhey", "Hello ");
    }


}
