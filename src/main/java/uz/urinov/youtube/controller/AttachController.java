package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;;
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
@SecurityRequirement(name = "Authorization")
@Tag(name = "Api for attach", description = "List of APIs for attach ")
public class AttachController {
    @Autowired
    private AttachService attachService;

    //    @PostMapping("/upload")
    @RequestMapping(
            path = "/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Upload file", description = "Api to upload a video, photo and other")
    public ResponseEntity<AttachDTO> upload(@RequestPart("file") MultipartFile file) {
        log.info("Uploading file {}", file.getOriginalFilename());
        AttachDTO response = attachService.saveAttach(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/open/{fileName}", produces = MediaType.ALL_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "File open", description = "API to open a media type")
    public byte[] open(@PathVariable String fileName) {
        log.info("Opening file {}", fileName);
        return this.attachService.open_general(fileName);
    }

    @GetMapping("/download/{attachId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Download a file", description = "API to open a media type")
    public ResponseEntity<Resource> download(@PathVariable("attachId") String attachId) {
        log.info("Downloading file with id  {}", attachId);
        return attachService.download(attachId);
    }

    @PostMapping("/pagination")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "All attach ", description = "API to return all attaches with pagination")
    public ResponseEntity<Page<AttachDTO>> pagination(@RequestParam(name = "page", defaultValue = "1") int page,
                                                      @RequestParam(name = "size", defaultValue = "3") int size) {
        log.info("Paging attach ");
        PageImpl<AttachDTO> response = attachService.pagination(page - 1, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{attachId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete a file", description = "API to delete an uploaded file")
    public ResponseEntity<String> delete(@PathVariable String attachId) {
        log.info("Deleting file with id  {}", attachId);
        String response = attachService.delete(attachId);
        return ResponseEntity.ok(response);
    }


}
