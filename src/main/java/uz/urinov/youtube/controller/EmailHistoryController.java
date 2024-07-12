package uz.urinov.youtube.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.urinov.youtube.dto.emailHistory.EmailHistoryResponseDTO;
import uz.urinov.youtube.service.EmailHistoryService;

import java.util.List;

@RestController
@RequestMapping("/email-history")
@RequiredArgsConstructor
public class EmailHistoryController {
    private final EmailHistoryService emailHistoryService;

    // 1. Get email pagination (ADMIN)
    @GetMapping("/pagination")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<EmailHistoryResponseDTO>> pagination(@RequestParam(name = "page",defaultValue = "1") int page,
                                                                        @RequestParam(name = "size",defaultValue = "3") int size) {
        PageImpl<EmailHistoryResponseDTO> responseDTOList = emailHistoryService.pagination(page - 1, size);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTOList);
    }
}
