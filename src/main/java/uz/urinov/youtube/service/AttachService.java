package uz.urinov.youtube.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.entity.AttachEntity;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.AttachRepository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${attach.upload.url}")
    private String attachUrl;

    public AttachDTO saveAttach(MultipartFile file) {

        try {
            String pathName = getYdmString();
            File folder = new File("uploads/" + pathName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String key = UUID.randomUUID().toString();
            // extension of the video
            String extension = getExtension(file.getOriginalFilename());
            //save to system
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + pathName + "/" + key + "." + extension);
            Files.write(path, bytes);

            //save to db
            AttachEntity attachEntity = new AttachEntity();
            attachEntity.setId(key + "." + extension);
            attachEntity.setPath(pathName);
            attachEntity.setOriginName(file.getOriginalFilename());
            attachEntity.setSize(file.getSize());
            attachEntity.setExtension(extension);
            attachRepository.save(attachEntity);

            return toDTO(attachEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private AttachDTO toDTO(AttachEntity attachEntity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(attachEntity.getId());
        attachDTO.setOriginName(attachEntity.getOriginName());
        attachDTO.setSize(attachEntity.getSize());
        attachDTO.setExtension(attachEntity.getExtension());
        attachDTO.setDuration(attachEntity.getDuration());
        attachDTO.setPath(attachUrl+attachEntity.getPath()+"/"+attachEntity.getId());
        return attachDTO;
    }


    private String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public String getYdmString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day;
    }

    public byte[] open_general(String attachId) {
        byte[] data;
        try {
            AttachEntity entity = get(attachId);
            String path = entity.getPath() + "/" + attachId;
            Path file = Paths.get("uploads/" + path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public AttachEntity get(String attachId) {
        Optional<AttachEntity> attachEntity = attachRepository.findById(attachId);
        if (attachEntity.isEmpty()) {
            throw new AppBadException("Attach Not Found");
        }
        return attachEntity.get();
    }

    public ResponseEntity download(String attachId) {
        try {
            AttachEntity entity = get(attachId);
            String path = entity.getPath() + "/" + attachId;
            Path file = Paths.get("uploads/" + path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public PageImpl<AttachDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttachEntity> pageObj = attachRepository.findAll(pageable);
        Long total = pageObj.getTotalElements();
        List<AttachDTO> list = new LinkedList<>();
        pageObj.forEach(attachEntity -> {
            AttachDTO attachDTO = toDTO(attachEntity);
            list.add(toDTO(attachEntity));
        });
        return new PageImpl<>(list, pageable, total);
    }

    public String delete(String filename) {
        AttachEntity attachEntity = get(filename);
        File file = new File("uploads/"+attachEntity.getPath()+"/"+filename);
        if (!file.delete()) {
           return "Could not delete the file!";
        }
        attachRepository.delete(attachEntity);
        return "Deleted the file!";
    }
}
