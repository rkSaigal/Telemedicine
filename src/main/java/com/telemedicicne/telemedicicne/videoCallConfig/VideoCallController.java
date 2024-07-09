//package com.telemedicicne.telemedicicne.videoCallConfig;//package com.telemedicicne.telemedicicne.videoCallConfig;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RequestBody;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RestController;
////
////@RestController
////@RequestMapping("/api/video")
////public class VideoCallController {
////
////    @Autowired
////    private VideoCallService videoCallService;
////
////    @PostMapping("/initiate")
////    public ResponseEntity<String> initiateCall(@RequestBody String doctorEmail) {
////        String callToken = videoCallService.generateCallToken(doctorEmail);
////        return ResponseEntity.ok(callToken);
////    }
////}
//
//
//import com.telemedicicne.telemedicicne.Repository.HealthOfficerRepository;
//import com.telemedicicne.telemedicicne.videoCallConfig.VideoCallService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/video")
//public class VideoCallController {
//
//    @Autowired
//    private VideoCallService videoCallService;
//
//    @Autowired
//    private HealthOfficerRepository docHsRepository;
//
//
//    @PostMapping("/initiate/{docHsId}")
//    public String initiateVideoCall(@PathVariable Long docHsId, @RequestBody String videoToken) {
//        videoCallService.updateVideoToken(docHsId, videoToken);
//        return "Video call initiated successfully";
//    }
//
//    @PostMapping("/end/{docHsId}")
//    public String endVideoCall(@PathVariable Long docHsId) {
//        videoCallService.clearVideoToken(docHsId);
//        return "Video call ended successfully";
//    }
//
//}
