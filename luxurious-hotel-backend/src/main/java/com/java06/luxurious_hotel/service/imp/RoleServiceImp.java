package com.java06.luxurious_hotel.service.imp;

import com.java06.luxurious_hotel.dto.EmployeeDTO;
import com.java06.luxurious_hotel.dto.RoleDTO;
import com.java06.luxurious_hotel.entity.RoleEntity;
import com.java06.luxurious_hotel.entity.UserEntity;
import com.java06.luxurious_hotel.repository.EmployeeReposiory;
import com.java06.luxurious_hotel.repository.RoleRepository;
import com.java06.luxurious_hotel.service.FilesStorageService;
import com.java06.luxurious_hotel.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    EmployeeReposiory employeeReposiory;

    @Autowired
    FilesStorageService filesStorageService;

    @Override
    public List<RoleDTO> getRoles() {
        List<RoleDTO> roledto = new ArrayList<>();
        List<RoleEntity> roles = roleRepository.findAll();
        for (RoleEntity data : roles) {
            RoleDTO dto = new RoleDTO();
            dto.setId(data.getId());
            dto.setName(data.getName());
            dto.setDescription(data.getDescription());
            roledto.add(dto);
        }
        return roledto;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> roledto = new ArrayList<>();
        List<RoleEntity> role = roleRepository.findAll();
        List <UserEntity> users = employeeReposiory.findAll();
        String imageBaseUrl = "http://localhost:9999/file/";
        for (RoleEntity data : role) {
            if (data.getId()==1){
                continue;
            }
            RoleDTO dto = new RoleDTO();
            dto.setId(data.getId());
            dto.setName(data.getName());
            dto.setDescription(data.getDescription());
            List<EmployeeDTO> employees = new ArrayList<>();
            for (UserEntity user : users) {
                if (user.getRole().getId() == data.getId()) {
                    EmployeeDTO employee = new EmployeeDTO();
                    employee.setId(user.getId());
                    employee.setImage(user.getImage());
                    employee.setFirstname(user.getFirstName());
                    employee.setLastname(user.getLastName());
                    employee.setEmail(user.getEmail());
                    if (employee.getImage() !=null && !employee.getImage().isEmpty()) {
                        String imageUrl = imageBaseUrl + user.getImage();
                        employee.setImage(imageUrl);
                    }else {
                        employee.setImage("http://localhost:9999/file/item.jpg.jpg");
                    }
                    employees.add(employee);
                }
            }
            dto.setEmployees(employees);
            roledto.add(dto);

        }
        return roledto;
    }
}
