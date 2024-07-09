package com.telemedicicne.telemedicicne.videoCallConfig;//package com.telemedicicne.telemedicicne.videoCallConfig;//package com.telemedicicne.telemedicicne.videoCallConfig;//package com.telemedicicne.telemedicicne.videoCallConfig;//package com.telemedicicne.telemedicicne.videoCallConfig;
////////
////////
////////
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.*;
//import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
//
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
////    @Override
////    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
////        registry.addHandler(new SignalingHandler(), "/ws").setAllowedOrigins("*");
////    }
//@Override
//public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//    registry.addHandler(new SignalingHandler(), "/ws")
//            .setAllowedOrigins("*");
//}
//
////    @Override
////    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
////        registry.addHandler(new SignalingHandler(), "/ws").setAllowedOrigins("*");
////    }
//
//
//}

//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic"); // Enables a simple in-memory broker
//        config.setApplicationDestinationPrefixes("/app"); // Defines the prefix for client messages
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS(); // WebSocket endpoint
//        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS().setSuppressCors(true); // Secure WebSocket endpoint (WSS)
//    }
//}

//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
////    @Override
////    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
////    }
////
////    @Override
////    public void configureMessageBroker(MessageBrokerRegistry registry) {
////        registry.setApplicationDestinationPrefixes("/app");
////        registry.enableSimpleBroker("/topic");
////    }
//@Override
//public void registerStompEndpoints(StompEndpointRegistry registry) {
//    registry.addEndpoint("/ws")
//            .setAllowedOrigins("*")
//            .withSockJS();  // Enable SockJS fallback for browsers that don't support WebSocket
//}
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.enableSimpleBroker("/topic");
//    }
//}
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .setAllowedOrigins("*")
//                .withSockJS();  // Enable SockJS fallback for browsers that don't support WebSocket
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.enableSimpleBroker("/topic");
//    }
//}
