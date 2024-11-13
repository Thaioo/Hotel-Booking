package com.java06.luxurious_hotel.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE }) // Áp dụng cho class
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Check-in date must be before check-out date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // Các trường mà bạn muốn kiểm tra (tên các trường chứa check-in và check-out)
    String checkInField();
    String checkOutField();
}
