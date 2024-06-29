package uz.urinov.youtube.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.service.AttachService;
@Slf4j
@RequestMapping("/attach")
@RestController
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO response = attachService.saveAttach(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/open/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open(@PathVariable String fileName) {
    return this.attachService.open_general(fileName);
    }

    @GetMapping("/download/{attachId}")
    public ResponseEntity<Resource> download(@PathVariable("attachId") String attachId) {
        return attachService.download(attachId);
    }

    @PostMapping("/pagination")
    public ResponseEntity<Page<AttachDTO>> pagination(@RequestParam(name = "page",defaultValue = "1") int page,
                                                      @RequestParam(name = "size",defaultValue = "3") int size) {
        PageImpl<AttachDTO> response = attachService.pagination(page-1,size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{attachId}")
    public ResponseEntity<String> delete(@PathVariable String attachId) {
       String response =  attachService.delete(attachId);
       return ResponseEntity.ok(response);
    }




}
