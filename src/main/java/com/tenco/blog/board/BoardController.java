package com.tenco.blog.board;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller // IoC 대상 - 싱글톤 패턴으로 관리 됨
public class BoardController {

    // 자동으로 DI 처리
    private final BoardPersistRepository br;

    // 화면 연결 처리
    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    // 게시글 작성 액션(수행) 처리
    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO reqDTO) {
        // HTTP 요청 본문 : title=값&content=값&username=값
        // form MIME (application/x-www-form-urlencoded)

        // DTO를 받아서 Board 객체를 만들고 넣어야 함
        Board board = reqDTO.toEntity();
        br.save(board);

        // PRG 패턴
        return "redirect:/";
    }

    @GetMapping({"/", "/index"})
    public String boardList(HttpServletRequest request) {
        List<Board> boardList = br.findAll();

        request.setAttribute("boardList", boardList);
        return "index";
    }

    // 게시물 상세보기
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") int id, HttpServletRequest request) {

        // 1차 캐시 효과 -> DB에 접근하지 않고 바로 영속성 컨텍스트에서 꺼냄
        Board board = br.findById(id);
        request.setAttribute("board", board);

        return "board/detail";
    }
}
