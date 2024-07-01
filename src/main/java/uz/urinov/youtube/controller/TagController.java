package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.tag.TagDTO;
import uz.urinov.youtube.dto.tag.UpdateTagDTO;
import uz.urinov.youtube.service.TagService;

import java.util.List;
@Slf4j
@RequestMapping("/tag")
@RestController
@Tag(name = "Api s for tag",description = "List of Api s to create tags")
public class TagController {
    @Autowired
    private TagService tagService;
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Create tag")
    public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagDTO tagDTO) {
        log.info("Creating tag with name : {}", tagDTO.getName());
        TagDTO response = tagService.createTag(tagDTO);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update tag")
    public ResponseEntity<TagDTO> updateTag(@PathVariable(name = "id") Integer id,@Valid @RequestBody UpdateTagDTO tagDTO) {
        log.info("Updating tag with name : {}", tagDTO.getName());
        TagDTO response = tagService.updateTag(id,tagDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<String> deleteTag(@RequestParam(name = "id") Integer id) {
        log.info("Deleting tag with id : {}", id);
        return ResponseEntity.ok(tagService.deleteTag(id));
    }

    @GetMapping("/all-tags")
    @Operation(summary = "Get all tags")
    public ResponseEntity<List<TagDTO>> getAllCategories() {
        log.info("Getting all tags");
        List<TagDTO> categoryList = tagService.getAllTags();
        return ResponseEntity.ok(categoryList);
    }

}
