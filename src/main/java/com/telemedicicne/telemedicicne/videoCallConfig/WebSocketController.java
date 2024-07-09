//package com.telemedicicne.telemedicicne.videoCallConfig;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketController {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/callUser")
//    public void callUser(@Payload CallPayload data) {
//        System.out.println("Incoming call from " + data.getFrom());
//        messagingTemplate.convertAndSendToUser(data.getUserToCall(), "/topic/callUser", data);
//    }
//
//    @MessageMapping("/answerCall")
//    public void answerCall(@Payload AnswerPayload data) {
//        System.out.println("Answering call from " + data.getFrom());
//        messagingTemplate.convertAndSendToUser(data.getTo(), "/topic/callAccepted", data.getSignal());
//    }
//}
