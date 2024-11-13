package com.java06.luxurious_hotel.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java06.luxurious_hotel.config.RabbitmqConfig;
import com.java06.luxurious_hotel.dto.NotificationDTO;
import com.java06.luxurious_hotel.entity.NotificationEntity;
import com.java06.luxurious_hotel.repository.NotificationRepository;
import com.java06.luxurious_hotel.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @RabbitListener(queues = RabbitmqConfig.NOTIFICATION_QUEUE)
    public void getNotification(String data) throws JsonProcessingException {
        NotificationDTO notificationDTO = objectMapper.readValue(data, NotificationDTO.class);
        //Lưu thông báo vào database
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setTitle(notificationDTO.getTitle());
        notificationEntity.setMessage(notificationDTO.getMessage());
        notificationEntity.setNotificationType(notificationDTO.getType());
        notificationEntity.setCreateTime(notificationDTO.getCreateDate());
        notificationRepository.save(notificationEntity);

        //Lưu gửi thông báo đến websocket
        messagingTemplate.convertAndSend("/topic/notifications", data);
    }
}
