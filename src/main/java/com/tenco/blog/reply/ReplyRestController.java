package com.tenco.blog.reply;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog._core.utils.Define;
import com.tenco.blog.user.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReplyRestController {

    private final ReplyService replyService;

    // 댓글 저장 기능 요청
    @PostMapping("/api/replies")
    public ResponseEntity<?> save(@RequestBody @Valid ReplyRequest.SaveDTO saveDTO,
                                                               Errors errors,
                                                               @RequestAttribute(name = Define.SESSION_USER) SessionUser sessionUser) {

        ReplyResponse.SaveDTO saveReply = replyService.save(saveDTO, sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiUtil<>(saveReply));
    }

    // 댓글 삭제 기능 요청
    @PostMapping("/api/replies/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long replyId,
                                                  @RequestParam(name = "boardId") Long boardId,
                                                  @RequestAttribute(name = Define.SESSION_USER) SessionUser sessionUser) {

        replyService.deleteById(replyId, sessionUser);
        return ResponseEntity.ok().body(new ApiUtil<>("삭제 성공"));
    }
}
