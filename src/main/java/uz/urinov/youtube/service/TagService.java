package uz.urinov.youtube.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.category.CategoryDTO;
import uz.urinov.youtube.dto.tag.TagDTO;
import uz.urinov.youtube.dto.tag.UpdateTagDTO;
import uz.urinov.youtube.entity.TagEntity;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.TagRepository;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public TagDTO createTag(TagDTO tagDTO) {
        //check if tag was created before
        if (checkTagExists(tagDTO.getName())) {
            log.info("Tag {} already exists", tagDTO.getName());
            TagEntity tagEntity = tagRepository.existsByName(tagDTO.getName());
            return toDTO(tagEntity);
        }
        // if not created , create new tag
        TagEntity tagEntity = new TagEntity();
        tagEntity.setName(tagDTO.getName());
        tagRepository.save(tagEntity);
        return toDTO(tagEntity);
    }

    public boolean checkTagExists(String tagName) {
        TagEntity tag = tagRepository.existsByName(tagName);
        if (tag == null) {
            return false;
        }
        return true;
    }

    public TagDTO toDTO(TagEntity tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setCreatedDate(tag.getCreatedDate());
        return dto;
    }

    public TagDTO updateTag(Integer id, UpdateTagDTO tagDTO) {
        TagEntity tag = getTag(id);
        tag.setName(tagDTO.getName());
        log.info("Updating tag {}", tag.getName());
        tagRepository.save(tag);
        return toDTO(tag);
    }

    public TagEntity getTag(Integer id) {
        return tagRepository.findById(id).orElseThrow(() -> {
            log.info("Tag {} not found", id);
            throw new AppBadException("Tag not found");
        });
    }

    public String deleteTag(Integer id) {
        TagEntity tag = getTag(id);
        log.info("Deleting tag {}", tag.getName());
        tagRepository.delete(tag);
        return "Tag " + id + " deleted";
    }

    public List<TagDTO> getAllTags() {
        List<TagDTO> dtoList = new LinkedList<>();
        tagRepository.findAll().forEach(tag -> {
            TagDTO dto =new TagDTO();
            dto.setName(tag.getName());
            dtoList.add(dto);
        });
        return dtoList;
    }
}
