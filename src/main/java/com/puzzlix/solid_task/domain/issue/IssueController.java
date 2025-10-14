package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task._global.config.jwt.JwtProvider;
import com.puzzlix.solid_task._global.dto.CommonResponseDto;
import com.puzzlix.solid_task.domain.issue.dto.IssueRequest;
import com.puzzlix.solid_task.domain.issue.dto.IssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final JwtProvider jwtProvider;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody IssueRequest.Create createIssue) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDto.success(issueService.create(createIssue)));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(CommonResponseDto.success(issueService.findAll()));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<?> find(@PathVariable("issueId") Long issueId) {
        return ResponseEntity.ok().body(CommonResponseDto.success(issueService.find(issueId)));
    }

    @PutMapping("/{issueId}")
    public ResponseEntity<?> update(@PathVariable("issueId") Long issueId,
                                    @RequestBody IssueRequest.Update request,
                                    @RequestAttribute("userEmail") String userEmail) {
        return ResponseEntity.ok().body(CommonResponseDto.success(issueService.update(issueId, request, userEmail)));
    }

    @PatchMapping("/{issueId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("issueId") Long issueId,
                                          @RequestParam("type") IssueStatus status,
                                          @RequestAttribute("userEmail") String userEmail) {

        return ResponseEntity.ok().body(CommonResponseDto.success(issueService.updateStatus(issueId, status, userEmail)));
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<?> delete(@PathVariable("issueId") Long issueId,
                                    @RequestAttribute("userEmail") String userEmail) {
        issueService.delete(issueId, userEmail);
        return ResponseEntity.ok().body(CommonResponseDto.success(null, "성공적으로 삭제되었습니다"));
    }
}
