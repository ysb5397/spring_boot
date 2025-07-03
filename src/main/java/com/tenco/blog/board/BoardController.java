package com.tenco.blog.board;


import com.tenco.blog.reply.ReplyService;
import com.tenco.blog.user.User;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller // IoC 대상 - 싱글톤 패턴으로 관리 됨
public class BoardController {

    private final Logger log = LoggerFactory.getLogger(BoardController.class);

    /**
     * DI 처리
     */
    private final BoardService boardService;

    /**
     * 1. 게시글 목록 조회
     * 2. 생각해볼 사항 - Board 엔티티에는 User 엔티티와 연관관계 중
     * 연관 관계 호출 확인
     * boardList.get(0).getUser().getUsername();
     * 3. 뷰에 데이터 전달
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Board> boardList = boardService.findAll();
        model.addAttribute("boardList", boardList);
        return "index";
    }


    /**
     * 게시글 상세 보기 화면 요청
     *
     * @param id      - 게시글 pk
     * @return detail.mustache
     */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        Board board = boardService.findByIdWithReplies(id, sessionUser);
        model.addAttribute("board", board);
        return "board/detail";
    }

    /**
     * 주소 설계 : http://localhost:8080/board/save-form
     *
     * @return
     */
    // 게시글 작성 화면 요청
    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.saveDTO saveDTO, HttpSession session) {

        // 1. 인증 검사
        // 2. 유효성 검사
        saveDTO.validate();

        // 3. 게시글 저장 Service로 위임
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.save(saveDTO, sessionUser);
        return "redirect:/";
    }

    // 게시글 수정하기 화면
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long id, HttpServletRequest request,
                             HttpSession session) {

        // 권한 체크
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.checkBoardOwner(id, sessionUser.getId());

        request.setAttribute("board", boardService.findById(id));
        return "board/update-form";
    }

    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long id,
                         HttpSession session,
                         BoardRequest.UpdateDTO updateDTO) {

        // 1. 인증 검사
        // 2. 데이터 유효성 검사
        updateDTO.validate();
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 3. 수정 요청 -> Service
        boardService.updateById(id, updateDTO, sessionUser);

        // 4. 리다이렉트
        return "redirect:/board/" + id;
    }

    /**
     * @param id
     * @param session
     * @return
     */
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, HttpSession session) {

        // 1. 인증 검사
        // 2. 세션에서 로그인 한 사용자 정보 추출
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 3. 서비스 위임
        boardService.deleteById(id, sessionUser);

        // 4. 메인 페이지로 리다이렉트 처리
        return "redirect:/";
    }
}
