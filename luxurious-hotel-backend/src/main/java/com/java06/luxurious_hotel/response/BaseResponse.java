package com.java06.luxurious_hotel.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    private int statusCode = 200;
    private String message;
    private Object data;
}
