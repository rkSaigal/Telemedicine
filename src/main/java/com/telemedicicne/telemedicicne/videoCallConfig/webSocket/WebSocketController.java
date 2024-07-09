package com.telemedicicne.telemedicicne.videoCallConfig.webSocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mysql.cj.conf.PropertyKey.logger;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, String> emailToSocketIdMap = new ConcurrentHashMap<>();
    private final Map<String, String> socketIdToEmailMap = new ConcurrentHashMap<>();

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/room/join")
    @SendTo("/topic/room")
//    public void joinRoom(RoomJoinMessage message) {
//        String email = message.getEmail();
//        String room = message.getRoom();
//        String socketId = message.getSocketId();
//
//        emailToSocketIdMap.put(email, socketId);
//        socketIdToEmailMap.put(socketId, email);
//
//        messagingTemplate.convertAndSend("/topic/room", new UserJoinedMessage(email, socketId));
//    }
    public void joinRoom(RoomJoinMessage message) {
        String email = message.getEmail();
        String room = message.getRoom();
        String socketId = message.getSocketId();

        emailToSocketIdMap.put(email, socketId);
        socketIdToEmailMap.put(socketId, email);

//        logger.info("User joined: email = {}, socketId = {}", email, socketId);
        messagingTemplate.convertAndSend("/topic/room", new UserJoinedMessage(email, socketId));
    }


    @MessageMapping("/user/call")
    public void userCall(UserCallMessage message) {
        String to = message.getTo();
        String offer = message.getOffer();
        String socketId = emailToSocketIdMap.get(to);

        if (socketId != null) {
            messagingTemplate.convertAndSendToUser(socketId, "/queue/incoming", new IncomingCallMessage(message.getFrom(), offer));
        }
    }

    @MessageMapping("/call/accepted")
    public void callAccepted(CallAcceptedMessage message) {
        String to = message.getTo();
        String ans = message.getAns();
        String socketId = emailToSocketIdMap.get(to);

        if (socketId != null) {
            messagingTemplate.convertAndSendToUser(socketId, "/queue/accepted", new CallAcceptedResponse(message.getFrom(), ans));
        }
    }

//    @MessageMapping("/peer/nego/needed")
//    public void peerNegoNeeded(PeerNegoMessage message) {
//        String to = message.getTo();
//        String offer = message.getOffer();
//        String socketId = emailToSocketIdMap.get(to);
//
//        if (socketId != null) {
//            messagingTemplate.convertAndSendToUser(socketId, "/queue/nego/needed", new PeerNegoMessage(message.getFrom(), offer));
//        }
//    }

    @MessageMapping("/peer/nego/needed")
    public void peerNegoNeeded(PeerNegoMessage message) {
        String to = message.getTo();
        String offer = message.getOffer();
        String socketId = emailToSocketIdMap.get(to);

        if (socketId != null) {
            messagingTemplate.convertAndSendToUser(socketId, "/queue/nego/needed", new PeerNegoMessage(message.getFrom(), to, offer));
        }
    }


    //    @MessageMapping("/peer/nego/done")
//    public void peerNegoDone(PeerNegoDoneMessage message) {
//        String to = message.getTo();
//        String ans = message.getAns();
//        String socketId = emailToSocketIdMap.get(to);
//
//        if (socketId != null) {
//            messagingTemplate.convertAndSendToUser(socketId, "/queue/nego/done", new PeerNegoDoneMessage(message.getFrom(), ans));
//        }
//    }
@MessageMapping("/peer/nego/done")
public void peerNegoDone(PeerNegoDoneMessage message) {
    String to = message.getTo();
    String ans = message.getAns();
    String socketId = emailToSocketIdMap.get(to);

    if (socketId != null) {
        messagingTemplate.convertAndSendToUser(socketId, "/queue/nego/done", new PeerNegoDoneMessage(message.getFrom(), to, ans));
    }
}

}


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class RoomJoinMessage {
    private String email;
    private String room;
    private String socketId;

    // getters and setters
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class UserJoinedMessage {
    private String email;
    private String socketId;

    // constructor, getters, and setters
}


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class UserCallMessage {
    private String from;
    private String to;
    private String offer;

    // getters and setters
}


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class IncomingCallMessage {
    private String from;
    private String offer;

    // constructor, getters, and setters
}


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class CallAcceptedMessage {
    private String from;
    private String to;
    private String ans;

    // getters and setters
}
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class CallAcceptedResponse {
    private String from;
    private String ans;

    // constructor, getters, and setters
}



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class PeerNegoMessage {
    private String from;
    private String to;
    private String offer;

    // getters and setters
}



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class PeerNegoDoneMessage {
    private String from;
    private String to;
    private String ans;

    // getters and setters
}