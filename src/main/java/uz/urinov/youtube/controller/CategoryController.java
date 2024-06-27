package uz.urinov.youtube.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.category.CategoryDTO;
import uz.urinov.youtube.dto.category.CategoryUpdateDTO;
import uz.urinov.youtube.service.CategoryService;

import java.util.List;

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
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable(name = "id")Integer id,@Valid  @RequestBody CategoryUpdateDTO categoryDTO) {
        CategoryDTO response = categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id")Integer id) {
        String response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all-categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
       List<CategoryDTO> categoryList = categoryService.getAllCategories();
       return ResponseEntity.ok(categoryList);
    }
}
