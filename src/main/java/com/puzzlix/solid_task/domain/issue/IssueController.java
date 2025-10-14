package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task._global.config.jwt.JwtProvider;
import com.puzzlix.solid_task._global.dto.CommonResponseDto;
import com.puzzlix.solid_task.domain.issue.dto.IssueRequest;
import com.puzzlix.solid_task.domain.issue.dto.IssueResponse;
import com.puzzlix.solid_task.domain.user.Role;
import jakarta.servlet.http.HttpServletRequest;
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

        Issue issue = issueService.create(createIssue);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDto.success(new IssueResponse.FindById(issue)));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(CommonResponseDto.success(IssueResponse.FindAll.from(issueService.findAll())));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<?> find(@PathVariable("issueId") Long issueId) {
        Issue issue = issueService.find(issueId);
        return ResponseEntity.ok().body(CommonResponseDto.success(new IssueResponse.FindById(issue)));
    }

    @PutMapping("/{issueId}")
    public ResponseEntity<?> update(@PathVariable("issueId") Long issueId,
                                    @RequestBody IssueRequest.Update request,
                                    @RequestAttribute("userEmail") String userEmail) {
        Issue issue = issueService.update(issueId, request, userEmail);
        return ResponseEntity.ok().body(CommonResponseDto.success(new IssueResponse.FindById(issue)));
    }

    @PatchMapping("/{issueId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("issueId") Long issueId,
                                          @RequestParam("type") IssueStatus status,
                                          @RequestAttribute("userEmail") String userEmail) {

        Issue issue = issueService.updateStatus(issueId, status, userEmail);
        return ResponseEntity.ok().body(CommonResponseDto.success(new IssueResponse.FindById(issue)));
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<?> delete(@PathVariable("issueId") Long issueId,
                                    @RequestAttribute("userEmail") String userEmail) {
        issueService.delete(issueId, userEmail);
        return ResponseEntity.ok().body(CommonResponseDto.success(null, "성공적으로 삭제되었습니다"));
    }
}
