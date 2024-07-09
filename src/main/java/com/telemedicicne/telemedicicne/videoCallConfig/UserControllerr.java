//package com.telemedicicne.telemedicicne.videoCallConfig;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.socket.WebSocketHandler;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@RestController
//public class UserControllerr {
//
//    private final SignalingHandler webSocketHandler;
//
//    @Autowired
//    public UserControllerr(SignalingHandler webSocketHandler) {
//        this.webSocketHandler = webSocketHandler;
//    }
//
//    @GetMapping("/users")
//    public Set<String> getUsers() {
//        return webSocketHandler.getUsers().keySet().stream().collect(Collectors.toSet());
//    }
//}
