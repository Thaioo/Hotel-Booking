package com.java06.luxurious_hotel.service.imp;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java06.luxurious_hotel.dto.RoomTypeDTO;
import com.java06.luxurious_hotel.entity.*;
import com.java06.luxurious_hotel.entity.keys.RoomAmenityKey;
import com.java06.luxurious_hotel.repository.AmenityRepository;
import com.java06.luxurious_hotel.repository.RoomAmenityRepository;
import com.java06.luxurious_hotel.repository.RoomRepository;
import com.java06.luxurious_hotel.repository.RoomTypeRepository;
import com.java06.luxurious_hotel.request.AddRoomtypeRequest;
import com.java06.luxurious_hotel.request.UpdateRoomtypeRequest;
import com.java06.luxurious_hotel.service.FilesStorageService;
import com.java06.luxurious_hotel.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Value("${key.list.roomtype}")
    private String listRoomTypeKey;
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private RoomAmenityRepository roomAmenityRepository;

    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public void addRoomType(AddRoomtypeRequest addRoomtypeRequest) {
        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
        roomTypeEntity.setName(addRoomtypeRequest.name());
        roomTypeEntity.setOverview(addRoomtypeRequest.overview());
        roomTypeEntity.setPrice(addRoomtypeRequest.price());
        roomTypeEntity.setArea(addRoomtypeRequest.area());
        roomTypeEntity.setCapacity(addRoomtypeRequest.capacity());

        BedTypeEntity bedTypeEntity = new BedTypeEntity();
        bedTypeEntity.setId(addRoomtypeRequest.iDBedType());

        roomTypeEntity.setBedType(bedTypeEntity);


        // lấy mọt list request image rồi dùng streamAPI đổi thành String rồi đưa vào database
        List<MultipartFile> listMFiles = addRoomtypeRequest.images();
        if (listMFiles != null && !listMFiles.isEmpty()) {
            String imagesString = addRoomtypeRequest.images().stream()
                    .map(MultipartFile::getOriginalFilename) // Extract the file names
                    .collect(Collectors.joining(","));
            roomTypeEntity.setImage(imagesString);
        }


        RoomTypeEntity roomType = roomTypeRepository.save(roomTypeEntity);
        if (roomType != null) {
            redisTemplate.delete(listRoomTypeKey);
        }

        for (MultipartFile multipartFile : listMFiles) {
            filesStorageService.save(multipartFile);
        }

        addAmenitiesToRoomType(roomType.getId(), addRoomtypeRequest.idAmenity());




    }

    @Transactional
    @Override
    public boolean updateRoomType(UpdateRoomtypeRequest updateRoomtypeRequest) {

        boolean result = false;


        System.out.println("This is update room type");
        Optional<RoomTypeEntity> roomTypeEntityCheck = roomTypeRepository.findById(updateRoomtypeRequest.id());

        if (roomTypeEntityCheck.isPresent()) {
            RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
            roomTypeEntity.setId(updateRoomtypeRequest.id());
            roomTypeEntity.setName(updateRoomtypeRequest.name());
            roomTypeEntity.setOverview(updateRoomtypeRequest.overview());
            roomTypeEntity.setPrice(updateRoomtypeRequest.price());
            roomTypeEntity.setArea(updateRoomtypeRequest.area());
            roomTypeEntity.setCapacity(updateRoomtypeRequest.capacity());

            BedTypeEntity bedTypeEntity = new BedTypeEntity();
            bedTypeEntity.setId(updateRoomtypeRequest.iDBedType());

            roomTypeEntity.setBedType(bedTypeEntity);


            List<MultipartFile> listMFiles = updateRoomtypeRequest.images();
            if (listMFiles != null && !listMFiles.isEmpty()) {
                String imagesString = updateRoomtypeRequest.images().stream()
                        .map(MultipartFile::getOriginalFilename) // Extract the file names
                        .collect(Collectors.joining(","));
                roomTypeEntity.setImage(imagesString);
            }




            RoomTypeEntity roomType = roomTypeRepository.save(roomTypeEntity);


            for (MultipartFile multipartFile : listMFiles) {
                filesStorageService.save(multipartFile);
            }


            roomAmenityRepository.deleteByRoomTypeId(updateRoomtypeRequest.id());
            addAmenitiesToRoomType(roomType.getId(), updateRoomtypeRequest.idAmenity());

            result = true;

            // xoá redis
            redisTemplate.delete(listRoomTypeKey);

        }



        return result;
    }


    @Override
    public List<RoomTypeDTO> allRoomTypes() {

        // parse object to json type
        ObjectMapper objectMapper = new ObjectMapper();

        // "List DTO" to respond to  user request
        List<RoomTypeDTO> roomTypeDTOList = new ArrayList<>();

        if (redisTemplate.hasKey(listRoomTypeKey)) {

             String data = redisTemplate.opsForValue().get(listRoomTypeKey).toString();

            try {
                roomTypeDTOList = objectMapper.readValue(data, List.class);
            }catch (JsonProcessingException e){
                throw new RuntimeException("Parsing to Object error"+ e.getMessage());
            }
        }else {

             // get All Value Form Dbase
            List<RoomTypeEntity> roomTypeEntityList = roomTypeRepository.findAll();
            for (RoomTypeEntity roomTypeEntity : roomTypeEntityList) {
                RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
                roomTypeDTO.setId(roomTypeEntity.getId());
                roomTypeDTO.setName(roomTypeEntity.getName());
                roomTypeDTO.setOverview(roomTypeEntity.getOverview());
                roomTypeDTO.setPrice(roomTypeEntity.getPrice());
                roomTypeDTO.setArea(roomTypeEntity.getArea());
                roomTypeDTO.setCapacity(roomTypeEntity.getCapacity());
                roomTypeDTO.setBedName(roomTypeEntity.getBedType().getName());

                List<RoomEntity> roomEntityList = roomRepository.findRoomEntityByRoomType(roomTypeEntity);
                List<String> roomNameList = roomEntityList.stream().map(RoomEntity::getName).collect(Collectors.toList());
                roomTypeDTO.setRoomName(roomNameList);

                String imagesString = roomTypeEntity.getImage();
                if (imagesString != null && !imagesString.isEmpty()) {
                    List<String> imagesList = Arrays.stream(imagesString.split(","))
                            .collect(Collectors.toList()).stream().map(item -> ("http://localhost:9999/roomType/file/"+item)).toList();
                    roomTypeDTO.setImage(imagesList);
                }

                List<RoomAmenityEntity> list = roomAmenityRepository.findByRoomTypeId(roomTypeEntity.getId());
                String amenityStr = list.stream().
                        map(roomAmenityEntity -> roomAmenityEntity.getAmenity().getDescription()).
                        collect(Collectors.joining(", "));

                roomTypeDTO.setAmenity(amenityStr);
                //          for (RoomAmenityEntity roomAmenityEntity : list) {
//              amenityStr+= (roomAmenityEntity.getAmenity().getDescription()+", ");
//          }
                roomTypeDTOList.add(roomTypeDTO);
            }



            // Saving to Redis
            try {
                String jsonString = objectMapper.writeValueAsString(roomTypeDTOList);
                redisTemplate.opsForValue().set(listRoomTypeKey, jsonString);

            }catch (JsonProcessingException e) {
                throw new RuntimeException("Parsing json error"+e.getMessage());
            }
        }





        return roomTypeDTOList;
    }

    @Transactional
    @Override
    public RoomTypeDTO findRoomTypeById(int id) {

        RoomTypeEntity roomTypeEntity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(roomTypeEntity.getId());
        roomTypeDTO.setName(roomTypeEntity.getName());
        roomTypeDTO.setOverview(roomTypeEntity.getOverview());
        roomTypeDTO.setPrice(roomTypeEntity.getPrice());
        roomTypeDTO.setArea(roomTypeEntity.getArea());
        roomTypeDTO.setCapacity(roomTypeEntity.getCapacity());
        roomTypeDTO.setBedName(roomTypeEntity.getBedType().getName());
        roomTypeDTO.setBedNum(roomTypeEntity.getBedType().getId());

        List<RoomEntity> roomEntityList = roomRepository.findRoomEntityByRoomType(roomTypeEntity);
        List<String> roomNameList = roomEntityList.stream().map(RoomEntity::getName).collect(Collectors.toList());
        roomTypeDTO.setRoomName(roomNameList);

        String imagesString = roomTypeEntity.getImage();

        if (imagesString != null && !imagesString.isEmpty()) {
            List<String> imagesList = Arrays.stream(imagesString.split(","))
                    .collect(Collectors.toList()).stream().map(item -> ("http://localhost:9999/roomType/file/"+item)).toList();
            roomTypeDTO.setImage(imagesList);
        }


        List<RoomAmenityEntity> list = roomAmenityRepository.findByRoomTypeId(roomTypeEntity.getId());
        String amenityStr = list.stream().
                map(roomAmenityEntity -> roomAmenityEntity.getAmenity().getDescription()).
                collect(Collectors.joining(", "));

        roomTypeDTO.setAmenity(amenityStr);

//        String amentityNum = list.stream().
//                map(roomAmenityEntity -> roomAmenityEntity.getAmenity().getDescription()).
//                collect(Collectors.joining(", "));
        return roomTypeDTO;
    }


    @Transactional
    @Override
    public boolean deleteRoomTypeById(int id) {

        boolean isSuccess = false;

        Optional<RoomTypeEntity> roomTypeOptional = roomTypeRepository.findById(id);

        if (roomTypeOptional.isPresent()) {
            roomAmenityRepository.deleteByRoomTypeId(id);
            roomTypeRepository.deleteById(id);

            isSuccess = true;
            redisTemplate.delete(listRoomTypeKey);
        }

        return isSuccess;
    }




    public void addAmenitiesToRoomType(int roomTypeId, List<Integer> idAmenity) {
        // Lấy RoomType dựa trên roomTypeId
        RoomTypeEntity roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        // Duyệt qua từng idAmenity trong danh sách
        for (Integer amenityId : idAmenity) {
            AmenityEntity amenity = amenityRepository.findById(amenityId)
                    .orElseThrow(() -> new RuntimeException("Amenity not found"));

            // Tạo đối tượng RoomAmenityEntity và lưu vào DB
            RoomAmenityKey key = new RoomAmenityKey(roomType.getId(), amenity.getId());
            RoomAmenityEntity roomAmenity = new RoomAmenityEntity();
            roomAmenity.setRoomAmenityKey(key);
            roomAmenity.setRoomType(roomType);
            roomAmenity.setAmenity(amenity);

            roomAmenityRepository.save(roomAmenity);
        }
    }


}
