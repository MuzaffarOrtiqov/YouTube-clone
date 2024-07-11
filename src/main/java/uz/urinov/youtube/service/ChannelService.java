package uz.urinov.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.dto.channel.ChannelCreateDTO;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.entity.ChannelEntity;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.enums.ChannelStatus;
import uz.urinov.youtube.enums.ProfileRole;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.ChannelRepository;
import uz.urinov.youtube.util.SecurityUtil;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private AttachService attachService;

    // 1. Create Channel (USER)
    public ChannelResponseDTO createChannel(ChannelCreateDTO channel) {
        Integer profileId = SecurityUtil.getProfileId();
        Optional<ChannelEntity> entityOptional = channelRepository.findByProfileIdAndName(profileId, channel.getName());
        if (entityOptional.isPresent()) {
            throw new AppBadException("Avval siz bunday kanal yaratgansiz");
        }
        ChannelEntity channelEntity = new ChannelEntity();
        channelEntity.setName(channel.getName());
        channelEntity.setProfileId(profileId);
        channelEntity.setDescription(channel.getDescription());
        channelEntity.setPhotoId(channel.getPhotoId());
        channelEntity.setBannerId(channel.getBannerId());
        channelRepository.save(channelEntity);
        return getDTO(channelEntity);
    }

    //  2. Update Channel ( USER and OWNER)
    public ChannelResponseDTO updateChannel(String channelId, ChannelCreateDTO channel) {
        Integer profileId = SecurityUtil.getProfileId();
        ChannelEntity channelEntity = get(channelId);
        if (!profileId.equals(channelEntity.getProfileId())) {
            throw new AppBadException("Bu kanal sizga tegishliy emas");
        }
        channelEntity.setName(channel.getName());
        channelEntity.setDescription(channel.getDescription());
        if (channel.getPhotoId() != null) {
            channelEntity.setPhotoId(channel.getPhotoId());
        }
        if (channel.getBannerId() != null) {
            channelEntity.setBannerId(channel.getBannerId());
        }
        channelRepository.save(channelEntity);
        return getDTO(channelEntity);
    }

    // 3. Update Channel photo ( USER and OWNER)
    public ChannelResponseDTO updateBanner(String channelId, MultipartFile banner) {
        ChannelEntity channelEntity = get(channelId);
        Integer profileId = SecurityUtil.getProfileId();
        if (!profileId.equals(channelEntity.getProfileId())) {
            throw new AppBadException("Bu kanal sizga tegishliy emas");
        }
        AttachDTO savedAttach = attachService.saveAttach(banner);
        channelEntity.setBannerId(savedAttach.getId());
        channelRepository.save(channelEntity);
        return getDTO(channelEntity);
    }

    // 4. Update Channel banner ( USER and OWNER)
    public ChannelResponseDTO updatePhoto(String channelId, MultipartFile photo) {
        ChannelEntity channelEntity = get(channelId);
        Integer profileId = SecurityUtil.getProfileId();
        if (!profileId.equals(channelEntity.getProfileId())) {
            throw new AppBadException("Bu kanal sizga tegishliy emas");
        }
        AttachDTO savedAttach = attachService.saveAttach(photo);
        channelEntity.setPhotoId(savedAttach.getId());
        channelRepository.save(channelEntity);
        return getDTO(channelEntity);
    }

    // 5. Channel Pagination (ADMIN)
    public PageImpl<ChannelResponseDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChannelEntity> pageObj = channelRepository.findAll(pageable);
        Long total = pageObj.getTotalElements();
        List<ChannelResponseDTO> list = new LinkedList<>();
        pageObj.forEach(channelEntity -> {
            ChannelResponseDTO channelResponseDTO = getDTO(channelEntity);
            list.add(getDTO(channelEntity));
        });
        return new PageImpl<>(list, pageable, total);
    }

    // 6. Get Channel By Id
    public ChannelResponseDTO getChannel(String channelId) {
        ChannelEntity channelEntity = get(channelId);
        return getDTO(channelEntity);
    }
    // 7. Change Channel Status (ADMIN,USER and OWNER)
    public ChannelResponseDTO channelStatus(String channelId, ChannelStatus status) {
        ProfileEntity profile = SecurityUtil.getProfile();
        if (profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            ChannelEntity channelEntity = get(channelId);
            channelEntity.setStatus(status);
            channelRepository.save(channelEntity);
            return getDTO(channelEntity);
        }
        Optional<ChannelEntity> entityOptional = channelRepository.findByIdAndProfileId(channelId, profile.getId());
        if (entityOptional.isEmpty()) {
            throw new AppBadException("Kanal sizga tegishliy emas");
        }
        ChannelEntity channelEntity = entityOptional.get();
        channelEntity.setStatus(status);
        channelRepository.save(channelEntity);
        return getDTO(channelEntity);
    }

    // 8. User Channel List (murojat qilgan userni)
    public List<ChannelResponseDTO> getChannelList() {
        Integer profileId = SecurityUtil.getProfileId();
        List<ChannelEntity> list = channelRepository.findByProfileId(profileId);
        return  list.stream().map(this::getDTO).toList();
    }

    public ChannelResponseDTO getDTO(ChannelEntity channelEntity) {
        ChannelResponseDTO channelResponseDTO = new ChannelResponseDTO();
        channelResponseDTO.setId(channelEntity.getId());
        channelResponseDTO.setName(channelEntity.getName());
        channelResponseDTO.setDescription(channelEntity.getDescription());
        channelResponseDTO.setBanner(channelEntity.getBannerId());
        channelResponseDTO.setPhoto(channelEntity.getPhotoId());
        channelResponseDTO.setProfileId(channelEntity.getProfileId());
        channelResponseDTO.setStatus(channelEntity.getStatus());
        channelResponseDTO.setCreateDate(channelEntity.getCreateDate());
        return channelResponseDTO;
    }

    public ChannelEntity get(String id) {
        return channelRepository.findById(id).orElseThrow(() ->
                new AppBadException("Channel id not found"));
    }

    public ChannelResponseDTO getChannelDTOByChannelId(String channelId) {
        ChannelEntity channelEntity = get(channelId);
        ChannelResponseDTO channelResponseDTO = new ChannelResponseDTO();
        channelResponseDTO.setId(channelEntity.getId());
        channelResponseDTO.setName(channelEntity.getName());
        channelEntity.setPhotoId(channelEntity.getPhotoId());
        return channelResponseDTO;
    }


}
