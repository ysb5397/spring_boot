package com.tenco.blog.board;


import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller // IoC 대상 - 싱글톤 패턴으로 관리 됨
public class BoardController {

    // DI 처리
    private final BoardRepository boardRepository;

    @GetMapping("/")
    public String index(HttpServletRequest request) {

        // 1. 게시글 목록 조회
        List<Board> boardList = boardRepository.findByAll();
        // 2. 생각해볼 사항 - Board 엔티티에는 User 엔티티와 연관관계 중
        // 연관 관계 호출 확인
        // boardList.get(0).getUser().getUsername();
        // 3. 뷰에 데이터 전달
        request.setAttribute("boardList", boardList);
        return "index";
    }


    /**
     * 게시글 상세 보기 화면 요청
     * @param id - 게시글 pk
     * @param request (뷰에 데이터 전달)
     * @return detail.mustache
     */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);
        return "board/detail";
    }

    /**
     *  주소 설계 : http://localhost:8080/board/save-form
     * @param session
     * @return
     */
    // 게시글 작성 화면 요청
    @GetMapping("/board/save-form")
    public String saveForm(HttpSession session) {
        // 권한 체크 -> 로그인 사용자만
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            // 로그인을 안한 경우 다시 로그인 페이지로 리다이렉트
            return "redirect:/login-form";
        }
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.saveDTO saveDTO, HttpSession session) {

        try {
            User sessionUser = (User) session.getAttribute("sessionUser");

            if (sessionUser == null) {
                return "redirect:/login-form";
            }

            // 유효성 검사
            saveDTO.validate();

            // 엔티티 만들기
            Board board = saveDTO.toEntity(sessionUser);
            boardRepository.save(board);
            return "redirect:/";
        } catch (Exception e) {
            return "/board/save-form";
        }
    }

    // 게시글 저장 액션 처리
    // 게시글 삭제
    // 1. 로그인을 했는지
    // 2. 글이 존재하는지
    // 3. 해당 글의 작성자인지
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, HttpSession session) {

        // 1. 로그인 체크
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        Board board = boardRepository.findById(id);

        if (board == null) {
            throw new IllegalArgumentException("이미 삭제된 글입니다.");
        }

        if (!board.isOwner(sessionUser.getId())) {
            throw new IllegalArgumentException("삭제할 권한이 없습니다.");
        }
//        if (!(sessionUser.getId() == board.getUser().getId())) {
//            throw new IllegalArgumentException("삭제할 권한이 없습니다.");
//        }

        boardRepository.deleteById(id);
        return "redirect:/";
    }

    // 게시글 수정하기 화면
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long id, HttpSession session, HttpServletRequest request) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        Board board = boardRepository.findById(id);

        if (board == null) {
            throw new RuntimeException("이미 삭제된 게시글입니다.");
        }

        if (!board.isOwner(sessionUser.getId())) {
            throw new RuntimeException("수정할 권한이 없습니다.");
        }

        request.setAttribute("board", board);

        return "board/update-form";
    }

    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long id,
                         HttpSession session,
                         BoardRequest.UpdateDTO updateDTO) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        updateDTO.validate();

        Board board = boardRepository.findById(id);

        if (board == null) {
            throw new RuntimeException("게시글이 이미 삭제처리되었습니다.");
        }

        if (!board.isOwner(sessionUser.getId())) {
            throw new RuntimeException("수정할 권한이 없습니다.");
        }

        boardRepository.updateById(id, updateDTO);

        return "redirect:/board/" + id;
    }
}
