package uz.urinov.youtube.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.urinov.youtube.dto.CategoryDTO;
import uz.urinov.youtube.service.CategoryService;

@RequestMapping("/category")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@Valid  @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO response = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(response);
    }
}
