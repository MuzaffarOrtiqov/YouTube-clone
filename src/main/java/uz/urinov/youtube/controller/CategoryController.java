package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.category.CategoryDTO;
import uz.urinov.youtube.dto.category.CategoryUpdateDTO;
import uz.urinov.youtube.service.CategoryService;

import java.util.List;

@Slf4j
@RequestMapping("/category")
@RestController
@Tag(name = "Api for Category", description = "List of Api s for category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/adm/create")
    @Operation(summary = "Category create")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("Category created: {}", categoryDTO.getName());
        CategoryDTO response = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/adm/update/{id}")
    @Operation(summary = "Update given category")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable(name = "id") Integer id, @Valid @RequestBody CategoryUpdateDTO categoryDTO) {
        log.info("Category updated: {}", categoryDTO.getName());
        CategoryDTO response = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/adm/delete/{id}")
    @Operation(summary = "Delete given category")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") Integer id) {
        log.info("Category with id {} deleted", id);
        String response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-categories")
    @Operation(summary = "Get all category types")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        log.info("Get all category types");
        List<CategoryDTO> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }
}
