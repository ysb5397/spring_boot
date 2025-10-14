package com.puzzlix.solid_task.domain.issue;

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
    public ResponseEntity<?> update(@PathVariable("issueId") Long issueId, @RequestBody IssueRequest.Update request) {
        Issue issue = issueService.update(issueId, request);
        return ResponseEntity.ok().body(CommonResponseDto.success(new IssueResponse.FindById(issue)));
    }
}
