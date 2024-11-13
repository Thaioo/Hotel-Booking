package com.java06.luxurious_hotel.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java06.luxurious_hotel.config.RabbitmqConfig;
import com.java06.luxurious_hotel.dto.NotificationDTO;
import com.java06.luxurious_hotel.entity.NotificationEntity;
import com.java06.luxurious_hotel.enumContraints.NotificationType;
import com.java06.luxurious_hotel.repository.NotificationRepository;
import com.java06.luxurious_hotel.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class NotificationServiceImp implements NotificationService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void sendNotification(String title, String message, NotificationType type) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle(title);
        notificationDTO.setMessage(message);
        notificationDTO.setType(type);
        notificationDTO.setCreateDate(LocalDateTime.now());

        //Gửi thông báo đến rabbitmq
        try {
            String json = objectMapper.writeValueAsString(notificationDTO);
            rabbitTemplate.convertAndSend(RabbitmqConfig.NOTIFICATION_EXCHANGE, RabbitmqConfig.NOTIFICATION_ROUTING_KEY, json);
        } catch (Exception e) {
            log.error("Error send notification! Title: " + title + " - exception message: " + e);
        }
    }

    @Override
    public List<NotificationDTO> getNotification(int pageNumber, int pageSize) {
        Pageable sortedByCreateTime =  PageRequest.of(pageNumber, pageSize, Sort.by("createTime").descending());
        List<NotificationDTO> notificationDTOList = notificationRepository.findAll(sortedByCreateTime).stream().map(
                notificationEntity -> {
                    NotificationDTO notificationDTO = new NotificationDTO();
                    notificationDTO.setTitle(notificationEntity.getTitle());
                    notificationDTO.setMessage(notificationEntity.getMessage());
                    notificationDTO.setCreateDate(notificationEntity.getCreateTime());
                    notificationDTO.setType(notificationEntity.getNotificationType());
                    return notificationDTO;
                }
        ).toList();
        return notificationDTOList;
    }
}
