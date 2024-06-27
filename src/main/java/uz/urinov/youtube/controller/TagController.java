package uz.urinov.youtube.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.category.CategoryDTO;
import uz.urinov.youtube.dto.tag.TagDTO;
import uz.urinov.youtube.dto.tag.UpdateTagDTO;
import uz.urinov.youtube.service.TagService;

import java.util.List;

@RequestMapping("/tag")
@RestController
public class TagController {
    @Autowired
    private TagService tagService;
    @PostMapping("/create")
    public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagDTO tagDTO) {
        TagDTO response = tagService.createTag(tagDTO);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable(name = "id") Integer id,@Valid @RequestBody UpdateTagDTO tagDTO) {
        TagDTO response = tagService.updateTag(id,tagDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTag(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(tagService.deleteTag(id));
    }

    @GetMapping("/all-tags")
    public ResponseEntity<List<TagDTO>> getAllCategories() {
        List<TagDTO> categoryList = tagService.getAllTags();
        return ResponseEntity.ok(categoryList);
    }

}
