//package com.telemedicicne.telemedicicne.videoCallConfig;//package com.telemedicicne.telemedicicne.videoCallConfig;//package com.telemedicicne.telemedicicne.videoCallConfig;
//////
//////import com.telemedicicne.telemedicicne.Security.JwtHelper;
//////import org.springframework.beans.factory.annotation.Autowired;
//////import org.springframework.stereotype.Service;
//////
//////@Service
//////public class VideoCallService {
//////
//////    @Autowired
//////    private JwtHelper jwtHelper;
//////
//////    public VideoCall initiateCall(VideoCallRequest request) {
//////        // Validate JWT token
//////        if (!jwtHelper.validateToken(request.getToken(), request.getCallerUsername())) {
//////            throw new IllegalArgumentException("Invalid JWT token");
//////        }
//////
//////        // Retrieve caller and callee information from request
//////        String callerUsername = request.getCallerUsername();
//////        String calleeUsername = request.getCalleeUsername();
//////
//////        // Generate a unique session ID for the video call (you can use UUID.randomUUID() for this)
//////        String sessionId = generateSessionId();
//////
//////        // Example: Persist the session ID in a database for session management
//////
//////        // Example: Notify the callee about the incoming call (using WebSocket or another notification mechanism)
//////        notifyCalleeAboutCall(calleeUsername, sessionId);
//////
//////        // Example: Return a new VideoCall object with session details
//////        return new VideoCall(sessionId, callerUsername, calleeUsername);
//////    }
//////
//////    private String generateSessionId() {
//////        // Example: Use UUID.randomUUID() to generate a unique session ID
//////        return UUID.randomUUID().toString();
//////    }
//////
//////    private void notifyCalleeAboutCall(String calleeUsername, String sessionId) {
//////        // Example: Implement notification logic using WebSocket or push notification
//////        // This could notify the callee about the incoming call
//////        // Example implementation using SimpMessagingTemplate (WebSocket)
//////        simpMessagingTemplate.convertAndSendToUser(
//////                calleeUsername,
//////                "/queue/video-calls",
//////                new VideoCallNotification(sessionId, "Incoming video call from " + callerUsername)
//////        );
//////    }
//////
//////    // Other methods like acceptCall(), endCall() can be implemented similarly
//////
//////}
//////
////
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.messaging.simp.SimpMessagingTemplate;
////import org.springframework.stereotype.Service;
////
////import java.util.UUID;
////
////@Service
////public class VideoCallService {
////
////    @Autowired
////    private SimpMessagingTemplate messagingTemplate;
////
////    public String generateCallToken(String doctorEmail) {
////        String callToken = UUID.randomUUID().toString();
////        messagingTemplate.convertAndSendToUser(doctorEmail, "/queue/call-token", callToken);
////        return callToken;
////    }
////}
//
//
//import com.telemedicicne.telemedicicne.Entity.HealthOfficer;
//import com.telemedicicne.telemedicicne.Repository.HealthOfficerRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class VideoCallService {
//
//    @Autowired
//    private HealthOfficerRepository docHsRepository;
//
//    public void updateVideoToken(Long docHsId, String videoToken) {
//        HealthOfficer docHs = docHsRepository.findById(docHsId)
//                .orElseThrow(() -> new EntityNotFoundException("HealthOfficer not found with id: " + docHsId));
//
//        docHs.setVideoToken(videoToken);
//        docHsRepository.save(docHs);
//    }
//
//    public void clearVideoToken(Long docHsId) {
//        HealthOfficer docHs = docHsRepository.findById(docHsId)
//                .orElseThrow(() -> new EntityNotFoundException("HealthOfficer not found with id: " + docHsId));
//
//        docHs.setVideoToken(null); // Clear the videoToken
//        docHsRepository.save(docHs);
//    }
//}
