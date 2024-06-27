package uz.urinov.youtube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.CategoryDTO;
import uz.urinov.youtube.entity.CategoryEntity;
import uz.urinov.youtube.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDTO.getName());
        categoryRepository.save(categoryEntity);
        return toDTO(categoryEntity);
    }

    public CategoryEntity getCategory(int id) {
        return null;
    }
    public CategoryDTO toDTO (CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setName(categoryEntity.getName());
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        return categoryDTO;
    }
}
