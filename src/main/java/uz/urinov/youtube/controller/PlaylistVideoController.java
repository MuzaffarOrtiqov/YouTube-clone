package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.playlist.PlaylistResponseDTO;
import uz.urinov.youtube.dto.playlistVideo.PlaylistVideoCreateDTO;
import uz.urinov.youtube.service.PlaylistVideoService;
import uz.urinov.youtube.util.Result;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/playlist-video")
public class PlaylistVideoController {
    @Autowired
    private PlaylistVideoService playlistVideoService;
    //  1. Create (User and Owner) (playlist_id,video_id, order_num) front dan keladigan malumotlar
    @PostMapping("/create")
    public ResponseEntity<Result> create(@Valid @RequestBody PlaylistVideoCreateDTO playlistVideoCreateDTO) {
        Result result = playlistVideoService.create(playlistVideoCreateDTO);
        return ResponseEntity.status(result.isSuccess() ? 201 : 401).body(result);
    }

    // 2. Update (playlist_id,video_id, order_num) front dan keladigan malumotlar
    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody PlaylistVideoCreateDTO playlistVideoCreateDTO) {
        Result result = playlistVideoService.update(playlistVideoCreateDTO);
        return ResponseEntity.status(result.isSuccess() ? 201 : 401).body(result);
    }
    //  3. Delete PlayListVideo (playlist_id,video_id) front dan keladigan malumotlar
     @DeleteMapping("/delete")
     public ResponseEntity<Result> delete(@RequestParam Integer playlistId, @RequestParam String videoId) {
         Result result = playlistVideoService.delete(playlistId,videoId);
         return ResponseEntity.status(result.isSuccess() ? 201 : 401).body(result);
     }

     // 4. Get Video list by playListId (video status published) PlaylistVideoInfo
    @GetMapping("/video-list-playlistId/{playListId}")
    public ResponseEntity<List<PlaylistResponseDTO>> getPlaylistVideoList(@PathVariable Integer playListId) {
        List<PlaylistResponseDTO> responseDTO=playlistVideoService.getPlaylistVideoList(playListId);
        return ResponseEntity.ok(responseDTO);
    }

}
