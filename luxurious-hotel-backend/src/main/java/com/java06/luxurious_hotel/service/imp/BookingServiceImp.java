package com.java06.luxurious_hotel.service.imp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.java06.luxurious_hotel.config.RabbitmqConfig;
import com.java06.luxurious_hotel.dto.*;
import com.java06.luxurious_hotel.dto.coverdto.RoomsDTO;

import com.java06.luxurious_hotel.entity.*;
import com.java06.luxurious_hotel.enumContraints.NotificationType;
import com.java06.luxurious_hotel.exception.booking.BookingNotFoundException;
import com.java06.luxurious_hotel.exception.room.RoomNotAvailableException;
import com.java06.luxurious_hotel.exception.room.RoomNotFoundException;
import com.java06.luxurious_hotel.repository.BookingRepository;
import com.java06.luxurious_hotel.repository.RoomBookingRepository;
import com.java06.luxurious_hotel.repository.RoomRepository;
import com.java06.luxurious_hotel.repository.UserRepository;
import com.java06.luxurious_hotel.request.AddBookingRequest;
import com.java06.luxurious_hotel.request.ConfirmBookingRequest;
import com.java06.luxurious_hotel.request.UpdateBookingRequest;
import com.java06.luxurious_hotel.service.BookingService;
import com.java06.luxurious_hotel.service.EmailService;
import com.java06.luxurious_hotel.service.NotificationService;
import com.java06.luxurious_hotel.utils.JwtUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImp implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomBookingRepository roomBookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Override
    public List<BookingDTO> getBookingByPhone(String phone) {
        List<BookingEntity> bookings = bookingRepository.findByGuest_Phone(phone);
        return bookings.stream().map(this::convertBookingEntityToBookingDTO).toList();
    }

    @Override
    public List<BookingDTO> getAllBooking() {
        return bookingRepository.findAll().stream().map(this::convertBookingEntityToBookingDTO).toList();
    }

    @Override
    public BookingDTO getDetailBooking(int idBooking) {
        return bookingRepository.findById(idBooking).stream().map(this::convertBookingEntityToBookingDTO)
                .findFirst().orElseThrow(BookingNotFoundException::new);
    }

    @Transactional
    @Override
    public BookingDTO addNewBooking(AddBookingRequest request) {
            /*
        1. Kiểm tra các phòng có available trong khoảng tgian checkIn checkOut hay không
        2. Kiểm tra thông xem khách đó đã book trước đó hay chưa thông qua sdt
        1. Thêm khách booking vào table user
        2. Thêm booking
        3. Thêm dữ liệu vào bảng room_booking
     * */

        //KIỂM TRA PHÒNG CÓ AVAILABLE
        if (request.idBookingStatus() > 1) { //chỉ cần ktra những booking xác nhận đặt phòng
            LocalDateTime inDate = LocalDate.parse(request.checkInDate()).atStartOfDay();
            LocalDateTime outDate = LocalDate.parse(request.checkOutDate()).atStartOfDay();

            var notAvailableRoom = this.checkAvailableRoom(inDate, outDate, request.rooms());
            if (notAvailableRoom.size() > 0) {
                throw new RoomNotAvailableException("Rooms are not available " + notAvailableRoom);
            }
        }


        //THÊM KHÁCH HÀNG VÀO TABLE USER
        UserEntity userEntity = new UserEntity();

        //Kiểm tra xem guest đã đặt phòng trước đó hay chưa thông qua sdt
        var optional = userRepository.findUserEntityByPhone(request.phone());
        if (optional.isPresent()) {
            userEntity = optional.get();
        }

        //Cập nhật lại thông tin guest
        userEntity.setFirstName(request.firstName());
        userEntity.setLastName(request.lastName());
        userEntity.setPhone(request.phone());
        userEntity.setEmail(request.email());
        userEntity.setAddress(request.address());
        RoleEntity role = new RoleEntity();
        role.setId(1);
        userEntity.setRole(role);

        UserEntity newGuest = userRepository.save(userEntity);

        //2. Thêm MỚI BOOKING
        BookingEntity newBooking = new BookingEntity();

        newBooking.setCheckIn(LocalDate.parse(request.checkInDate()).atStartOfDay());
        newBooking.setCheckOut(LocalDate.parse(request.checkOutDate()).atStartOfDay());
        newBooking.setRoomNumber(request.roomNumber());

        newBooking.setGuest(newGuest);
        newBooking.setAdultNumber(request.adultNumber());
        newBooking.setChildrenNumber(request.childrenNumber());

        PaymentMethodEntity paymentMethod = new PaymentMethodEntity();
        paymentMethod.setId(request.idPayment());
        newBooking.setPaymentMethod(paymentMethod);

        PaymentStatusEntity paymentStatus = new PaymentStatusEntity();
        if (request.idPaymentStatus() == 0) {
            paymentStatus.setId(1);
        } else {
            paymentStatus.setId(request.idPaymentStatus());
        }
        newBooking.setPaymentStatus(paymentStatus);

        BookingStatusEntity bookingStatus = new BookingStatusEntity();
        if (request.idBookingStatus() == 0) {
            bookingStatus.setId(1);
        } else {
            bookingStatus.setId(request.idBookingStatus());
        }
        newBooking.setBookingStatus(bookingStatus);

        newBooking.setCreateDate(LocalDateTime.now());

        newBooking.setPaidAmount(request.paidAmount());
        newBooking.setTotal(request.total());

        BookingEntity insertedBooking = bookingRepository.save(newBooking);

        //THÊM DỮ LIỆU VÀO BẢNG ROOM_BOOKING
        List<RoomBookingEntity> roomBookingEntityList = this.insertRoomBooking(request.rooms(), insertedBooking);

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(insertedBooking.getId());

        //Gửi notification
        notificationService.sendNotification(
                "New booking (ID: "+insertedBooking.getId()+")",
                "Room " + request.roomName() + " have been booked by " + userEntity.getEmail(),
                NotificationType.ALERT
        );

        return bookingDTO;
    }

    @Override
    public void confirmBooking(ConfirmBookingRequest request) {
        BookingEntity booking = bookingRepository.findById(request.id())
                .orElseThrow(BookingNotFoundException::new);

        //Kiểm tra nếu khách hàng đã confirm rồi thì khồng cần confirm nữa
        if (booking.getBookingStatus().getId() != 1) return;

        //Kiểm tra xem token xác nhận đã hết hạn hay chưa
        int idBooking = Integer.parseInt(jwtUtils.verifyConfirmToken(request.token()));

        if (idBooking != request.id()) {
            throw new RuntimeException("The idBooking inside the token does not match the idBooking that needs to be confirmed.");
        }

        //Kiểm tra lại trong thời gian confirm đã có khách nào đặt trùng phòng hay không
        LocalDateTime inDate = booking.getCheckIn();
        LocalDateTime outDate = booking.getCheckOut();
        List<String> rooms = booking.getRoomBookings().stream().map(roomBookingEntity ->
                String.valueOf(roomBookingEntity.getRoom().getId())).toList();
        var notAvailableRoom = this.checkAvailableRoom(inDate, outDate, rooms);
        if (notAvailableRoom.size() > 0) {
            throw new RoomNotAvailableException("Rooms are not available: " + notAvailableRoom);
        }

        //Cập nhật booking status thành Confirmed
        BookingStatusEntity bookingStatusEntity = new BookingStatusEntity();
        bookingStatusEntity.setId(2);
        booking.setBookingStatus(bookingStatusEntity);
        BookingEntity bookingEntity = bookingRepository.save(booking);

        //Gửi thông tin booking lên rabbitmq để gửi email xác nhận booking thành công
        BookingDTO bookingDTO = this.convertBookingEntityToBookingDTO(bookingEntity);
        emailService.sendSuccessfulBookingEmailToQueue(bookingDTO);

        //Gửi notification lên rabbitmq
        notificationService.sendNotification(
                "Confirmed booking (ID: "+bookingEntity.getId()+")",
                "Booking have been confirmed successfully!",
                NotificationType.SUCCESS
        );
    }

    @Transactional
    @Override
    public void updateBooking(UpdateBookingRequest request) {
        BookingEntity updateBooking = bookingRepository.findById(request.idBooking())
                .orElseThrow(BookingNotFoundException::new);

        //CẬP NHẬT BẢNG ROOM_BOOKING
        //1. Xóa các dòng có id_booking == updateBooking
        roomBookingRepository.deleteByBooking(updateBooking);

//        //KIỂM TRA PHÒNG CÓ ĐANG AVAILABLE
//        LocalDateTime inDate = LocalDate.parse(request.checkInDate()).atStartOfDay();
//        LocalDateTime outDate = LocalDate.parse(request.checkOutDate()).atStartOfDay();
//
//        var notAvailableRoom = this.checkAvailableRoom(inDate, outDate, request.rooms());
//        if (notAvailableRoom.size() > 0) {
//            throw new RoomNotAvailableException("Rooms are not available " + notAvailableRoom);
//        }

        //Update lại các phòng mới
        this.insertRoomBooking(request.rooms(), updateBooking);

        //CẬP NHẬT BẢNG BOOKING
        updateBooking.setCheckIn(LocalDate.parse(request.checkInDate()).atStartOfDay());
        updateBooking.setCheckOut(LocalDate.parse(request.checkOutDate()).atStartOfDay());
        updateBooking.setRoomNumber(request.roomNumber());
        updateBooking.setAdultNumber(request.adultNumber());
        updateBooking.setChildrenNumber(request.childrenNumber());

        PaymentMethodEntity paymentMethod = new PaymentMethodEntity();
        paymentMethod.setId(request.idPayment());
        updateBooking.setPaymentMethod(paymentMethod);

        PaymentStatusEntity paymentStatus = new PaymentStatusEntity();
        paymentStatus.setId(request.idPaymentStatus());
        updateBooking.setPaymentStatus(paymentStatus);

        BookingStatusEntity bookingStatus = new BookingStatusEntity();
        bookingStatus.setId(request.idBookingStatus());
        updateBooking.setBookingStatus(bookingStatus);

        UserEntity guest = new UserEntity();
        guest.setId(request.idGuest());
        updateBooking.setGuest(guest);

        updateBooking.setPaidAmount(request.paidAmount());
        updateBooking.setTotal(request.total());

        bookingRepository.save(updateBooking);
        //Gửi notification
        notificationService.sendNotification(
                "Booking updated(ID: "+updateBooking.getId()+")",
                "Booking " + updateBooking.getId() + " have been updated successfully!",
                NotificationType.SUCCESS
        );
    }

    @Transactional
    @Override
    public void deleteBooking(int idBooking) {
        BookingEntity delBooking = new BookingEntity();
        delBooking.setId(idBooking);
        if (roomBookingRepository.deleteByBooking(delBooking) <= 0) {
            throw new BookingNotFoundException();
        } else {
            bookingRepository.delete(delBooking);
        }
    }

    private List<RoomBookingEntity> insertRoomBooking(List<String> strRooms, BookingEntity booking) {
        //Lấy danh sách room từ list roomId
        List<RoomBookingEntity> rooms = strRooms.stream().map(item -> {
            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setId(Integer.parseInt(item));

            RoomBookingEntity roomBooking = new RoomBookingEntity();
            roomBooking.setRoom(roomEntity);
            roomBooking.setBooking(booking);

            return roomBooking;
        }).toList();
        try {
            return roomBookingRepository.saveAll(rooms);
        } catch (Exception e) {
            throw new RoomNotFoundException(e.getCause().getMessage());
        }
    }


    @Override
    public List<BookingGuestDTO> getListBooking(int idGuest) {

        List<BookingGuestDTO> bookingGuestDTOS = new ArrayList<>();
        UserEntity userEntity = userRepository.findById(idGuest);

        // Add phần tử UserEntity vào đối tượng GuestDTO của BookingGuestDTO
        GuestDTO guestDTO = new GuestDTO();

        guestDTO.setId(userEntity.getId());

        guestDTO.setLinkImage("http://localhost:9999/file/hauchuc/" + userEntity.getImage());

        guestDTO.setFullName(userEntity.getFirstName() + " " + userEntity.getLastName());
        guestDTO.setEmail(userEntity.getEmail());
        guestDTO.setPhone(userEntity.getPhone());
        guestDTO.setAddress(userEntity.getAddress());
        guestDTO.setSummary(userEntity.getSummary());

        if (userEntity.getBookings().size() > 0){
            userEntity.getBookings().stream().map(bookingEntity -> {

                // khởi tạo đối tượng DTO để nhận giá trị
                BookingGuestDTO bookingGuestDTO = new BookingGuestDTO();

                bookingGuestDTO.setGuestDTO(guestDTO);

                bookingGuestDTO.setIdBooking(bookingEntity.getId());
                bookingGuestDTO.setCheckIn(bookingEntity.getCheckIn());
                bookingGuestDTO.setCheckOut(bookingEntity.getCheckOut());
                bookingGuestDTO.setPaymentStatus(bookingEntity.getPaymentStatus().getName());
                bookingGuestDTO.setPaymentMethod(bookingEntity.getPaymentMethod().getName());
                bookingGuestDTO.setMember(bookingEntity.getAdultNumber() + bookingEntity.getChildrenNumber());
                bookingGuestDTO.setQuantilyRoom(bookingEntity.getRoomNumber());
                bookingGuestDTO.setAmount(bookingEntity.getPaidAmount());
                bookingEntity.getRoomBookings().stream().map(roomBookingEntity -> {

                    // khởi tạo 1 map để nhóm các phòng theo loại phòng
                    Map<String, RoomsDTO> roomTypeMap = new HashMap<>();

                    // Lặp qua các RoomBookingEntity để nhóm phòng lại theo loại phòng
                    for (RoomBookingEntity roomBooking : bookingEntity.getRoomBookings()) {

                        // nhận name của roomtype
                        String roomTypeName = roomBooking.getRoom().getRoomType().getName();

                        // tìm roomsDTO theo name roomtype được lấy ở trên
                        RoomsDTO roomsDTO = roomTypeMap.get(roomTypeName);

                        // kiểm tra giá trị roomsDTO có tồn tại hay không nếu null thì khởi tạo
                        if (roomsDTO == null) {
                            roomsDTO = new RoomsDTO();
                            roomsDTO.setNameRoomType(roomTypeName);
                            roomsDTO.setRoomNumber(new ArrayList<>());
                            roomTypeMap.put(roomTypeName, roomsDTO);
                        }

                        roomsDTO.getRoomNumber().add(roomBooking.getRoom().getName());

                    }
                    bookingGuestDTO.setRoomsDTO(new ArrayList<>(roomTypeMap.values()));
                    return  bookingEntity;
                }).toList();

                bookingGuestDTOS.add(bookingGuestDTO);

                return bookingGuestDTO;
            }).toList();
        }else {
            // khởi tạo đối tượng DTO để nhận giá trị
            BookingGuestDTO bookingGuestDTO = new BookingGuestDTO();

            bookingGuestDTO.setGuestDTO(guestDTO);

            bookingGuestDTOS.add(bookingGuestDTO);
        }

        return bookingGuestDTOS;

//        // lấy danh sách trả về từ câu qr
//        List<Object[]> results = bookingRepository.findByGuest_Id(idGuest);
//
//        // tạo list DTO trả ra
//        List<BookingGuestDTO> bookingGuestDTOS = new ArrayList<>();
//
//
//        // chạy vòng for qua tất cả các luồng Objects được trả ra từ câu qr
//        for (Object[] result : results) {
//            List<String> listBedType = new ArrayList<>();
//            // khởi tạo BookingEntity để nhận dữ liệu trả ra từ Object
//            BookingEntity booking = (BookingEntity) result[0];
//
//            // khởi tạo đối tượng DTO để nhận giá trị
//            BookingGuestDTO bookingGuestDTO = new BookingGuestDTO();
//
//
//            // Add phần tử UserEntity vào đối tượng GuestDTO của BookingGuestDTO
//            GuestDTO guestDTO = new GuestDTO();
//
//            guestDTO.setId(booking.getGuest().getId());
//
//            guestDTO.setFullName(booking.getGuest().getFirstName() + " " + booking.getGuest().getLastName());
//            guestDTO.setEmail(booking.getGuest().getEmail());
//            guestDTO.setPhone(booking.getGuest().getPhone());
//            guestDTO.setAddress(booking.getGuest().getAddress());
//            guestDTO.setSummary(booking.getGuest().getSummary());
//
//            bookingGuestDTO.setGuestDTO(guestDTO);
//
//            if (booking.getId() > 0){
//                booking.getRoomBookings().stream().map(roomBookingEntity -> {
//                    boolean check = false;
//                    for (int i = 0; i<listBedType.size(); i++) {
//                        if (listBedType.get(i).equals(roomBookingEntity.getRoom().getRoomType().getBedType().getName())) {
//                            check = true;
//                        }
//                    }
//                    if (check == false){
//                        listBedType.add(roomBookingEntity.getRoom().getRoomType().getBedType().getName());
//                    }
//                    return roomBookingEntity;
//                }).toList();
//
//                // Add các phần tử khác vào BookingGuestDTO
//                bookingGuestDTO.setIdBooking(booking.getId());
//                bookingGuestDTO.setCheckIn(booking.getCheckIn());
//                bookingGuestDTO.setCheckOut(booking.getCheckOut());
//                bookingGuestDTO.setPaymentStatus(booking.getPaymentStatus().getName());
//                bookingGuestDTO.setPaymentMethod(booking.getPaymentMethod().getName());
//                bookingGuestDTO.setMember(booking.getAdultNumber() + booking.getChildrenNumber());
//                bookingGuestDTO.setQuantilyRoom(booking.getRoomNumber());
//                bookingGuestDTO.setBedType(listBedType);
//                bookingGuestDTO.setAmount(booking.getTotal());
//
//                // khởi tạo 1 map để nhóm các phòng theo loại phòng
//                Map<String, RoomsDTO> roomTypeMap = new HashMap<>();
//
//                // Lặp qua các RoomBookingEntity để nhóm phòng lại theo loại phòng
//                for (RoomBookingEntity roomBooking : booking.getRoomBookings()) {
//
//                    // nhận name của roomtype
//                    String roomTypeName = roomBooking.getRoom().getRoomType().getName();
//
//                    // tìm roomsDTO theo name roomtype được lấy ở trên
//                    RoomsDTO roomsDTO = roomTypeMap.get(roomTypeName);
//
//                    // kiểm tra giá trị roomsDTO có tồn tại hay không nếu null thì khởi tạo
//                    if (roomsDTO == null) {
//                        roomsDTO = new RoomsDTO();
//                        roomsDTO.setNameRoomType(roomTypeName);
//                        roomsDTO.setRoomNumber(new ArrayList<>());
//                        roomTypeMap.put(roomTypeName, roomsDTO);
//                    }
//
//                    roomsDTO.getRoomNumber().add(roomBooking.getRoom().getName());
//                }
//
//                bookingGuestDTO.setRoomsDTO(new ArrayList<>(roomTypeMap.values()));
//            }
//            bookingGuestDTOS.add(bookingGuestDTO);
//        }
//        return bookingGuestDTOS;
    }

    private List<String> checkAvailableRoom(LocalDateTime inDate, LocalDateTime outDate, List<String> bookRoomId) {

        List<String> notAvalableRooms = new ArrayList<>();

        //Lấy ra những room đã được booking trong khoảng tgian khách chọn
        List<RoomEntity> bookedRooms = roomRepository.findBookedRoomsByDateRange(inDate, outDate);
        for (RoomEntity bookedRoom : bookedRooms) {
            if (bookRoomId.contains("" + bookedRoom.getId())) {
                notAvalableRooms.add(bookedRoom.getName());
            }
        }
        return notAvalableRooms;
    }

    private BookingDTO convertBookingEntityToBookingDTO(BookingEntity booking) {

        if (booking == null) return null;

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setIdGuest(booking.getGuest().getId());
        bookingDTO.setFirstName(booking.getGuest().getFirstName());
        bookingDTO.setLastName(booking.getGuest().getLastName());
        bookingDTO.setEmail(booking.getGuest().getEmail());
        bookingDTO.setPhone(booking.getGuest().getPhone());
        bookingDTO.setAddress(booking.getGuest().getAddress());
        bookingDTO.setCheckIn(booking.getCheckIn().toLocalDate());
        bookingDTO.setCheckOut(booking.getCheckOut().toLocalDate());
        bookingDTO.setCreateDate(booking.getCreateDate().toLocalDate());


        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setId(booking.getPaymentMethod().getId());
        paymentMethodDTO.setName(booking.getPaymentMethod().getName());
        bookingDTO.setPaymentMethod(paymentMethodDTO);

        PaymentStatusDTO paymentStatusDTO = new PaymentStatusDTO();
        paymentStatusDTO.setId(booking.getPaymentStatus().getId());
        paymentStatusDTO.setName(booking.getPaymentStatus().getName());
        bookingDTO.setPaymentStatus(paymentStatusDTO);

        BookingStatusDTO bookingStatusDTO = new BookingStatusDTO();
        bookingStatusDTO.setId(booking.getBookingStatus().getId());
        bookingStatusDTO.setName(booking.getBookingStatus().getName());
        bookingDTO.setBookingStatus(bookingStatusDTO);

        bookingDTO.setPaidAmount(booking.getPaidAmount());
        bookingDTO.setTotal(booking.getTotal());
        bookingDTO.setAdultNo(booking.getAdultNumber());
        bookingDTO.setChildrenNo(booking.getChildrenNumber());

        bookingDTO.setRoomTypes(booking.getRoomBookings().stream().collect(Collectors.groupingBy(
                roomBookingEntity -> roomBookingEntity.getRoom().getRoomType().getName(),
                Collectors.mapping(roomBookingEntity -> {
                    RoomDTO roomDTO = new RoomDTO();
                    roomDTO.setId(roomBookingEntity.getRoom().getId());
                    roomDTO.setName(roomBookingEntity.getRoom().getName());
                    return roomDTO;
                }, Collectors.toList())
        )));

        return bookingDTO;
    }

}
