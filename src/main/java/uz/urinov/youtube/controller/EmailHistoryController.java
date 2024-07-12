package uz.urinov.youtube.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.urinov.youtube.service.EmailHistoryService;

@RestController
@RequestMapping("/email-history")
@RequiredArgsConstructor
public class EmailHistoryController {
    private final EmailHistoryService emailHistoryService;
}
