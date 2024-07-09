//package com.telemedicicne.telemedicicne.videoCallConfig;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class WebSocketSignalingController {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public WebSocketSignalingController(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @MessageMapping("/sendOffer")
//    @SendTo("/topic/offer")
//    public String sendOffer(String offer) {
//
//        System.out.println("offer--------"+offer);
//        return offer;
//    }
//
//    @MessageMapping("/sendAnswer")
//    @SendTo("/topic/answer")
//    public String sendAnswer(String answer) {
//        System.out.println("Answer--------"+answer);
//        return answer;
//    }
//
//    @MessageMapping("/sendCandidate")
//    @SendTo("/topic/candidate")
//    public String sendCandidate(String candidate) {
//        System.out.println("Candiate--------- "+candidate);
//        return candidate;
//    }
//}
