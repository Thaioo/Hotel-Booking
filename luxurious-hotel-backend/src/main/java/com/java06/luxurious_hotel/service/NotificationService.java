package com.java06.luxurious_hotel.service;

import com.java06.luxurious_hotel.dto.NotificationDTO;
import com.java06.luxurious_hotel.entity.NotificationEntity;
import com.java06.luxurious_hotel.enumContraints.NotificationType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    public void sendNotification(String title, String message, NotificationType type);
    public List<NotificationDTO> getNotification(int pageNumber, int pageSize);
}
