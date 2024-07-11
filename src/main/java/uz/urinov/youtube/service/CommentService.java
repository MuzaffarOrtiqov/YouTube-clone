package uz.urinov.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.dto.comment.CommentCreateDto;
import uz.urinov.youtube.dto.comment.CommentResponseDto;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.dto.video.VideoDTO;
import uz.urinov.youtube.entity.CommentEntity;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.enums.ProfileRole;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.mapper.CommentInfoMapper;
import uz.urinov.youtube.mapper.CommentMapper;
import uz.urinov.youtube.repository.CommentRepository;
import uz.urinov.youtube.util.Result;
import uz.urinov.youtube.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final VideoService videoService;
    private final ProfileService profileService;


    // 1. Crate Comment (USER)
    public Result addComment(CommentCreateDto dto) {
        videoService.getVideoById(dto.getVideoId());
        CommentEntity comment = new CommentEntity();
        comment.setVideoId(dto.getVideoId());
        comment.setProfileId(SecurityUtil.getProfileId());
        comment.setContent(dto.getContent());
        comment.setReplyId(dto.getReplyId());
        commentRepository.save(comment);
        return new Result("Comment saqlandi", true);
    }

    // 2. Update Comment (USER AND OWNER)
    public Result updateComment(CommentCreateDto dto) {
        Integer profileId = SecurityUtil.getProfileId();
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findByProfileIdAndVideoId(profileId, dto.getVideoId());
        if (optionalCommentEntity.isEmpty()) {
            return new Result("Comment not found", false);
        }
        CommentEntity commentEntity = optionalCommentEntity.get();
        commentEntity.setContent(dto.getContent());
        commentEntity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(commentEntity);
        return new Result("Comment o'zgartirildi", true);
    }

    // 3. Delete Comment (USER AND OWNER, ADMIN)
    public Result delete(Integer commentId) {
        ProfileEntity profile = SecurityUtil.getProfile();
        CommentEntity comment = getComment(commentId);
        if (profile.getRole().equals(ProfileRole.ROLE_ADMIN)){
            comment.setVisible(false);
            commentRepository.save(comment);
            return new Result("Comment deleted", true);
        }

        if (profile.getId().equals(comment.getProfileId())){
            comment.setVisible(false);
            commentRepository.save(comment);
            return new Result("Comment deleted", true);
        }
        return new Result("Siz bu commentni o'chira olmaysiz", false);
    }

    // 4. Comment List Pagination (ADMIN)
    public PageImpl<CommentResponseDto> paginationComment(int page, Integer size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<CommentEntity> comments = commentRepository.findAll(pageable);
        List<CommentResponseDto> commentResponseDtoList = comments.stream().map(this::getCommentDto).toList();
        return new PageImpl<>(commentResponseDtoList, pageable, comments.getTotalElements());
    }

    public CommentEntity getComment(Integer commentId) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new AppBadException("Bunday comment not found");
        }
        return optionalComment.get();
    }

    //  5. Comment List By profileId(ADMIN)
    public List<CommentResponseDto> getCommentListByProfileId(Integer profileId) {
        profileService.getProfile(profileId);
        List<CommentMapper> commentListByProfileId = commentRepository.getCommentListByProfileId(profileId);
        return commentListByProfileId.stream().map(this::getCommentMapperDto).toList();
    }

    // 6. Comment List By Profile (murojat qilgan odamning comment lari) (USER AND OWNER)
    public List<CommentResponseDto> getCommentList() {
        Integer profileId = SecurityUtil.getProfileId();
        List<CommentMapper> commentListByProfileId = commentRepository.getCommentListByProfileId(profileId);
        return commentListByProfileId.stream().map(this::getCommentMapperDto).toList();
    }

    // 7. Comment List by videoId CommentInfo
    public List<CommentResponseDto> getCommentListByVideoId(String videoId) {
        List<CommentInfoMapper> mapperList = commentRepository.getCommentListByVideoId(videoId);
        return mapperList.stream().map(this::getCommentInfoMapper).toList();
    }

    public CommentResponseDto getCommentInfoMapper(CommentInfoMapper mapper) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(mapper.getId());
        dto.setContent(mapper.getContent());
        dto.setCommentTime(mapper.getCreatedDate());
        dto.setLike(mapper.getLikeCount());
        dto.setDislike(mapper.getDisLikeCount());
        ProfileResponseDTO profile=new ProfileResponseDTO();
        profile.setId(mapper.getProfileId());
        profile.setName(mapper.getName());
        profile.setSurname(mapper.getSurname());
        profile.setPhotoId(mapper.getPhotoId());
        dto.setProfile(profile);
        return dto;
    }

    public CommentResponseDto getCommentMapperDto(CommentMapper mapper) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(mapper.getId());
        dto.setContent(mapper.getContent());
        dto.setCommentTime(mapper.getCreated_date());
        dto.setLike(mapper.getLikeCount());
        dto.setDislike(mapper.getDisLike_count());
        VideoDTO videoDTO=new VideoDTO();
        videoDTO.setId(mapper.getVideoId());
        videoDTO.setTitle(mapper.getTitle());
        videoDTO.setPreviewAttachId(mapper.getPreview_attach_id());
        dto.setVideo(videoDTO);
        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setOriginName(mapper.getOrigin_name());
        attachDTO.setDuration(mapper.getDuration());
        dto.setAttach(attachDTO);
        return dto;
    }


    public CommentResponseDto getCommentDto(CommentEntity entity) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(entity.getId());
        commentResponseDto.setProfileId(entity.getProfileId());
        commentResponseDto.setVideoId(entity.getVideoId());
        commentResponseDto.setContent(entity.getContent());
        commentResponseDto.setReplyId(entity.getReplyId());
        commentResponseDto.setVisible(entity.getVisible());
        commentResponseDto.setUploadTime(entity.getUpdateDate());
        commentResponseDto.setCommentTime(entity.getCreateDate());
        return commentResponseDto;
    }



}
