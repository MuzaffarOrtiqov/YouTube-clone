package uz.urinov.youtube.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.category.CategoryDTO;
import uz.urinov.youtube.dto.category.CategoryUpdateDTO;
import uz.urinov.youtube.entity.CategoryEntity;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.CategoryRepository;

import java.util.LinkedList;
import java.util.List;
@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDTO.getName());
        log.info("Category saving: " + categoryDTO.getName());
        categoryRepository.save(categoryEntity);
        return toDTO(categoryEntity);
    }

    public CategoryDTO toDTO(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setName(categoryEntity.getName());
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        return categoryDTO;
    }

    public CategoryDTO updateCategory(Integer id, CategoryUpdateDTO categoryDTO) {
        CategoryEntity categoryEntity = getCategory(id);
        categoryEntity.setName(categoryDTO.getName());
        log.info("Category updating: " + categoryDTO.getName());
        categoryRepository.save(categoryEntity);
        return toDTO(categoryEntity);
    }

    public CategoryEntity getCategory(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.info("Category not found with id : " + id);
            throw new AppBadException("Category not found");
        });
    }

    public String deleteCategory(Integer id) {
        CategoryEntity categoryEntity = getCategory(id);
        if (categoryEntity != null) {
            log.info("Category deleting: " + id);
            categoryRepository.delete(categoryEntity);
        }
        return "Category deleted";
    }

    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> dtoList = new LinkedList<>();
        categoryRepository.findAll().forEach(categoryEntity -> {
           CategoryDTO dto =new CategoryDTO();
           dto.setName(categoryEntity.getName());
           dtoList.add(dto);
        });
        return dtoList;
    }
}
