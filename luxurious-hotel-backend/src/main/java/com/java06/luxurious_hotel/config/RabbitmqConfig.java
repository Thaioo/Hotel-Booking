package com.java06.luxurious_hotel.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    final static public String BOOKING_EMAIL_EXCHANGE = "booking-email-exchange";
    final static public String CONFIRM_BOOKING_EMAIL_QUEUE = "confirm-booking-email-queue";
    final static public String CONFIRM_BOOKING_EMAIL_ROUTING_KEY = "/confirm-booking-email";
    final static public String SUCCESS_BOOKING_EMAIL_QUEUE = "success-booking-email-queue";
    final static public String SUCCESS_BOOKING_EMAIL_ROUTING_KEY = "/success-booking-email";

    final static public String NOTIFICATION_EXCHANGE = "notification-exchange";
    final static public String NOTIFICATION_QUEUE = "notification-queue";
    final static public String NOTIFICATION_ROUTING_KEY = "/notification";

    @Bean public Exchange exchangeBookingEmail() { return new TopicExchange(BOOKING_EMAIL_EXCHANGE); }

    @Bean public Exchange exchangeNotification()
    {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue queueConfirmEmail()
    {
        return new Queue(CONFIRM_BOOKING_EMAIL_QUEUE, true);
    }

    @Bean
    public Queue queueSuccessBookingEmail()
    {
        return new Queue(SUCCESS_BOOKING_EMAIL_QUEUE, true);
    }

    @Bean
    public Queue queueNotification()
    {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding confirmBookingBinding()
    {
        return BindingBuilder.bind(queueConfirmEmail())
                .to(exchangeBookingEmail())
                .with(CONFIRM_BOOKING_EMAIL_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Binding successBookingBinding()
    {
        return BindingBuilder.bind(queueSuccessBookingEmail())
                .to(exchangeBookingEmail())
                .with(SUCCESS_BOOKING_EMAIL_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Binding notificationBinding()
    {
        return BindingBuilder.bind(queueNotification())
                .to(exchangeNotification())
                .with(NOTIFICATION_ROUTING_KEY)
                .noargs();
    }
}
