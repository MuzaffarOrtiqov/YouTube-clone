package uz.urinov.youtube.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.report.ReportCreateDTO;
import uz.urinov.youtube.dto.report.ReportResponseDTO;
import uz.urinov.youtube.service.ReportService;
import uz.urinov.youtube.util.Result;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // 1. Create repost (USER)
    @PostMapping("/create")
    public ResponseEntity<Result> createReport(@RequestBody ReportCreateDTO dto) {
        Result result = reportService.createReport(dto);
        return ResponseEntity.status(result.isSuccess()?201:401).body(result);
    }

    //  2. ReportList Pagination ADMIN ReportInfo
    @GetMapping("/pagination")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<ReportResponseDTO>> pagination(@RequestParam(name = "page",defaultValue = "1") int page,
                                                                  @RequestParam(name = "size",defaultValue = "3") int size) {
        PageImpl<ReportResponseDTO> responseDTOList=reportService.pagination(page-1,size);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTOList);
    }

    // 3. Remove Report by id (ADMIN)
    @GetMapping("/get-by-id/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReportResponseDTO> getById(@PathVariable(name = "id") Integer id) {
        ReportResponseDTO responseDTO=reportService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    // 4. Report List By User id (ADMIN) ReportInfo
    @GetMapping("/get-by-user-id/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReportResponseDTO>> getByUserId(@PathVariable(name = "id") Integer id) {
        List<ReportResponseDTO> responseDTOList=reportService.getByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTOList);
    }

}
