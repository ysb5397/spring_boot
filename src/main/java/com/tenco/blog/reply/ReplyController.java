package com.tenco.blog.reply;

import com.tenco.blog.board.Board;
import com.tenco.blog.user.User;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/reply/save")
    public String save(ReplyRequest.SaveDTO saveDTO, HttpSession session) {
        saveDTO.validate();

        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        replyService.save(saveDTO, sessionUser);

        return "redirect:/board/" + saveDTO.getBoardId();
    }

    // 댓글 삭제
    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, HttpSession session,
                        @RequestParam(name = "boardId") Long boardId) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        replyService.deleteById(id, sessionUser);


        return "redirect:/board/" + boardId;
    }
}
