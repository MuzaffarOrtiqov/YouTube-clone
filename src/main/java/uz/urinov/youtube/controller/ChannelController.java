package uz.urinov.youtube.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.urinov.youtube.dto.channel.ChannelCreateDTO;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.enums.ChannelStatus;
import uz.urinov.youtube.service.ChannelService;


import java.util.List;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    // 5. Channel
    // 1. Create Channel (USER)
    @PostMapping("/create")
    public ResponseEntity<ChannelResponseDTO> createChannel(@Valid @RequestBody ChannelCreateDTO channel) {
        ChannelResponseDTO response = channelService.createChannel(channel);
        return ResponseEntity.ok(response);
    }

    //  2. Update Channel ( USER and OWNER)
    @PutMapping("/update/{channelId}")
    public ResponseEntity<ChannelResponseDTO> updateChannel(@PathVariable("channelId") String channelId, @Valid @RequestBody ChannelCreateDTO channel) {
        ChannelResponseDTO response = channelService.updateChannel(channelId, channel);
        return ResponseEntity.ok(response);
    }

    // 3. Update Channel photo ( USER and OWNER)
    @PutMapping("/banner/{channelId}")
    public ResponseEntity<ChannelResponseDTO> updateBanner(@PathVariable("channelId") String channelId, @RequestParam("banner") MultipartFile banner) {
        ChannelResponseDTO response = channelService.updateBanner(channelId, banner);
        return ResponseEntity.ok(response);
    }

    // 4. Update Channel banner ( USER and OWNER)
    @PutMapping("/photo/{channelId}")
    public ResponseEntity<ChannelResponseDTO> updatePhoto(@PathVariable("channelId") String channelId, @RequestParam("photo") MultipartFile photo) {
        ChannelResponseDTO response = channelService.updatePhoto(channelId, photo);
        return ResponseEntity.ok(response);
    }

    // 5. Channel Pagination (ADMIN)
    @PostMapping("/adm/pagination")
    public ResponseEntity<Page<ChannelResponseDTO>> pagination(@RequestParam(name = "page",defaultValue = "1") int page,
                                                      @RequestParam(name = "size",defaultValue = "3") int size) {
        PageImpl<ChannelResponseDTO> response = channelService.pagination(page-1,size);
        return ResponseEntity.ok(response);
    }
    // 6. Get Channel By Id
    @PutMapping("/get-channel/{channelId}")
    public ResponseEntity<ChannelResponseDTO> getChannel(@PathVariable("channelId") String channelId ) {
        ChannelResponseDTO response = channelService.getChannel(channelId);
        return ResponseEntity.ok(response);
    }

    // 7. Change Channel Status (ADMIN,USER and OWNER)
    @PutMapping("/channel-status")
    public ResponseEntity<ChannelResponseDTO> channelStatus(@RequestParam("channelId") String channelId,
                                                            @RequestParam("status") ChannelStatus status) {
        ChannelResponseDTO response = channelService.channelStatus(channelId,status);
        return ResponseEntity.ok(response);
    }

    // 8. User Channel List (murojat qilgan userni)
    @GetMapping("/channel-list")
    public ResponseEntity<List<ChannelResponseDTO>> getChannelList() {
        List<ChannelResponseDTO> channelResponseDTOList=channelService.getChannelList();
        return ResponseEntity.ok(channelResponseDTOList);
    }

}
