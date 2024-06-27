package uz.urinov.youtube.repository;

import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
}
