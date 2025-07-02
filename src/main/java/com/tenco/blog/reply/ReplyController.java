package com.tenco.blog.reply;

import com.tenco.blog.board.Board;
import com.tenco.blog.user.User;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

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
}
