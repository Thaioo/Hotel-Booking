package com.java06.luxurious_hotel.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

//2 tham số: 1 là tên validator annotation, 2 là kiểu dữ liệu của FIELD hoặc tên CLASS cần validate
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String checkInField;
    private String checkOutField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.checkInField = constraintAnnotation.checkInField();
        this.checkOutField = constraintAnnotation.checkOutField();

    }

    @SneakyThrows
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

            // Sử dụng reflection để lấy giá trị của các trường checkIn và checkOut
            Field checkInField = object.getClass().getDeclaredField(this.checkInField);
            Field checkOutField = object.getClass().getDeclaredField(this.checkOutField);

            //bỏ qua giới hạn quyền truy cập, cho phép đọc và sửa đổi giá trị của các field private, protected (hoặc package-private) của một đối tượng.
            checkInField.setAccessible(true);
            checkOutField.setAccessible(true);

            try {
                LocalDate checkInDate = LocalDate.parse(checkInField.get(object).toString());
                LocalDate checkOutDate = LocalDate.parse(checkOutField.get(object).toString());

                return checkInDate.isBefore(checkOutDate); // Kiểm tra check-in trước check-out
            } catch (DateTimeParseException e){
                return true; // ko parse được thì có annotation khác bắt định dạng, mỗi annotation chi3 phụ trách 1 nhiệm vụ
            }

    }
}
