//package com.telemedicicne.telemedicicne.videoCallConfig;//    package com.telemedicicne.telemedicicne.videoCallConfig;
////
////
////    import com.telemedicicne.telemedicicne.Entity.HealthOfficer;
////    import com.telemedicicne.telemedicicne.Repository.HealthOfficerRepository;
////    import org.slf4j.Logger;
////    import org.slf4j.LoggerFactory;
////    import org.springframework.beans.factory.annotation.Autowired;
////    import org.springframework.web.socket.CloseStatus;
////    import org.springframework.web.socket.TextMessage;
////    import org.springframework.web.socket.WebSocketSession;
////    import org.springframework.web.socket.handler.TextWebSocketHandler;
////
////    import java.util.Collections;
////    import java.util.HashSet;
////    import java.util.Set;
////
////
////
////
////    public class SignalingHandler extends TextWebSocketHandler {
////        @Autowired
////        private HealthOfficerRepository docHsRepository; // Inject DocHsRepository
////
//////    @Override
//////    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//////        // Broadcast the signaling message to all connected clients
//////        for (WebSocketSession webSocketSession : session.getOpenSessions()) {
//////            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
//////                webSocketSession.sendMessage(message);
//////            }
//////        }
//////    }
////
////
////    //old
////
////    // Maintain a set of active sessions
//////    private static final Set<WebSocketSession> sessions = new HashSet<>();
//////
//////    @Override
//////    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//////        // Add new session to the set
//////        sessions.add(session);
//////    }
//////@Override
//////protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//////    // Broadcast the signaling message to all connected clients
//////    for (WebSocketSession webSocketSession : sessions) {
//////        if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
//////            webSocketSession.sendMessage(message);
//////        }
//////    }
//////}
//////
//////    @Override
//////    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus closeStatus) throws Exception {
//////        // Remove closed session from the set
//////        sessions.remove(session);
//////    }
////
////    private static final Logger logger = LoggerFactory.getLogger(SignalingHandler.class);
////
////    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
////
//////    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
////
////    @Override
////    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
////        sessions.add(session);
////        logger.info("Session -------------",session);
////        logger.info("WebSocket session opened: {}", session.getId());
////
////
////    }
////
////    @Override
////
////    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////        String payload = message.getPayload();
////        logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
////        logger.info("Received message from session payload ------ {}: {}", message.getPayload());
////
////        synchronized (sessions) {
////            for (WebSocketSession webSocketSession : sessions) {
////                if (webSocketSession.isOpen() && !webSocketSession.equals(session)) {
////                    webSocketSession.sendMessage(message);
////                }
////            }
////        }
////    }
////
////    @Override
////    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
////        sessions.remove(session);
////        logger.info("WebSocket session closed: {}", session.getId());
////
////    }
////
////        private String determineRecipient(String recipientEmail) {
////            // Implement logic to fetch recipient email dynamically from HealthOfficer table
////            // Example: Fetch by a condition or criteria
////            // Here, we fetch based on a specific email condition for demonstration
////            // Replace with your actual logic based on session, message content, or any other criteria
////            String fetchedRecipientEmail = null;
////
////            // Example: Fetching recipient email based on a predefined condition (for demo)
////            // Replace with your actual logic to fetch dynamically from HealthOfficer table
////            // For example, based on session attributes or message content
////            // Hardcoded example to find recipient with the provided email
////            HealthOfficer recipient = docHsRepository.findByEmail(recipientEmail);
////
////            if (recipient != null) {
////                fetchedRecipientEmail = recipient.getEmail(); // Get recipient's email
////            }
////
////            return fetchedRecipientEmail;
////        }
////
////    }
//
//
//
//
////import com.telemedicicne.telemedicicne.Repository.HealthOfficerRepository;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Component;
////import org.springframework.web.socket.CloseStatus;
////import org.springframework.web.socket.TextMessage;
////import org.springframework.web.socket.WebSocketSession;
////import org.springframework.web.socket.handler.TextWebSocketHandler;
////
////import java.util.Collections;
////import java.util.HashSet;
////import java.util.Set;
////
////@Component
////public class SignalingHandler extends TextWebSocketHandler {
////
////    private static final Logger logger = LoggerFactory.getLogger(SignalingHandler.class);
////
////    @Autowired
////    private HealthOfficerRepository docHsRepository; // Inject DocHsRepository
////
////    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
////
////    @Override
////    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
////        sessions.add(session);
////        logger.info("WebSocket session opened: {}", session.getId());
////    }
////
////    @Override
////    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////        String payload = message.getPayload();
////        logger.info("Received message from session {}: {}", session.getId(), payload);
////
////        // Example logic to determine recipient email dynamically
////        String recipientEmail = determineRecipient(session);
////
////        if (recipientEmail != null) {
////            // Find the WebSocketSession associated with the recipient
////            WebSocketSession recipientSession = findSessionByEmail(recipientEmail);
////
////            if (recipientSession != null && recipientSession.isOpen()) {
////                recipientSession.sendMessage(message); // Send the message to the recipient
////            } else {
////                logger.warn("Recipient session not found or closed for email: {}", recipientEmail);
////                // Handle the case where recipient session is not found or closed
////            }
////        } else {
////            logger.warn("Recipient email not found or determined.");
////            // Handle the case where recipient email is not found
////        }
////    }
////
////    private String determineRecipient(WebSocketSession session) {
////        // Implement logic to fetch recipient email dynamically
////        // For example, you can extract it from session attributes, message content, or any other logic
////        // Here, we demonstrate fetching by a predefined condition
////        // Replace with your actual logic based on your application's requirements
////        return "healthofficer@gmail.com"; // Example hardcoded recipient email for demonstration
////    }
////
////    private WebSocketSession findSessionByEmail(String email) {
////        // Implement logic to find WebSocketSession by recipient email
////        // Iterate through sessions to find the matching session
////        for (WebSocketSession webSocketSession : sessions) {
////            if (webSocketSession.isOpen()) {
////                // Example: Check if the session belongs to the recipient email
////                // You can implement more sophisticated checks as per your application's needs
////                if (webSocketSession.getAttributes().get("email").equals(email)) {
////                    return webSocketSession;
////                }
////            }
////        }
////        return null; // Return null if session not found
////    }
////
////    @Override
////    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
////        sessions.remove(session);
////        logger.info("WebSocket session closed: {}", session.getId());
////    }
////}
//
//
////
////
////
////import org.springframework.web.socket.CloseStatus;
////import org.springframework.web.socket.TextMessage;
////import org.springframework.web.socket.WebSocketSession;
////import org.springframework.web.socket.handler.TextWebSocketHandler;
////import com.fasterxml.jackson.databind.ObjectMapper;
////
////import java.util.HashMap;
////import java.util.Map;
////import java.util.concurrent.ConcurrentHashMap;
////
////public class SignalingHandler extends TextWebSocketHandler {
////
////    private static final Map<String, WebSocketSession> users = new ConcurrentHashMap<>();
////    private final ObjectMapper objectMapper = new ObjectMapper();
////
////    @Override
////    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
////        users.put(session.getId(), session);
////        broadcastToAll("users:joined", session.getId());
////        sendMessage(session, "hello", Map.of("id", session.getId()));
////    }
////
////    @Override
////    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////        Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);
////        String type = (String) data.get("type");
////
////        switch (type) {
////            case "outgoing:call":
////                handleOutgoingCall(session, data);
////                break;
////            case "call:accepted":
////                handleCallAccepted(session, data);
////                break;
////            default:
////                break;
////        }
////    }
////
////    private void handleOutgoingCall(WebSocketSession session, Map<String, Object> data) throws Exception {
////        String to = (String) data.get("to");
////        String fromOffer = (String) data.get("fromOffer");
////        WebSocketSession recipientSession = users.get(to);
////        if (recipientSession != null) {
////            sendMessage(recipientSession, "incomming:call", Map.of("from", session.getId(), "offer", fromOffer));
////        }
////    }
////
////    private void handleCallAccepted(WebSocketSession session, Map<String, Object> data) throws Exception {
////        String to = (String) data.get("to");
////        String answer = (String) data.get("answere");
////        WebSocketSession recipientSession = users.get(to);
////        if (recipientSession != null) {
////            sendMessage(recipientSession, "incomming:answere", Map.of("from", session.getId(), "offer", answer));
////        }
////    }
////
////    @Override
////    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
////        users.remove(session.getId());
////        broadcastToAll("user:disconnect", session.getId());
////    }
////
////    private void sendMessage(WebSocketSession session, String type, Map<String, Object> data) throws Exception {
////        data.put("type", type);
////        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(data)));
////    }
////
////    private void broadcastToAll(String type, String id) throws Exception {
////        Map<String, Object> data = new HashMap<>();
////        data.put("id", id);
////        data.put("type", type);
////        TextMessage message = new TextMessage(objectMapper.writeValueAsString(data));
////        for (WebSocketSession session : users.values()) {
////            session.sendMessage(message);
////        }
////    }
////}
//
//
////import com.fasterxml.jackson.core.JsonParseException;
////import com.fasterxml.jackson.core.type.TypeReference;
////import com.fasterxml.jackson.databind.JsonMappingException;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.web.socket.CloseStatus;
////import org.springframework.web.socket.TextMessage;
////import org.springframework.web.socket.WebSocketSession;
////import org.springframework.web.socket.handler.TextWebSocketHandler;
////
////import java.io.IOException;
////import java.util.HashMap;
////import java.util.Map;
////import java.util.concurrent.ConcurrentHashMap;
////
////import com.fasterxml.jackson.core.JsonParseException;
////import com.fasterxml.jackson.databind.JsonMappingException;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import io.jsonwebtoken.io.IOException;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.web.socket.CloseStatus;
////import org.springframework.web.socket.TextMessage;
////import org.springframework.web.socket.WebSocketSession;
////import org.springframework.web.socket.handler.TextWebSocketHandler;
////import com.fasterxml.jackson.core.type.TypeReference;
////
////import java.util.HashMap;
////import java.util.Map;
////import java.util.concurrent.ConcurrentHashMap;
////
////public class SignalingHandler extends TextWebSocketHandler {
////
////    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
////    private static final Logger logger = LoggerFactory.getLogger(SignalingHandler.class);
////
////    @Override
////    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
////        sessions.put(session.getId(), session);
////
////        broadcastToAll("users:joined", session.getId());
////        sendMessage(session, "outgoing:call", Map.of("id", session.getId()));
////    }
////
//////    @Override
//////    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//////        sessions.put(session.getId(), session);
//////        logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
//////        Map<String, Object> data = new ObjectMapper().readValue(message.getPayload(), Map.class);
//////        String type = (String) data.get("type");
//////
//////        logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
//////        switch (type) {
//////            case "outgoing:call":
//////                handleOutgoingCall(session, data );
//////                break;
//////            case "call:accepted":
//////                handleCallAccepted(session, data);
//////                break;
//////            default:
//////                break;
//////        }
//////    }
//////@Override
//////protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//////    sessions.put(session.getId(), session);
//////    logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
//////
////////    try {
//////        // Attempt to parse the message payload as JSON
//////        Map<String, Object> data = new ObjectMapper().readValue(message.getPayload(), new TypeReference<Map<String, Object>>() {});
//////        String type = (String) data.get("type");
//////
//////        logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
//////        switch (type) {
//////            case "outgoing:call":
//////                handleOutgoingCall(session, data);
//////                break;
//////            case "call:accepted":
//////                handleCallAccepted(session, data);
//////                break;
//////            default:
//////                logger.warn("Unknown message type received: {}", type);
//////                break;
//////        }
////////    }
////////    catch (JsonParseException | JsonMappingException e) {
////////        // Handle JSON parsing errors
////////        logger.error("Error parsing JSON message from session {}: {}", session.getId(), e.getMessage());
////////        // Optionally, you can close the session or notify the client about the invalid message
////////    }
//////}
////
////    @Override
////    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////        sessions.put(session.getId(), session);
////        logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
////
////        try {
////            // Attempt to parse the message payload as JSON
////            Map<String, Object> data = new ObjectMapper().readValue(message.getPayload(), new TypeReference<Map<String, Object>>() {});
////            String type = (String) data.get("type");
////
////            logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
////            switch (type) {
////                case "outgoing:call":
////                    handleOutgoingCall(session, data);
////                    break;
////                case "call:accepted":
////                    handleCallAccepted(session, data);
////                    break;
////                default:
////                    logger.warn("Unknown message type received: {}", type);
////                    break;
////            }
////        } catch (JsonParseException | JsonMappingException e) {
////            // Handle JSON parsing errors
////            logger.error("Error parsing JSON message from session {}: {}", session.getId(), e.getMessage());
////            // Optionally, you can close the session or notify the client about the invalid message
////            session.close(CloseStatus.BAD_DATA);
////        }
////    }
////
////
////
////    private void handleOutgoingCall(WebSocketSession session, Map<String, Object> data) throws IOException {
////        String to = (String) data.get("to");
////        String fromOffer = (String) data.get("fromOffer");
////        WebSocketSession recipientSession = sessions.get(to);
////        if (recipientSession != null) {
////            sendMessage(recipientSession, "incoming:call", Map.of("from", session.getId(), "offer", fromOffer));
////        }
////    }
////
////    private void handleCallAccepted(WebSocketSession session, Map<String, Object> data) throws IOException {
////        String to = (String) data.get("to");
////        String answer = (String) data.get("answer");
////        WebSocketSession recipientSession = sessions.get(to);
////        if (recipientSession != null) {
////            sendMessage(recipientSession, "incoming:answer", Map.of("from", session.getId(), "answer", answer));
////        }
////    }
////
////    @Override
////    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
////        sessions.remove(session.getId());
////        broadcastToAll("user:disconnect", session.getId());
////    }
////
//////    private void sendMessage(WebSocketSession session, String type, Map<String, Object> data) throws IOException {
//////        Map<String, Object> messageData = new HashMap<>(data); // Ensure mutable map for modifications
//////        messageData.put("type", type);
//////        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(messageData)));
//////    }
////private void sendMessage(WebSocketSession session, String type, Map<String, Object> data) throws IOException {
////    if (session.isOpen()) {
////        Map<String, Object> messageData = new HashMap<>(data); // Ensure mutable map for modifications
////        messageData.put("type", type);
////        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(messageData)));
////    } else {
////        // Handle the case where the session is closed, maybe log it or take appropriate action
////        logger.warn("Trying to send message to a closed session: {}", session.getId());
////        // Optionally, you can remove the session from your sessions map here if needed
////        sessions.remove(session.getId());
////    }
////}
////
////
////    private void broadcastToAll(String type, String id) throws IOException {
////        Map<String, Object> data = Map.of("type", type, "id", id); // Immutable map is safe for read-only operations
////        TextMessage message = new TextMessage(new ObjectMapper().writeValueAsString(data));
////        for (WebSocketSession session : sessions.values()) {
////            session.sendMessage(message);
////        }
////    }
////}
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class SignalingHandler extends TextWebSocketHandler {
//
//    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//    private static final Logger logger = LoggerFactory.getLogger(SignalingHandler.class);
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.put(session.getId(), session);
//
//        broadcastToAll("users:joined", session.getId());
//        sendMessage(session, "outgoing:call", Map.of("id", session.getId()));
//    }
//
////    @Override
////    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
////        System.out.println("WebSocket connection established: " + session.getId());
////    }
//
//
////    @Override
////    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////        sessions.put(session.getId(), session);
////        logger.info("Received message from session {}: {}", session.getId(), message.getPayload());
////
////        try {
////            // Attempt to parse the message payload as JSON
////            Map<String, Object> data = new ObjectMapper().readValue(message.getPayload(), new TypeReference<Map<String, Object>>() {});
////            String type = (String) data.get("type");
////
////            switch (type) {
////                case "outgoing:call":
////                    handleOutgoingCall(session, data);
////                    break;
////                case "call:accepted":
////                    handleCallAccepted(session, data);
////                    break;
////                case "candidate":
////                    handleCandidate(session, data);
////                    break;
////                default:
////                    logger.warn("Unknown message type received: {}", type);
////                    break;
////            }
////        } catch (JsonParseException | JsonMappingException e) {
////            // Handle JSON parsing errors
////            logger.error("Error parsing JSON message from session {}: {}", session.getId(), e.getMessage());
////            session.close(CloseStatus.BAD_DATA);
////        }
////    }
//@Override
//protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//    String payload = message.getPayload();
//    System.out.println("Received message: " + payload);
//
//    // Handle the incoming message and send a response if necessary
//    session.sendMessage(new TextMessage("Message received: " + payload));
//}
//
//    private void handleCandidate(WebSocketSession session, Map<String, Object> data) throws IOException {
//        Map<String, Object> candidateData = (Map<String, Object>) data.get("candidate");
//        // Process the candidate message here
//        // Example: Forward the candidate message to the appropriate recipient
//        String toUserId = (String) data.get("to");
//        WebSocketSession recipientSession = sessions.get(toUserId);
//        if (recipientSession != null) {
//            sendMessage(recipientSession, "candidate", Map.of("from", session.getId(), "candidate", candidateData));
//        }
//    }
//
////    private void handleOutgoingCall(WebSocketSession session, Map<String, Object> data) throws IOException {
////        String toUserId = (String) data.get("to");
////        String fromOffer = (String) data.get("fromOffer");
////        WebSocketSession recipientSession = sessions.get(toUserId);
////        if (recipientSession != null) {
////            sendMessage(recipientSession, "incoming:call", Map.of("from", session.getId(), "offer", fromOffer));
////        }
////    }
////
////    private void handleCallAccepted(WebSocketSession session, Map<String, Object> data) throws IOException {
////        String toUserId = (String) data.get("to");
////        String answer = (String) data.get("answer");
////        WebSocketSession recipientSession = sessions.get(toUserId);
////        if (recipientSession != null) {
////            sendMessage(recipientSession, "incoming:answer", Map.of("from", session.getId(), "answer", answer));
////        }
////    }
//
////    private void handleOutgoingCall(WebSocketSession session, Map<String, Object> data) throws IOException {
////        String toUserId = (String) data.get("to");
////        String fromOffer = (String) data.get("fromOffer");
////        if (toUserId != null) {
////            WebSocketSession recipientSession = sessions.get(toUserId);
////            if (recipientSession != null) {
////                sendMessage(recipientSession, "incoming:call", Map.of("from", session.getId(), "offer", fromOffer));
////            } else {
////                logger.warn("Recipient session not found for user id: {}", toUserId);
////            }
////        } else {
////            logger.error("Invalid 'to' user id received in outgoing call message.");
////        }
////    }
//private void handleOutgoingCall(WebSocketSession session, Map<String, Object> data) throws IOException {
////    String toUserId = (String) data.get("to");
//    String toUserId = "1";
//    String fromOffer = (String) data.get("fromOffer");
//
//    if (toUserId != null) {
//        WebSocketSession recipientSession = sessions.get(toUserId);
//
//        if (recipientSession != null && recipientSession.isOpen()) {
//            sendMessage(recipientSession, "incoming:call", Map.of("from", session.getId(), "offer", fromOffer));
//        } else {
//            logger.warn("Recipient session not found or closed for user id: {}", toUserId);
//            // Optionally handle this case, e.g., notify the sender that the recipient is not available
//        }
//    } else {
//        logger.error("Invalid 'to' user id received in outgoing call message.");
//        // Optionally handle this case, e.g., notify the sender of an invalid recipient ID
//    }
//}
//
//
//    private void handleCallAccepted(WebSocketSession session, Map<String, Object> data) throws IOException {
//        String toUserId = (String) data.get("to");
//        String answer = (String) data.get("answer");
//        if (toUserId != null) {
//            WebSocketSession recipientSession = sessions.get(toUserId);
//            if (recipientSession != null) {
//                sendMessage(recipientSession, "incoming:answer", Map.of("from", session.getId(), "answer", answer));
//            } else {
//                logger.warn("Recipient session not found for user id: {}", toUserId);
//            }
//        } else {
//            logger.error("Invalid 'to' user id received in call accepted message.");
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session.getId());
//
//        logger.info("Session closed with status: {}", status.toString());
//        broadcastToAll("user:disconnect", session.getId());
//    }
////@Override
////public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
////    System.out.println("WebSocket connection closed: " + session.getId() + " Status: " + status);
////}
//
//
////    private void sendMessage(WebSocketSession session, String type, Map<String, Object> data) throws IOException {
////        if (session.isOpen()) {
////            Map<String, Object> messageData = new HashMap<>(data);
////            messageData.put("type", type);
////            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(messageData)));
////        } else {
////            logger.warn("Trying to send message to a closed session: {}", session.getId());
////            sessions.remove(session.getId());
////        }
////    }
//
//    private void sendMessage(WebSocketSession session, String type, Map<String, Object> data) throws IOException {
//        if (session.isOpen()) {
//            Map<String, Object> messageData = new HashMap<>(data);
//            messageData.put("type", type);
//            String jsonMessage = new ObjectMapper().writeValueAsString(messageData);
//
//            // Use synchronized block to ensure sequential sending
//            synchronized (session) {
//                if (session.isOpen()) {
//                    session.sendMessage(new TextMessage(jsonMessage));
//                } else {
//                    logger.warn("Trying to send message to a closed session: {}", session.getId());
//                    sessions.remove(session.getId());
//                }
//            }
//        } else {
//            logger.warn("Trying to send message to a closed session: {}", session.getId());
//            sessions.remove(session.getId());
//        }
//
//
//
//    }
//
//    private void broadcastToAll(String type, String id) throws IOException {
//        Map<String, Object> data = Map.of("type", type, "id", id);
//        TextMessage message = new TextMessage(new ObjectMapper().writeValueAsString(data));
//
//        for (WebSocketSession session : sessions.values()) {
//            if (session.isOpen()) {
//                session.sendMessage(message);
//            } else {
//                logger.warn("Skipping sending message to closed session: {}", session.getId());
//                sessions.remove(session.getId());
//            }
//        }
//    }
//
////    private void broadcastToAll(String type, String id) throws IOException {
////        Map<String, Object> data = Map.of("type", type, "id", id);
////        TextMessage message = new TextMessage(new ObjectMapper().writeValueAsString(data));
////        for (WebSocketSession session : sessions.values()) {
////            session.sendMessage(message);
////        }
////    }
//}
