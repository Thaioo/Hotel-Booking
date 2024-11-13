package com.java06.luxurious_hotel.service;

import com.java06.luxurious_hotel.dto.GuestDTO;
import com.java06.luxurious_hotel.entity.UserEntity;
import com.java06.luxurious_hotel.request.AddGuestRequest;
import com.java06.luxurious_hotel.request.UpdateGuestRequest;

import java.util.List;

public interface UserService {

    GuestDTO getGuestInfoByPhone(String phone);

    List<GuestDTO> getListGuest();

    Boolean addGuest(AddGuestRequest addGuestRequest);

    Boolean deleteUser(int idUser);

    Boolean updateUser(UpdateGuestRequest updateGuestRequest);

    GuestDTO getUser(int idUser);

}
