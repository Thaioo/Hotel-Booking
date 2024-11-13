package com.java06.luxurious_hotel.service.imp;

import com.java06.luxurious_hotel.dto.GuestDTO;
import com.java06.luxurious_hotel.entity.*;
import com.java06.luxurious_hotel.exception.role.RoleNotFoundException;
import com.java06.luxurious_hotel.exception.user.DuplicateMailOrPhoneException;
import com.java06.luxurious_hotel.exception.user.UserNotFoundException;
import com.java06.luxurious_hotel.repository.*;
import com.java06.luxurious_hotel.request.AddGuestRequest;
import com.java06.luxurious_hotel.request.UpdateGuestRequest;
import com.java06.luxurious_hotel.service.EmailService;
import com.java06.luxurious_hotel.service.UserService;
import com.java06.luxurious_hotel.supportmethod.ParseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceIMP implements UserService {

    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private ParseName parseName;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoomBookingRepository roomBookingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private OrdersRepository ordersRepository;


    @Override
    public GuestDTO getUser(int idUser) {

        // lấy code thông qua idUser
        UserEntity userEntity = userRepository.findById(idUser);

        // khởi tạo DTO
        GuestDTO guestDTO = new GuestDTO();

        // kiểm tra null và gán giá trị vào DTO
        if (userEntity != null) {
            guestDTO.setFullName(userEntity.getFirstName() + " " + userEntity.getLastName());
            guestDTO.setEmail(userEntity.getEmail());
            guestDTO.setPhone(userEntity.getPhone());
            guestDTO.setId(userEntity.getId());
            guestDTO.setDob(userEntity.getDob());
            guestDTO.setAddress(userEntity.getAddress());
            guestDTO.setSummary(userEntity.getSummary());
        }

        return guestDTO;
    }

    @Transactional // đảm bảo phương thức được thực hiện trong 1 giao dịch
    @Override
    public Boolean updateUser(UpdateGuestRequest updateGuestRequest) {

        // khởi tạo đối tượng UserEntity
        UserEntity userEntity = new UserEntity();

        // tạo biến gán giá trị của Guest khi đã update thành công dùng if để trả true false
        UserEntity updatedUser;

        if (updateGuestRequest.image() != null){
            userEntity.setImage(updateGuestRequest.image());
        }else {
            userEntity.setImage("client.PNG");
        }

        userEntity.setId(updateGuestRequest.idGuest());
        String[] names = parseName.parseName(updateGuestRequest.fullName());
        userEntity.setFirstName(names[0]);
        userEntity.setLastName(names[1]);

        userEntity.setDob(updateGuestRequest.dob());
        userEntity.setPhone(updateGuestRequest.phone());
        userEntity.setEmail(updateGuestRequest.email());
        userEntity.setAddress(updateGuestRequest.address());
        userEntity.setSummary(updateGuestRequest.summary());
        userEntity.setRole(roleRepository.findRoleEntitiesById(2));
        userEntity.setDeleted(0);


        try {

            // Lưu người dùng và xử lý trùng lặp
            updatedUser = userRepository.save(userEntity);

        } catch (DataIntegrityViolationException e) {

            // Kiểm tra nguyên nhân gốc của lỗi bằng getCause()
            Throwable cause = e.getCause();

            // Log lỗi để kiểm tra nguyên nhân chi tiết
            System.out.println("Cause: " + (cause != null ? cause.getMessage() : "null"));

            if (cause != null && cause.getMessage().contains("Duplicate entry")) {
                // Nếu nguyên nhân là lỗi duplicate entry
                throw new DuplicateMailOrPhoneException("Duplicate mail or phone number");
            }

            // Bắt thêm lỗi khác nếu có
            throw e;  // Ném lại lỗi nếu không phải do vi phạm ràng buộc
        }

        // kiểm tra update thành công hay chưa để return
        if (updatedUser != null && updatedUser.getId() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public Boolean deleteUser(int idUser) {

        // update status delete guest
        userRepository.resetDeleteStatus(idUser);

        // Lấy lại user để kiểm tra
        UserEntity user = userRepository.findById(idUser);

        // Kiểm tra nếu người dùng tồn tại và trạng thái delete là false (0)
        return user != null && user.getDeleted() == 1;
    }

    @Override
    public GuestDTO getGuestInfoByPhone(String phone) {
        return userRepository.findUserEntityByPhone(phone).stream().map(userEntity -> {
            GuestDTO guestDTO = new GuestDTO();
            guestDTO.setFirstName(userEntity.getFirstName());
            guestDTO.setLastName(userEntity.getLastName());
            guestDTO.setPhone(userEntity.getPhone());
            guestDTO.setAddress(userEntity.getAddress());
            guestDTO.setEmail(userEntity.getEmail());
            return guestDTO;
        }).findFirst().orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<GuestDTO> getListGuest() {

        // lấy list Guest thông qua ROLE
        List<UserEntity> listGuest = userRepository.findByRole_Name("ROLE_GUEST");

        // tạo list GuestDTO để nhận về các giá trị đã truy vấn ở trên
        List<GuestDTO> guestDTOS = new ArrayList<>();

        for (UserEntity user : listGuest) {

            if (user.getDeleted() == 0){

                // khởi tạo và lấy giá trị của từng guest để thêm vào list GuestDTO
                GuestDTO guestDTO = new GuestDTO();
                guestDTO.setId(user.getId());
                guestDTO.setFullName(user.getFirstName() + " " + user.getLastName());
                guestDTO.setPhone(user.getPhone());
                guestDTO.setEmail(user.getEmail());
                guestDTO.setAddress(user.getAddress());
                guestDTO.setSummary(user.getSummary());
                guestDTO.setLinkImage("http://localhost:9999/file/hauchuc/client.PNG");

                // add từng giá trị theo vòng lặp
                guestDTOS.add(guestDTO);
            }
        }

        return guestDTOS;
    }

    @Transactional
    @Override
    public Boolean addGuest(AddGuestRequest addGuestRequest) {

        // khởi tạo đối tượng để nhập vào
        UserEntity userEntity = new UserEntity();

        // save image
        if (addGuestRequest.filePicture() != null){
            filesStorageService.save1(addGuestRequest.filePicture());
            userEntity.setImage(addGuestRequest.filePicture().getOriginalFilename());
        }else {
            userEntity.setImage("client.PNG");
        }

        // tách fullname
        String[] names = parseName.parseName(addGuestRequest.fullName());
        userEntity.setFirstName(names[0]);
        userEntity.setLastName(names[1]);

        // set ngày khởi tạo
        userEntity.setDob(LocalDate.now());

        userEntity.setEmail(addGuestRequest.email());
        userEntity.setPhone(addGuestRequest.phone());

        userEntity.setAddress(addGuestRequest.address());
        userEntity.setSummary(addGuestRequest.summary());

        // Định dạng theo kiểu ngày trong chuỗi
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        userEntity.setDob(LocalDate.parse(addGuestRequest.dob(),formatter));

        // tạo đối tượng roleEntity và gán giá trị Role
        RoleEntity guestRole = roleRepository.findByName("ROLE_GUEST");

        // trả về lỗi nếu role null
        if (guestRole == null){
            throw new RoleNotFoundException();
        }

        // set Role
        userEntity.setRole(guestRole);

        // set status delete
        userEntity.setDeleted(0);

        try {
            userRepository.save(userEntity);
        }catch (Exception e){
            throw new DuplicateMailOrPhoneException("duplicate mail or phone number");
        }

        return userEntity.getId() > 0;
    }

}
