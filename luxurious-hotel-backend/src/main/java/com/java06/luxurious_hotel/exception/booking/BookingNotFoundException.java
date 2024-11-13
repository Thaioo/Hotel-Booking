package com.java06.luxurious_hotel.exception.booking;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingNotFoundException extends RuntimeException {
    private String message = "Booking not found!";
}
