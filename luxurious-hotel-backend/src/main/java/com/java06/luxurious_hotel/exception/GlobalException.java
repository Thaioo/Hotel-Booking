package com.java06.luxurious_hotel.exception;

import com.java06.luxurious_hotel.exception.Employee.EmployeeNotExitsException;
import com.java06.luxurious_hotel.exception.authen.TokenExpirationException;
import com.java06.luxurious_hotel.exception.booking.BookingNotFoundException;
import com.java06.luxurious_hotel.exception.room.RoomNotAvailableException;
import com.java06.luxurious_hotel.exception.room.RoomNotFoundException;
import com.java06.luxurious_hotel.exception.roomType.AmentityNotFoundException;
import com.java06.luxurious_hotel.exception.roomType.RoomTypeNotFoundException;
import com.java06.luxurious_hotel.exception.user.IncorrectPasswordException;
import com.java06.luxurious_hotel.exception.user.UserNotFoundException;
import com.java06.luxurious_hotel.response.BaseResponse;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler({TokenExpirationException.class, JwtException.class, SignatureException.class})
    public ResponseEntity<?> tokenExeption(Exception e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage(e.getMessage()); // Thông báo lỗi hết hạn token

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(Exception e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(500);
        baseResponse.setMessage(e.getMessage() + " class: " + e.getClass()
        );
        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    //Bắt các lỗi về chứng thực
//    @ExceptionHandler(value = AccessDeniedException.class)
//    public ResponseEntity<?> handlingAccessDeniedException(AccessDeniedException e) {
//        BaseResponse baseResponse = new BaseResponse();
//        baseResponse.setStatusCode(500);
//        baseResponse.setMessage(e.getMessage());
//
//        return new ResponseEntity<>(baseResponse, HttpStatus.UNAUTHORIZED);
//    }

//    @ExceptionHandler(TokenInvalidException.class)
//    public ResponseEntity<?> handleTokenInValid(TokenInvalidException e) {
//        BaseResponse baseResponse = new BaseResponse();
//        baseResponse.setStatusCode(500);
//        baseResponse.setMessage(e.getMessage());
//        return new ResponseEntity<>(baseResponse, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handeValidation(MethodArgumentNotValidException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(500);
        baseResponse.setMessage(e.getAllErrors().getFirst().getDefaultMessage());

        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(500);
        baseResponse.setMessage(e.getLocalizedMessage());

        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Bắt exception trùng dữ liệu unique          --> cho e ké bắt dublicate data này tí, tạo 2 hàm trùng DataIntegrityViolationException bị lỗi
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handeValidation(DataIntegrityViolationException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(500);
        //        baseResponse.setMessage(e.getRootCause().getMessage());

        // Kiểm tra nguyên nhân gốc
        Throwable rootCause = e.getRootCause();
        if (rootCause != null && rootCause.getMessage().contains("Duplicate entry")) {
            baseResponse.setMessage("Duplicate mail or phone number");
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } else {
            baseResponse.setMessage(e.getRootCause().getMessage());
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }


//        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

//    Handle Exception ROOM ------------------------------------------------------------------------

    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<?> handleRoomNotAvailableException(RoomNotAvailableException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }


    @ExceptionHandler(RoomTypeNotFoundException.class)
    public ResponseEntity<?> handleRoomTypeNotFoundException(RoomTypeNotFoundException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @ExceptionHandler(RoomNotFoundException.class)

    public ResponseEntity<?> handleRoomNotAvailableException(RoomNotFoundException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    //    Handle Exception BOOKING ------------------------------------------------------------------------
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<?> handleBookingNotFoundException(Exception e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    //    Handle Exception USER ------------------------------------------------------------------------
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(Exception e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handleIncorrectPasswordException(IncorrectPasswordException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

//    Handle Exception AMENITY ------------------------------------------------------------------------

    @ExceptionHandler(AmentityNotFoundException.class)
    public ResponseEntity<?> handleAmentityNotFoundException(AmentityNotFoundException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
//    Handle Exception EMPLOYEE ------------------------------------------------------------------------
    @ExceptionHandler(EmployeeNotExitsException.class)
    public ResponseEntity<?> EmployeeNotExitsException(Exception e) {
         BaseResponse baseResponse = new BaseResponse();
         baseResponse.setStatusCode(200);
         baseResponse.setMessage(e.getMessage());
         return new ResponseEntity<>(baseResponse, HttpStatus.OK);
}
}

