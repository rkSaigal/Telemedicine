//package com.telemedicicne.telemedicicne.newVideoCall;
//
//
//
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Component
//public class VideoCallHandler extends TextWebSocketHandler {
//
//    private final UserServiceee userService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public VideoCallHandler(UserServiceee userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String id = userService.createUser(session);
//        if (id != null) {
//            session.sendMessage(new TextMessage("{\"init\": {\"id\": \"" + id + "\"}}"));
//        } else {
//            session.sendMessage(new TextMessage("{\"error\": {\"message\": \"Failed to generate user ID\"}}"));
//            session.close(CloseStatus.SERVER_ERROR);
//        }
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        // Parse the payload using Jackson
//        JsonNode jsonNode = objectMapper.readTree(payload);
//        String type = jsonNode.get("type").asText();
//        String to = jsonNode.has("to") ? jsonNode.get("to").asText() : null;
//        WebSocketSession receiver;
//
//        switch (type) {
//            case "request":
//                receiver = userService.getUser(to);
//                if (receiver != null) {
//                    receiver.sendMessage(new TextMessage("{\"request\": {\"from\": \"" + session.getId() + "\"}}"));
//                }
//                break;
//            case "call":
//                receiver = userService.getUser(to);
//                if (receiver != null) {
//                    receiver.sendMessage(new TextMessage("{\"call\": {\"from\": \"" + session.getId() + "\"}}"));
//                } else {
//                    session.sendMessage(new TextMessage("{\"failed\": {\"message\": \"User not available\"}}"));
//                }
//                break;
//            case "end":
//                receiver = userService.getUser(to);
//                if (receiver != null) {
//                    receiver.sendMessage(new TextMessage("{\"end\": {\"from\": \"" + session.getId() + "\"}}"));
//                }
//                break;
//            default:
//                session.sendMessage(new TextMessage("{\"error\": {\"message\": \"Unknown message type\"}}"));
//                break;
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        // Find the user ID associated with this session and remove it
//        // Assuming user ID is stored as an attribute in session
//        String id = (String) session.getAttributes().get("userID");
//        userService.removeUser(id);
//        System.out.println(id + " disconnected");
//    }
//
//}


package com.telemedicicne.telemedicicne.newVideoCall;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class VideoCallHandler extends TextWebSocketHandler {

    private final UserServiceee userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VideoCallHandler(UserServiceee userService) {
        this.userService = userService;
    }

//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String id = userService.createUser(session);
//        if (id != null) {
//            session.sendMessage(new TextMessage("{\"init\": {\"id\": \"" + id + "\"}}"));
//        } else {
//            session.sendMessage(new TextMessage("{\"error\": {\"message\": \"Failed to generate user ID\"}}"));
//            session.close(CloseStatus.SERVER_ERROR);
//        }
//    }
@Override
public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    String id = userService.createUser(session);
    if (id != null) {
        session.sendMessage(new TextMessage("{\"init\": {\"id\": \"" + id + "\"}}"));
    } else {
        session.sendMessage(new TextMessage("{\"error\": {\"message\": \"Failed to generate user ID\"}}"));
        session.close(CloseStatus.SERVER_ERROR);
    }
}


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // Parse the payload using Jackson
        JsonNode jsonNode = objectMapper.readTree(payload);
        String type = jsonNode.get("type").asText();
        String to = jsonNode.has("to") ? jsonNode.get("to").asText() : null;
        WebSocketSession receiver;

        switch (type) {
            case "request":
                receiver = userService.getUser(to);
                if (receiver != null) {
                    receiver.sendMessage(new TextMessage("{\"request\": {\"from\": \"" + session.getId() + "\"}}"));
                }
                break;
            case "call":
                receiver = userService.getUser(to);
                if (receiver != null) {
                    receiver.sendMessage(new TextMessage("{\"call\": {\"from\": \"" + session.getId() + "\"}}"));
                } else {
                    session.sendMessage(new TextMessage("{\"failed\": {\"message\": \"User not available\"}}"));
                }
                break;
            case "end":
                receiver = userService.getUser(to);
                if (receiver != null) {
                    receiver.sendMessage(new TextMessage("{\"end\": {\"from\": \"" + session.getId() + "\"}}"));
                }
                break;
            default:
                session.sendMessage(new TextMessage("{\"error\": {\"message\": \"Unknown message type\"}}"));
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Find the user ID associated with this session and remove it
        String id = (String) session.getAttributes().get("userID");
        if (id != null) {
            userService.removeUser(id);
            System.out.println(id + " disconnected");
        } else {
            System.out.println("User ID not found for session: " + session.getId());
        }
    }
}
