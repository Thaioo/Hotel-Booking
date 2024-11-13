package com.java06.luxurious_hotel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java06.luxurious_hotel.config.RabbitmqConfig;
import com.java06.luxurious_hotel.dto.NotificationDTO;
import com.java06.luxurious_hotel.entity.NotificationEntity;
import com.java06.luxurious_hotel.response.BaseResponse;
import com.java06.luxurious_hotel.service.NotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody NotificationEntity notification) throws JsonProcessingException {
        String jsonNotification = objectMapper.writeValueAsString(notification);
        rabbitTemplate.convertAndSend(RabbitmqConfig.NOTIFICATION_EXCHANGE, RabbitmqConfig.NOTIFICATION_ROUTING_KEY, jsonNotification);
//        template.convertAndSend("/topic/notifications", notification);
        return ResponseEntity.ok(jsonNotification);
    }

    @GetMapping
    public ResponseEntity<?> getNotifications(@RequestParam int pageNumber, @RequestParam int pageSize){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(notificationService.getNotification(pageNumber, pageSize));
        return ResponseEntity.ok(baseResponse);
    }
}
