package uz.urinov.youtube.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.playlist.PlayListShortInfoDTO;
import uz.urinov.youtube.dto.playlist.PlaylistCreateDTO;
import uz.urinov.youtube.dto.playlist.PlaylistResponseDTO;
import uz.urinov.youtube.dto.playlist.PlaylistUpdateDTO;
import uz.urinov.youtube.enums.PlaylistStatus;
import uz.urinov.youtube.service.PlaylistService;
import uz.urinov.youtube.util.Result;

import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    // 6. Playlist
    //    1. Create Playlist (USER)
    @PostMapping("/create")
    public ResponseEntity<PlaylistResponseDTO> createPlaylist(@Valid @RequestBody PlaylistCreateDTO playlistCreateDTO) {
        PlaylistResponseDTO responseDTO = playlistService.createPlaylist(playlistCreateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // 2. Update Playlist(USER and OWNER)
    @PutMapping("/update/{playlistId}")
    public ResponseEntity<PlaylistResponseDTO> updatePlaylist(@PathVariable("playlistId") Integer playlistId,
                                                              @Valid @RequestBody PlaylistUpdateDTO dto) {
        PlaylistResponseDTO responseDTO = playlistService.updatePlaylist(playlistId, dto);
        return ResponseEntity.ok(responseDTO);
    }

    //  3. Change Playlist Status (USER and OWNER)
    @PutMapping("/status")
    public ResponseEntity<PlaylistResponseDTO> statusPlaylist(@RequestParam("playlistId") Integer playlistId,
                                                              @RequestParam("status") PlaylistStatus status) {
        PlaylistResponseDTO responseDTO = playlistService.statusPlaylist(playlistId, status);
        return ResponseEntity.ok(responseDTO);
    }

    // 4. Delete Playlist (USER and OWNER, ADMIN)
    @DeleteMapping("/delete/{playlist}")
    public ResponseEntity<Result> deletePlaylist(@PathVariable("playlist") Integer playlistId) {
        Result response = playlistService.deletePlaylist(playlistId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // 5. Playlist Pagination (ADMIN) PlayListInfo
    @GetMapping("/adm/pagination")
    public ResponseEntity<Page<PlaylistResponseDTO>> listPlaylistPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                      @RequestParam(value = "size",defaultValue = "3") int size) {
        PageImpl<PlaylistResponseDTO> response=playlistService.listPlaylistPage(page-1,size);
        return ResponseEntity.ok(response);
    }

    // 6. Playlist List By UserId (order by order number desc) (ADMIN) PlayListInfo
     @GetMapping("/list-userId/{userId}")
    public ResponseEntity<List<PlaylistResponseDTO>> listPlaylistByUserId(@PathVariable("userId") Integer userId) {
        List<PlaylistResponseDTO> responseDTOList=playlistService.listPlaylistByUserId(userId);
        return ResponseEntity.ok(responseDTOList);
     }

     // 7. Get User Playlist (order by order number desc) (murojat qilgan user ni) PlayListShortInfo
    @GetMapping("/list-user")
    public ResponseEntity<List<PlayListShortInfoDTO>> listPlaylistByUser() {
        List<PlayListShortInfoDTO> responseDTOList=playlistService.listPlaylistByUser();
        return ResponseEntity.ok(responseDTOList);
    }

    // 8. Get Channel Play List By ChannelKey (order by order_num desc) (only Public) PlayListShortInfo
    @GetMapping("/list-user-all/{channelId}")
    public ResponseEntity<List<PlayListShortInfoDTO>> listPlaylistByUserAll(@PathVariable String channelId) {
        List<PlayListShortInfoDTO> responseDTOList=playlistService.listPlaylistByUserAll(channelId);
        return ResponseEntity.ok(responseDTOList);
    }

    // 9.Get Playlist by id
    // id,name,video_count, total_view_count (shu play listdagi videolarni ko'rilganlar soni), last_update_date
     @GetMapping("/getId/{id}")
    public ResponseEntity<PlaylistResponseDTO> getPlaylistById(@PathVariable("id") Integer id) {
        PlaylistResponseDTO playlistResponseDTO=playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlistResponseDTO);
     }


}
