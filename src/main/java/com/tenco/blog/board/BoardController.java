package com.tenco.blog.board;


import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
    private final BoardRepository boardRepository;

    /**
     * 1. 게시글 목록 조회
     * 2. 생각해볼 사항 - Board 엔티티에는 User 엔티티와 연관관계 중
     * 연관 관계 호출 확인
     * boardList.get(0).getUser().getUsername();
     * 3. 뷰에 데이터 전달
     */
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        log.info("메인 페이지 요청");
        List<Board> boardList = boardRepository.findByAll();

        log.info("현재 가지고 온 게시글 갯수 : {}", boardList.size());
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
        log.info("게시글 상세 보기 요청 - id: {}", id);
        Board board = boardRepository.findById(id);
        log.info("게시글 상세 보기 조회 완료 - 제목: {}, 작성자: {}", board.getTitle(), board.getUser().getUsername());
        request.setAttribute("board", board);
        return "board/detail";
    }

    /**
     *  주소 설계 : http://localhost:8080/board/save-form
     * @return
     */
    // 게시글 작성 화면 요청
    @GetMapping("/board/save-form")
    public String saveForm() {

        log.info("게시글 작성 화면 요청");
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.saveDTO saveDTO, HttpSession session) {
        log.info("게시글 작성 요청");

        User sessionUser = (User) session.getAttribute("sessionUser");

        // 유효성 검사
        saveDTO.validate();

        // 엔티티 만들기
        Board board = saveDTO.toEntity(sessionUser);
        boardRepository.save(board);
        return "redirect:/";
    }

    /**
     *
     * @param id
     * @param session
     * @return
     */
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, HttpSession session) {

        log.info("게시글 삭제 요청 - id: {}", id);

        // 1. 로그인 체크
        User sessionUser = (User) session.getAttribute("sessionUser");

        Board board = boardRepository.findById(id);

        if (board == null) {
            throw new Exception404("게시글이 존재하지 않습니다.");
        }

        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("게시글 삭제 권한이 없습니다.");
        }

        boardRepository.deleteById(id);
        return "redirect:/";
    }

    // 게시글 수정하기 화면
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long id, HttpSession session, HttpServletRequest request) {

        log.info("게시글 수정 폼 요청 - boardId: {}", id);

        User sessionUser = (User) session.getAttribute("sessionUser");


        Board board = boardRepository.findById(id);

        if (board == null) {
            throw new Exception404("게시글이 존재하지 않습니다.");
        }

        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("게시글 수정 권한이 없습니다.");
        }

        request.setAttribute("board", board);

        return "board/update-form";
    }

    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long id,
                         HttpSession session,
                         BoardRequest.UpdateDTO updateDTO) {

        log.info("게시글 수정 기능 요청 - boardId: {}, 새 제목: {}", id, updateDTO.getTitle());
        User sessionUser = (User) session.getAttribute("sessionUser");



        updateDTO.validate();

        Board board = boardRepository.findById(id);

        if (board == null) {
            throw new Exception404("게시글을 찾을 수 없습니다.");
        }

        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("게시글 수정 권한이 없습니다.");
        }

        boardRepository.updateById(id, updateDTO);

        return "redirect:/board/" + id;
    }
}
