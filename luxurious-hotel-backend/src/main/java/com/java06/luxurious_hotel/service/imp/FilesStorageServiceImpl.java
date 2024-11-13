package com.java06.luxurious_hotel.service.imp;


import com.java06.luxurious_hotel.exception.roomType.FileNotFoundException;
import com.java06.luxurious_hotel.service.FilesStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("uploads");

    private final Path root1 = Paths.get("uploads/guest");

    @Override
    public void save1(MultipartFile file) {
        if (file != null) {
            try {
                if (!Files.exists(root1)) {
                    Files.createDirectories(root1);
                }

                // Thực hiện ghi đè nếu tệp đã tồn tại trên server, tránh throw exception (tệp đã tồn tại)
                Files.copy(file.getInputStream(), this.root1.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public Resource load1(String filename) {
        try {
            Path file = root1.resolve(filename);

            Resource resource = new UrlResource(file.toUri());  // Biển file thành resource
            if(resource.exists()){
                return resource;
            }else {
                throw new FileNotFoundException();
            }
        }catch (Exception e){
            throw new FileNotFoundException(e.getMessage());
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            //Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

            // thực hiện ghi đè nêu hình đã tồn tại ở sever, tranh throw exception(A file of that name already exists.)
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
//            if (e instanceof FileAlreadyExistsException) {
//                throw new RuntimeException("A file of that name already exists.");
//            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {



        try {
            Path file = root.resolve(filename);

            Resource resource = new UrlResource(file.toUri());  // Biển file thành resource
            if(resource.exists()){
                return resource;
            }else {
                throw new FileNotFoundException();
            }
        }catch (Exception e){
            throw new FileNotFoundException(e.getMessage());
        }



    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Stream<Path> loadAll() {
        return Stream.empty();
    }
}
