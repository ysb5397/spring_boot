package com.tenco.blog.controller;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // IoC 대상 - 싱글톤 패턴으로 관리 됨
public class BoardController {

    private BoardNativeRepository boardNativeRepository;

    // DI : 의존성 주입 : 스프링이 자동으로 객체 주입
    public BoardController(BoardNativeRepository boardNativeRepository) {
        this.boardNativeRepository = boardNativeRepository;
    }

    @GetMapping({"/", "/index"})
    // public String index(Model model)
    public String index(HttpServletRequest request) {
        // DB에 접근해 결과값을 받아 mustache 파일에 바인딩 처리
        List<Board> boardList = boardNativeRepository.findAll();

        // 뷰에 데이터를 전달 -> model 사용가능
        request.setAttribute("boardList", boardList);

        return "index";
    }

    @GetMapping("/board/save-form")
    public String saveFrom() {
        // /templates//board
        // /templates/board/
        return "board/save-form";
    }

    /**
     * 상세보기 화면 요청
     * board/1
     */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
        // URL 에서 받은 id 값을 사용해서 특정 게시글 상세보기 조회
        // 실제로는 이 id값으로 데이터베이스에 있는 게시글 조회 하고
        // 머스태치 파일로 데이터를 내려 주어야 함 (Model)
        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board", board);

        return "board/detail";
    }

    @PostMapping("/board/save")
    // username, title, content --> dto를 통해서 받는 방법, 기본 데이터 타입 설정
    // form 태그에서 넘어오는 데이터 받기
    public String save(@RequestParam("title") String title,
                       @RequestParam("content") String content,
                       @RequestParam("username") String username) {
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        System.out.println("username : " + username);

        boardNativeRepository.save(title, content, username);

        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") int id) {
        boardNativeRepository.deleteById(id);

        // PRG 패턴(POST / Redirect / GET 적용
        // 삭제 후 메인 페이지로 리다이렉트 하여 중복 삭제 방지
        // 새로고침을 하더라도 삭제가 재실행 되지 않음
        return "redirect:/";
    }

    // 게시글 수정 화면 요청 GET 방식
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") int id, HttpServletRequest request) {

        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board", board);

        return "board/update-form";
    }

    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") int id,
                         @RequestParam(name = "title") String title,
                         @RequestParam(name = "content") String content,
                         @RequestParam(name = "username") String username,
                         HttpServletRequest request) {

        boardNativeRepository.updateById(id, title, content, username);

        // PRG 패턴 적용
        // 수정 완료 후 상세보기 페이지로 리다이렉트
        // 게시글 상세보기 페이지
        return "redirect:/board/" + id;
    }
}
